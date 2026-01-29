package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // ---------------------------------------------------
    // Create Patient
    // ---------------------------------------------------
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------
    // Get Patient Appointments (Authorized)
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {

        Map<String, Object> response = new HashMap<>();

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null || !patient.getId().equals(id)) {
            response.put("message", "Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<Appointment> appointments =
                appointmentRepository.findByPatientId(id);

        List<AppointmentDTO> appointmentDTOS = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", appointmentDTOS);
        response.put("count", appointmentDTOS.size());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Get Patient Appointments (Doctor View)
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointmentForDoctor(Long id) {
        Map<String, Object> response = new HashMap<>();

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);

        List<AppointmentDTO> appointmentDTOS = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", appointmentDTOS);
        response.put("count", appointmentDTOS.size());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Filter Appointments By Condition (Past / Future)
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(
            String condition, Long id) {

        Map<String, Object> response = new HashMap<>();

        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            response.put("message", "Invalid condition");
            return ResponseEntity.badRequest().body(response);
        }

        List<Appointment> appointments =
                appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
                        id, status
                );

        List<AppointmentDTO> dtos = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", dtos);
        response.put("count", dtos.size());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Filter Appointments By Doctor Name
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(
            String name, Long patientId) {

        Map<String, Object> response = new HashMap<>();

        List<Appointment> appointments =
                appointmentRepository.filterByDoctorNameAndPatientId(
                        name, patientId
                );

        List<AppointmentDTO> dtos = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", dtos);
        response.put("count", dtos.size());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Filter Appointments By Doctor + Condition
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(
            String condition, String name, long patientId) {

        Map<String, Object> response = new HashMap<>();

        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            response.put("message", "Invalid condition");
            return ResponseEntity.badRequest().body(response);
        }

        List<Appointment> appointments =
                appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(
                        name, patientId, status
                );

        List<AppointmentDTO> dtos = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", dtos);
        response.put("count", dtos.size());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Get Patient Details From Token
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {

        Map<String, Object> response = new HashMap<>();

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("patient", patient);
        return ResponseEntity.ok(response);
    }
}
