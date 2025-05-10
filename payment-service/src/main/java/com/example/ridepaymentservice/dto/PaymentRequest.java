package com.example.ridepaymentservice.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long rideId;
    private Long userId;
    private String paymentMethod;
    private String paymentToken;
}
