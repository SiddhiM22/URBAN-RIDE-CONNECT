package com.example.ridepaymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private app_User user;

    private Double amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime timestamp;
    private String failureReason;

    public Payment(Ride ride, app_User user, Double amount, String currency, String paymentMethod,
                   String transactionId, String status, LocalDateTime timestamp, String failureReason) {
        this.ride = ride;
        this.user = user;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.timestamp = timestamp;
        this.failureReason = failureReason; // Can be null in case of success
    }
}
