package com.example.ridepaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long rideId;
    private Double amount;
    private String currency;
    private String status;
    private String transactionId;
    private LocalDateTime timestamp;
    private String failureReason;
}