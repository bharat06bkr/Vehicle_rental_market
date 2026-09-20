package com.vehiclerental.config;

import com.vehiclerental.entity.*;
import com.vehiclerental.enums.*;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleCategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Seed Locations
        if (locationRepository.count() == 0) {
            Location loc1 = locationRepository.save(new Location("Gachibowli", "Hyderabad", "Telangana"));
            Location loc2 = locationRepository.save(new Location("Koramangala", "Bangalore", "Karnataka"));
            Location loc3 = locationRepository.save(new Location("T Nagar", "Chennai", "Tamil Nadu"));
            Location loc4 = locationRepository.save(new Location("Bypass Road", "Tirupati", "Andhra Pradesh"));
            Location loc5 = locationRepository.save(new Location("Benz Circle", "Vijayawada", "Andhra Pradesh"));
        }

        // Seed Categories
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new VehicleCategory("HATCHBACK", "Compact and fuel-efficient cars"));
            categoryRepository.save(new VehicleCategory("SEDAN", "Comfortable 5-seater sedans"));
            categoryRepository.save(new VehicleCategory("SUV", "Spacious family SUVs"));
            categoryRepository.save(new VehicleCategory("LUXURY", "High-end premium luxury vehicles"));
            categoryRepository.save(new VehicleCategory("ELECTRIC", "Eco-friendly zero emission electric vehicles"));
            categoryRepository.save(new VehicleCategory("BIKE", "Two-wheelers and motorcycles"));
        }

        // Seed Admin User
        if (!userRepository.existsByEmail("admin@vehiclerental.com")) {
            User admin = new User(
                    "System Admin",
                    "admin@vehiclerental.com",
                    passwordEncoder.encode("admin123"),
                    "9999999999",
                    Role.ROLE_ADMIN,
                    UserStatus.APPROVED
            );
            userRepository.save(admin);
        }

        // Seed Demo Approved Owner
        User owner = userRepository.findByEmail("owner@vehiclerental.com").orElseGet(() -> {
            User newOwner = new User(
                    "Rajesh Owner",
                    "owner@vehiclerental.com",
                    passwordEncoder.encode("owner123"),
                    "9876543210",
                    Role.ROLE_OWNER,
                    UserStatus.APPROVED
            );
            return userRepository.save(newOwner);
        });

        // Seed Demo Customer
        if (!userRepository.existsByEmail("customer@vehiclerental.com")) {
            User customer = new User(
                    "Priya Customer",
                    "customer@vehiclerental.com",
                    passwordEncoder.encode("customer123"),
                    "9123456789",
                    Role.ROLE_CUSTOMER,
                    UserStatus.APPROVED
            );
            userRepository.save(customer);
        }

        // Seed Sample Vehicles
        if (vehicleRepository.count() == 0) {
            VehicleCategory hatchback = categoryRepository.findByName("HATCHBACK").orElse(null);
            VehicleCategory sedan = categoryRepository.findByName("SEDAN").orElse(null);
            VehicleCategory suv = categoryRepository.findByName("SUV").orElse(null);
            VehicleCategory electric = categoryRepository.findByName("ELECTRIC").orElse(null);

            Location hyd = locationRepository.findByCity("Hyderabad").stream().findFirst().orElse(null);
            Location blr = locationRepository.findByCity("Bangalore").stream().findFirst().orElse(null);

            if (hatchback != null && hyd != null) {
                Vehicle v1 = new Vehicle();
                v1.setOwner(owner);
                v1.setCategory(hatchback);
                v1.setLocation(hyd);
                v1.setVehicleNumber("TS-09-AB-1234");
                v1.setBrand("Hyundai");
                v1.setModel("i20 Asta");
                v1.setManufacturingYear(2022);
                v1.setFuelType(FuelType.PETROL);
                v1.setTransmission(TransmissionType.MANUAL);
                v1.setSeatingCapacity(5);
                v1.setPricePerDay(new BigDecimal("2200.00"));
                v1.setDescription("Well maintained Hyundai i20 with touchscreen, sunroof, and climate control.");
                v1.setApprovalStatus(VehicleApprovalStatus.APPROVED);
                v1.setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);
                vehicleRepository.save(v1);
            }

            if (sedan != null && blr != null) {
                Vehicle v2 = new Vehicle();
                v2.setOwner(owner);
                v2.setCategory(sedan);
                v2.setLocation(blr);
                v2.setVehicleNumber("KA-01-CD-5678");
                v2.setBrand("Honda");
                v2.setModel("City ZX");
                v2.setManufacturingYear(2023);
                v2.setFuelType(FuelType.PETROL);
                v2.setTransmission(TransmissionType.AUTOMATIC);
                v2.setSeatingCapacity(5);
                v2.setPricePerDay(new BigDecimal("3500.00"));
                v2.setDescription("Premium automatic Honda City with ADAS, leather seats, and high highway mileage.");
                v2.setApprovalStatus(VehicleApprovalStatus.APPROVED);
                v2.setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);
                vehicleRepository.save(v2);
            }

            if (suv != null && hyd != null) {
                Vehicle v3 = new Vehicle();
                v3.setOwner(owner);
                v3.setCategory(suv);
                v3.setLocation(hyd);
                v3.setVehicleNumber("TS-07-EF-9012");
                v3.setBrand("Mahindra");
                v3.setModel("XUV700 AX7");
                v3.setManufacturingYear(2023);
                v3.setFuelType(FuelType.DIESEL);
                v3.setTransmission(TransmissionType.AUTOMATIC);
                v3.setSeatingCapacity(7);
                v3.setPricePerDay(new BigDecimal("4800.00"));
                v3.setDescription("Powerful 7-seater Diesel SUV ideal for long road trips and family vacations.");
                v3.setApprovalStatus(VehicleApprovalStatus.APPROVED);
                v3.setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);
                vehicleRepository.save(v3);
            }
        }
    }
}
