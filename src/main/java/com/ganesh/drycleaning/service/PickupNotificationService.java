package com.ganesh.drycleaning.service;

import com.ganesh.drycleaning.entity.BusinessSettings;
import com.ganesh.drycleaning.entity.PickupRequest;
import com.ganesh.drycleaning.repository.BusinessSettingsRepository;
import com.ganesh.drycleaning.repository.PickupRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@Service
public class PickupNotificationService {
    private static final Logger log = LoggerFactory.getLogger(PickupNotificationService.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final PickupRequestRepository pickups;
    private final BusinessSettingsRepository settings;
    private final ObjectProvider<JavaMailSender> mailSenders;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${app.notifications.email.from:}")
    private String emailFrom;

    @Value("${app.notifications.twilio.account-sid:}")
    private String twilioAccountSid;

    @Value("${app.notifications.twilio.auth-token:}")
    private String twilioAuthToken;

    @Value("${app.notifications.twilio.whatsapp-from:}")
    private String twilioWhatsappFrom;

    @Value("${app.notifications.twilio.whatsapp-content-sid:}")
    private String twilioWhatsappContentSid;

    public PickupNotificationService(
            PickupRequestRepository pickups,
            BusinessSettingsRepository settings,
            ObjectProvider<JavaMailSender> mailSenders,
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper) {
        this.pickups = pickups;
        this.settings = settings;
        this.mailSenders = mailSenders;
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPickupCreated(PickupCreatedEvent event) {
        PickupRequest pickup = pickups.findById(event.pickupId()).orElse(null);
        if (pickup == null) {
            log.warn("Pickup notification skipped: pickup {} was not found", event.pickupId());
            return;
        }

        BusinessSettings business = settings.findById(1L).orElse(null);
        if (business == null) {
            log.warn("Pickup notification skipped: business settings are not initialized");
            return;
        }

        sendEmail(pickup, business);
        sendWhatsApp(pickup, business);
    }

    private void sendEmail(PickupRequest pickup, BusinessSettings business) {
        JavaMailSender mailSender = mailSenders.getIfAvailable();
        if (mailSender == null || !StringUtils.hasText(emailFrom) || !StringUtils.hasText(business.getEmail())) {
            log.info("Pickup email notification skipped: sender or business email is not configured");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(business.getEmail());
            helper.setSubject("New pickup request #" + pickup.getId());
            helper.setText(emailBody(pickup), false);
            mailSender.send(message);
        } catch (MessagingException | RuntimeException e) {
            log.error("Could not email pickup request #{}", pickup.getId(), e);
        }
    }

    private String emailBody(PickupRequest pickup) {
        return "A new pickup request was submitted.\n\n"
                + "Request: #" + pickup.getId() + "\n"
                + "Name: " + safe(pickup.getName()) + "\n"
                + "Phone: " + safe(pickup.getPhone()) + "\n"
                + "Email: " + safe(pickup.getEmail()) + "\n"
                + "Address: " + safe(pickup.getAddress()) + "\n"
                + "Service: " + safe(pickup.getService()) + "\n"
                + "Preferred date: " + (pickup.getPreferredDate() == null ? "Not specified" : pickup.getPreferredDate().format(DATE_FORMAT)) + "\n"
                + "Preferred time: " + safe(pickup.getPreferredTime()) + "\n"
                + "Notes: " + safe(pickup.getNotes());
    }

    private void sendWhatsApp(PickupRequest pickup, BusinessSettings business) {
        if (!StringUtils.hasText(twilioAccountSid)
                || !StringUtils.hasText(twilioAuthToken)
                || !StringUtils.hasText(twilioWhatsappFrom)
                || !StringUtils.hasText(business.getWhatsapp())) {
            log.info("Pickup WhatsApp notification skipped: Twilio or business WhatsApp is not configured");
            return;
        }

        String destination = whatsappAddress(business.getWhatsapp());
        String sender = whatsappAddress(twilioWhatsappFrom);
        if (destination == null || sender == null) {
            log.warn("Pickup WhatsApp notification skipped: a WhatsApp number is not in international format");
            return;
        }

        String credentials = Base64.getEncoder().encodeToString(
                (twilioAccountSid + ":" + twilioAuthToken).getBytes(StandardCharsets.UTF_8));
        String url = "https://api.twilio.com/2010-04-01/Accounts/"
                + UriUtils.encodePathSegment(twilioAccountSid, StandardCharsets.UTF_8)
                + "/Messages.json";

        try {
            LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("From", sender);
            form.add("To", destination);
            if (StringUtils.hasText(twilioWhatsappContentSid)) {
                form.add("ContentSid", twilioWhatsappContentSid);
                form.add("ContentVariables", objectMapper.writeValueAsString(Map.of(
                        "1", String.valueOf(pickup.getId()),
                        "2", safe(pickup.getName()),
                        "3", safe(pickup.getPhone()),
                        "4", safe(pickup.getService()),
                        "5", safe(pickup.getAddress()),
                        "6", whatsappPreferredTime(pickup))));
            } else {
                form.add("Body", whatsappBody(pickup));
            }

            restClient.post()
                    .uri(url)
                    .header("Authorization", "Basic " + credentials)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .toBodilessEntity();
        } catch (JsonProcessingException | RuntimeException e) {
            log.error("Could not send WhatsApp notification for pickup request #{}", pickup.getId(), e);
        }
    }

    private String whatsappBody(PickupRequest pickup) {
        return "New pickup request #" + pickup.getId()
                + "\nName: " + safe(pickup.getName())
                + "\nPhone: " + safe(pickup.getPhone())
                + "\nService: " + safe(pickup.getService())
                + "\nAddress: " + safe(pickup.getAddress())
                + "\nPreferred: " + whatsappPreferredTime(pickup);
    }

    private String whatsappPreferredTime(PickupRequest pickup) {
        return (pickup.getPreferredDate() == null
                ? "Not specified"
                : pickup.getPreferredDate().format(DATE_FORMAT))
                + " " + safe(pickup.getPreferredTime());
    }

    private String whatsappAddress(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        if (digits.length() < 8 || digits.length() > 15) {
            return null;
        }
        return "whatsapp:+" + digits;
    }

    private String safe(String value) {
        return StringUtils.hasText(value) ? value : "Not provided";
    }
}
