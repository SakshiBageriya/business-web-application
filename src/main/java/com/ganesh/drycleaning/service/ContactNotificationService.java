package com.ganesh.drycleaning.service;

import com.ganesh.drycleaning.entity.BusinessSettings;
import com.ganesh.drycleaning.entity.ContactMessage;
import com.ganesh.drycleaning.repository.BusinessSettingsRepository;
import com.ganesh.drycleaning.repository.ContactMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

@Service
public class ContactNotificationService {
    private static final Logger log = LoggerFactory.getLogger(ContactNotificationService.class);

    private final ContactMessageRepository messages;
    private final BusinessSettingsRepository settings;
    private final ObjectProvider<JavaMailSender> mailSenders;

    @Value("${app.notifications.email.from:}")
    private String emailFrom;

    public ContactNotificationService(
            ContactMessageRepository messages,
            BusinessSettingsRepository settings,
            ObjectProvider<JavaMailSender> mailSenders) {
        this.messages = messages;
        this.settings = settings;
        this.mailSenders = mailSenders;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onContactCreated(ContactCreatedEvent event) {
        ContactMessage contact = messages.findById(event.messageId()).orElse(null);
        if (contact == null) {
            log.warn("Contact email notification skipped: message {} was not found", event.messageId());
            return;
        }

        BusinessSettings business = settings.findById(1L).orElse(null);
        if (business == null) {
            log.warn("Contact email notification skipped: business settings are not initialized");
            return;
        }

        sendEmail(contact, business);
    }

    private void sendEmail(ContactMessage contact, BusinessSettings business) {
        JavaMailSender mailSender = mailSenders.getIfAvailable();
        if (mailSender == null || !StringUtils.hasText(emailFrom) || !StringUtils.hasText(business.getEmail())) {
            log.info("Contact email notification skipped: sender or business email is not configured");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(business.getEmail());
            helper.setSubject("New website enquiry #" + contact.getId());
            helper.setText(emailBody(contact), false);
            mailSender.send(message);
        } catch (MessagingException | RuntimeException e) {
            log.error("Could not email website enquiry #{}", contact.getId(), e);
        }
    }

    private String emailBody(ContactMessage contact) {
        return "A new message was submitted through the website contact form.\n\n"
                + "Message: #" + contact.getId() + "\n"
                + "Name: " + safe(contact.getName()) + "\n"
                + "Phone: " + safe(contact.getPhone()) + "\n"
                + "Email: " + safe(contact.getEmail()) + "\n\n"
                + "Enquiry:\n" + safe(contact.getMessage());
    }

    private String safe(String value) {
        return StringUtils.hasText(value) ? value : "Not provided";
    }
}
