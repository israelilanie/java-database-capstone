package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient") // Base path for patient operations
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    // ---------------------------------------------------
    // Get Patient Details
    // ---------------------------------------------------
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        return patientService.getPatientDetails(token);
    }

    // ---------------------------------------------------
    // Create a New Patient
    // ---------------------------------------------------
    @PostMapping()
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        boolean isValid = service.validatePatient(patient);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Patient with email id or phone no already exist"));
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Signup successful"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
        }
    }

    // ---------------------------------------------------
    // Patient Login
    // ---------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // ---------------------------------------------------
    // Get Patient Appointments
    // ---------------------------------------------------
    @GetMapping("/{id}/{user}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(
            @PathVariable Long id,
            @PathVariable String user,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        if ("doctor".equalsIgnoreCase(user)) {
            return patientService.getPatientAppointmentForDoctor(id);
        }

        return patientService.getPatientAppointment(id, token);
    }

    // ---------------------------------------------------
    // Filter Patient Appointments
    // ---------------------------------------------------
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        return service.filterPatient(condition, name, token);
    }
}
