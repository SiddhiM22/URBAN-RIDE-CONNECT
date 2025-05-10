package com.example.ridepaymentservice.service;

import com.example.ridepaymentservice.dto.PaymentRequest;
import com.example.ridepaymentservice.dto.PaymentResponse;
import com.example.ridepaymentservice.exception.PaymentException;
import com.example.ridepaymentservice.exception.ResourceNotFoundException;
import com.example.ridepaymentservice.model.Payment;
import com.example.ridepaymentservice.model.Ride;
import com.example.ridepaymentservice.model.app_User;
import com.example.ridepaymentservice.repository.PaymentRepository;
import com.example.ridepaymentservice.repository.RideRepository;
import com.example.ridepaymentservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RideRepository rideRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Fetch ride details
        Ride ride = rideRepository.findById(paymentRequest.getRideId())
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + paymentRequest.getRideId()));

        // Check if ride is completed
        if (!"COMPLETED".equals(ride.getStatus())) {
            throw new PaymentException("Cannot process payment for a ride that is not completed");
        }

        // Check if payment already exists
        paymentRepository.findByRideId(ride.getId())
                .ifPresent(p -> {
                    if ("SUCCESS".equals(p.getStatus())) {
                        throw new PaymentException("Payment already processed for this ride");
                    }
                });

        // Fetch user details
        app_User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paymentRequest.getUserId()));

        // Calculate payment
        double ratePerKm = 3.0;
        double amount = ride.getBaseFare() + (ride.getDistanceInKm() * ratePerKm);

        // Create payment record
        Payment payment = new Payment();
        payment.setRide(ride);
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setCurrency("USD");
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setTimestamp(LocalDateTime.now());

        // Simulate payment processing
        try {
            String transactionId = processPaymentWithGateway(user, amount, paymentRequest.getPaymentToken());
            payment.setTransactionId(transactionId);
            payment.setStatus("SUCCESS");
        } catch (Exception e) {
            payment.setStatus("FAILED");
            payment.setFailureReason(e.getMessage());
            paymentRepository.save(payment);
            throw new PaymentException("Payment processing failed: " + e.getMessage());
        }

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // Update ride status
        if ("SUCCESS".equals(payment.getStatus())) {
            ride.setStatus("PAID");
            rideRepository.save(ride);
        }

        return mapToPaymentResponse(savedPayment);
    }


    private String processPaymentWithGateway(app_User user, Double amount, String paymentToken) {
        // Simulate payment gateway processing
        // In a real application, this would integrate with Stripe, PayPal, etc.
        if (amount <= 0) {
            throw new PaymentException("Invalid payment amount");
        }

        if (paymentToken == null || paymentToken.isEmpty()) {
            throw new PaymentException("Invalid payment token");
        }

        // Simulate successful payment
        return UUID.randomUUID().toString();
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToPaymentResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToPaymentResponse(payment);
    }

    public PaymentResponse getPaymentByRideId(Long rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride id: " + rideId));
        return mapToPaymentResponse(payment);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .rideId(payment.getRide().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .timestamp(payment.getTimestamp())
                .failureReason(payment.getFailureReason())
                .build();
    }
}