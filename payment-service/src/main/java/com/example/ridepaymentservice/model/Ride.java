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
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private app_User rider;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private app_User driver;

    private String pickupLocation;
    private String dropoffLocation;
    private double distanceInKm; // Field for storing the distance
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double baseFare;
    private Double totalFare;
    private String status;

    // This constructor is needed to initialize the object when adding sample data
    public Ride(app_User rider, app_User driver, String pickupLocation, String dropoffLocation,
                double distanceInKm, LocalDateTime startTime, LocalDateTime endTime,
                Double baseFare, Double totalFare, String status) {
        this.rider = rider;
        this.driver = driver;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.distanceInKm = distanceInKm;
        this.startTime = startTime;
        this.endTime = endTime;
        this.baseFare = baseFare;
        this.totalFare = totalFare;
        this.status = status;
    }

    // Calculate total fare (if necessary) based on distance and base fare
    public void calculateTotalFare(double pricePerKm) {
        this.totalFare = this.baseFare + (this.distanceInKm * pricePerKm);
    }

    // If you want to dynamically calculate the distance, you can override this method as needed
    public Double getDistanceInKm() {
        return this.distanceInKm; // Return the actual distance in km
    }
}
