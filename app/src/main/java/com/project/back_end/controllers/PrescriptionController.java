package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription") // Base path for prescription operations
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public PrescriptionController(PrescriptionService prescriptionService, Service service) {
        this.prescriptionService = prescriptionService;
        this.service = service;
    }

    // ---------------------------------------------------
    // Save Prescription
    // ---------------------------------------------------
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription) {

        // Validate token for doctor role
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        // Save prescription using service
        return prescriptionService.savePrescription(prescription);
    }

    // ---------------------------------------------------
    // Get Prescription by Appointment ID
    // ---------------------------------------------------
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        // Validate token for doctor role
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        // Fetch prescription using service
        return prescriptionService.getPrescription(appointmentId);
    }
}
