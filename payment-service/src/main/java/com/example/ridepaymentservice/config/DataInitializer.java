package com.example.ridepaymentservice.config;

import com.example.ridepaymentservice.model.Payment;
import com.example.ridepaymentservice.model.Ride;
import com.example.ridepaymentservice.model.app_User;
import com.example.ridepaymentservice.repository.PaymentRepository;
import com.example.ridepaymentservice.repository.RideRepository;
import com.example.ridepaymentservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, RideRepository rideRepository,PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void run(String... args) {
        // Create sample users
        app_User rider1 = new app_User(null, "John Doe", "john@example.com", "CREDIT_CARD", "tok_visa");
        app_User rider2 = new app_User(null, "Jane Smith", "jane@example.com", "PAYPAL", "tok_paypal");
        app_User driver1 = new app_User(null, "Mike Driver", "mike@example.com", "BANK_ACCOUNT", "acct_123456");
        app_User driver2 = new app_User(null, "Sarah Driver", "sarah@example.com", "BANK_ACCOUNT", "acct_789012");

        // Save users
        List<app_User> users = userRepository.saveAll(Arrays.asList(rider1, rider2, driver1, driver2));

        double pricePerKm = 2.5;
        // Create sample rides with saved users
        Ride ride1 = new Ride(
                rider1,
                driver1,
                "123 Main St",
                "456 Park Ave",
                5.2, // distance in km
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1),
                10.0, // base fare
                0.0,  // total fare (will be calculated)
                "COMPLETED"
        );
        ride1.calculateTotalFare(pricePerKm);

        Ride ride2 = new Ride(
                rider2,
                driver2,
                "789 Broadway",
                "101 Fifth Ave",
                3.8, // distance in km
                LocalDateTime.now().minusHours(3),
                LocalDateTime.now().minusHours(2),
                8.0, // base fare
                0.0,  // total fare (will be calculated)
                "COMPLETED"
        );
        ride2.calculateTotalFare(pricePerKm); // Calculate the total fare based on distance and price per km

        Ride ride3 = new Ride(
                rider1,
                driver2,
                "222 Oak St",
                "333 Pine St",
                2.5, // distance in km
                LocalDateTime.now().minusMinutes(45),
                LocalDateTime.now().minusMinutes(15),
                6.0, // base fare
                0.0,  // total fare (will be calculated)
                "COMPLETED"
        );
        ride3.calculateTotalFare(pricePerKm); // Calculate the total fare based on distance and price per km

        rideRepository.saveAll(Arrays.asList(ride1, ride2, ride3));

        // Create and save payments
        Payment payment1 = new Payment(
                ride1,
                rider1,
                ride1.getTotalFare(),
                "INR",
                "CREDIT_CARD",
                UUID.randomUUID().toString(),
                "PENDING",
                LocalDateTime.now(),
                null // No failure reason since payment is successful
        );

        Payment payment2 = new Payment(
                ride2,
                rider2,
                ride2.getTotalFare(),
                "INR",
                "PAYPAL",
                UUID.randomUUID().toString(),
                "SUCCESS",
                LocalDateTime.now(),
                null // No failure reason since payment is successful
        );

        Payment payment3 = new Payment(
                ride3,
                rider1,
                ride3.getTotalFare(),
                "INR",
                "CREDIT_CARD",
                UUID.randomUUID().toString(),
                "SUCCESS",
                LocalDateTime.now(),
                null // No failure reason since payment is successful
        );

        // Save payments to the database
        paymentRepository.saveAll(Arrays.asList(payment1, payment2, payment3));

        System.out.println("Sample data initialized!");
    }
}
