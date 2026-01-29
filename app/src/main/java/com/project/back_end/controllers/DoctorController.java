package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor") // Base path from config + doctor
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // ---------------------------------------------------
    // Get Doctor Availability
    // ---------------------------------------------------
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        // Validate token
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        LocalDate appointmentDate = LocalDate.parse(date);
        List<String> availability = doctorService.getDoctorAvailability(doctorId, appointmentDate);

        return ResponseEntity.ok(Map.of("availability", availability));
    }

    // ---------------------------------------------------
    // Get All Doctors
    // ---------------------------------------------------
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        List<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok(Map.of("doctors", doctors));
    }

    // ---------------------------------------------------
    // Add New Doctor
    // ---------------------------------------------------
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        // Only admin can add doctors
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        int result = doctorService.saveDoctor(doctor);
        switch (result) {
            case 1:
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Doctor added to db"));
            case -1:
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Doctor already exists"));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Some internal error occurred"));
        }
    }

    // ---------------------------------------------------
    // Doctor Login
    // ---------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // ---------------------------------------------------
    // Update Doctor
    // ---------------------------------------------------
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        // Only admin can update doctor
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        int result = doctorService.updateDoctor(doctor);
        switch (result) {
            case 1:
                return ResponseEntity.ok(Map.of("message", "Doctor updated"));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Doctor not found"));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Some internal error occurred"));
        }
    }

    // ---------------------------------------------------
    // Delete Doctor
    // ---------------------------------------------------
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {

        // Only admin can delete doctor
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validation.getBody(), validation.getStatusCode());
        }

        int result = doctorService.deleteDoctor(id);
        switch (result) {
            case 1:
                return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Doctor not found with id"));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Some internal error occurred"));
        }
    }

    // ---------------------------------------------------
    // Filter Doctors
    // ---------------------------------------------------
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String specialty) {

        Map<String, Object> filteredDoctors = service.filterDoctor(name, specialty, time);
        return ResponseEntity.ok(filteredDoctors);
    }
}
