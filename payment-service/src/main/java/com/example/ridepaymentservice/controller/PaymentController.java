package com.example.ridepaymentservice.controller;

import com.example.ridepaymentservice.dto.PaymentRequest;
import com.example.ridepaymentservice.dto.PaymentResponse;
import com.example.ridepaymentservice.model.Ride;
import com.example.ridepaymentservice.repository.RideRepository;
import com.example.ridepaymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RideRepository rideRepository;

    @Autowired
    public PaymentController(PaymentService paymentService,RideRepository rideRepository) {
        this.paymentService = paymentService;
        this.rideRepository = rideRepository;
    }


    @PostMapping("/CreateRide")
    public ResponseEntity<Ride> createRide(@RequestBody Ride ride) {
        Ride savedRide = rideRepository.save(ride);
        return new ResponseEntity<>(savedRide, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    // Get ride by id
    @GetMapping("ride/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if (ride.isPresent()) {
            return new ResponseEntity<>(ride.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ride>> getRidesByStatus(@PathVariable String status) {
        List<Ride> rides = rideRepository.findByStatus(status);
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long id) {
        // Find the ride by its ID
        Ride ride = rideRepository.findById(id).orElse(null);

        if (ride == null) {
            // Ride not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the status to "COMPLETED"
        ride.setStatus("COMPLETED");

        // Save the updated ride
        Ride updatedRide = rideRepository.save(ride);

        return new ResponseEntity<>(updatedRide, HttpStatus.OK);
    }
}
