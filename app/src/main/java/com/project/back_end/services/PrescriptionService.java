package com.project.back_end.service;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    // ---------------------------------------------------
    // Save Prescription
    // ---------------------------------------------------
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {

        Map<String, String> response = new HashMap<>();

        try {
            // Check if a prescription already exists for this appointment
            List<Prescription> existing =
                    prescriptionRepository.findByAppointmentId(
                            prescription.getAppointmentId()
                    );

            if (!existing.isEmpty()) {
                response.put("message", "Prescription already exists for this appointment");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            prescriptionRepository.save(prescription);

            response.put("message", "Prescription saved");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception e) {
            response.put("message", "Failed to save prescription");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    // ---------------------------------------------------
    // Get Prescription By Appointment ID
    // ---------------------------------------------------
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                response.put("message", "No prescription found for this appointment");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(response);
            }

            // Usually only one prescription per appointment
            response.put("prescription", prescriptions.get(0));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error retrieving prescription");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
