package com.ganesh.drycleaning.config;

import com.ganesh.drycleaning.entity.*;
import com.ganesh.drycleaning.repository.*;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ServiceItemRepository items;
    private final BusinessSettingsRepository settings;
    private final AdminUserRepository users;
    private final PasswordEncoder encoder;
    private final String username, password;

    public DataInitializer(ServiceItemRepository items, BusinessSettingsRepository settings, AdminUserRepository users, PasswordEncoder encoder, @Value("${app.admin.username}") String username, @Value("${app.admin.password}") String password) {
        this.items = items;
        this.settings = settings;
        this.users = users;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (items.count() == 0) {
            add("Ironing and Pressing", "Simple Ironing", "Thoughtful fabric-specific care for simple ironing. Treatment is confirmed after garment inspection.", 1);
            add("Ironing and Pressing", "Paper Press", "Thoughtful fabric-specific care for paper press. Treatment is confirmed after garment inspection.", 2);
            add("Ironing and Pressing", "Roll Press", "Thoughtful fabric-specific care for roll press. Treatment is confirmed after garment inspection.", 3);
            add("Ironing and Pressing", "Jacket Press", "Thoughtful fabric-specific care for jacket press. Treatment is confirmed after garment inspection.", 4);
            add("Ironing and Pressing", "Professional Garment Pressing", "Thoughtful fabric-specific care for professional garment pressing. Treatment is confirmed after garment inspection.", 5);
            add("Laundry and Wash & Iron", "Regular Clothes Washing", "Thoughtful fabric-specific care for regular clothes washing. Treatment is confirmed after garment inspection.", 6);
            add("Laundry and Wash & Iron", "Wash and Iron", "Thoughtful fabric-specific care for wash and iron. Treatment is confirmed after garment inspection.", 7);
            add("Laundry and Wash & Iron", "Family Laundry", "Thoughtful fabric-specific care for family laundry. Treatment is confirmed after garment inspection.", 8);
            add("Laundry and Wash & Iron", "Daily Wear Garment Care", "Thoughtful fabric-specific care for daily wear garment care. Treatment is confirmed after garment inspection.", 9);
            add("Premium and Ethnic Wear", "Lehenga", "Thoughtful fabric-specific care for lehenga. Treatment is confirmed after garment inspection.", 10);
            add("Premium and Ethnic Wear", "Heavy Lehenga", "Thoughtful fabric-specific care for heavy lehenga. Treatment is confirmed after garment inspection.", 11);
            add("Premium and Ethnic Wear", "Saree", "Thoughtful fabric-specific care for saree. Treatment is confirmed after garment inspection.", 12);
            add("Premium and Ethnic Wear", "Heavy Saree", "Thoughtful fabric-specific care for heavy saree. Treatment is confirmed after garment inspection.", 13);
            add("Premium and Ethnic Wear", "Designer Saree", "Thoughtful fabric-specific care for designer saree. Treatment is confirmed after garment inspection.", 14);
            add("Premium and Ethnic Wear", "Sherwani", "Thoughtful fabric-specific care for sherwani. Treatment is confirmed after garment inspection.", 15);
            add("Premium and Ethnic Wear", "Heavy Suit", "Thoughtful fabric-specific care for heavy suit. Treatment is confirmed after garment inspection.", 16);
            add("Professional Dry Cleaning", "Suits and Blazers", "Thoughtful fabric-specific care for suits and blazers. Treatment is confirmed after garment inspection.", 17);
            add("Professional Dry Cleaning", "Dresses and Gowns", "Thoughtful fabric-specific care for dresses and gowns. Treatment is confirmed after garment inspection.", 18);
            add("Professional Dry Cleaning", "Shirts and Trousers", "Thoughtful fabric-specific care for shirts and trousers. Treatment is confirmed after garment inspection.", 19);
            add("Winter Wear and Blankets", "Jackets and Coats", "Thoughtful fabric-specific care for jackets and coats. Treatment is confirmed after garment inspection.", 20);
            add("Winter Wear and Blankets", "Sweaters and Shawls", "Thoughtful fabric-specific care for sweaters and shawls. Treatment is confirmed after garment inspection.", 21);
            add("Winter Wear and Blankets", "Blankets and Quilts", "Thoughtful fabric-specific care for blankets and quilts. Treatment is confirmed after garment inspection.", 22);
        }
        if (!settings.existsById(1L)) {
            BusinessSettings s = new BusinessSettings();
            s.setId(1L);
            s.setPhone("[Add phone in admin]");
            s.setWhatsapp("[Add WhatsApp in admin]");
            s.setEmail("[Add email in admin]");
            s.setHours("[Add opening hours in admin]");
            s.setBranches("[Add branches and addresses in admin]");
            s.setMapsUrl("[Add Maps URL in admin]");
            s.setInstagram("[Add Instagram URL in admin]");
            s.setFacebook("[Add Facebook URL in admin]");
            s.setExperience("25+ Years of Experience");
            s.setCareClaim("Professional Garment Care");
            s.setFabricClaim("Attention to Fabric and Color");
            s.setCustomerClaim("Customer-Focused Service");
            settings.save(s);
        }
        if (users.findByUsername(username).isEmpty()) {
            AdminUser u = new AdminUser();
            u.setUsername(username);
            u.setPasswordHash(encoder.encode(password));
            users.save(u);
        }
    }

    private void add(String category, String name, String description, int sort) {
        ServiceItem s = new ServiceItem();
        s.setCategory(category);
        s.setName(name);
        s.setDescription(description);
        s.setSortOrder(sort);
        items.save(s);
    }
}