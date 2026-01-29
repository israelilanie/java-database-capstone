package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // ---------------------------------------------------
    // Validate Token
    // ---------------------------------------------------
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Unauthorized: Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response.put("message", "Token is valid");
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Validate Admin
    // ---------------------------------------------------
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

        if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(admin.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Filter Doctors
    // ---------------------------------------------------
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        String normalizedName = name != null && !name.isBlank() ? name : null;
        String normalizedSpecialty = specialty != null && !specialty.isBlank() ? specialty : null;
        String normalizedTime = time != null && !time.isBlank() ? time : null;

        if (normalizedName == null && normalizedSpecialty == null && normalizedTime == null) {
            var doctors = doctorService.getDoctors();
            return Map.of("doctors", doctors, "count", doctors.size());
        }

        if (normalizedName != null && normalizedSpecialty != null && normalizedTime != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(
                    normalizedName, normalizedSpecialty, normalizedTime
            );
        }

        if (normalizedName != null && normalizedSpecialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(normalizedName, normalizedSpecialty);
        }

        if (normalizedName != null && normalizedTime != null) {
            return doctorService.filterDoctorByNameAndTime(normalizedName, normalizedTime);
        }

        if (normalizedSpecialty != null && normalizedTime != null) {
            return doctorService.filterDoctorByTimeAndSpecility(normalizedSpecialty, normalizedTime);
        }

        if (normalizedName != null) {
            return doctorService.findDoctorByName(normalizedName);
        }

        if (normalizedSpecialty != null) {
            return doctorService.filterDoctorBySpecility(normalizedSpecialty);
        }

        return doctorService.filterDoctorsByTime(normalizedTime);
    }

    // ---------------------------------------------------
    // Validate Appointment
    // ---------------------------------------------------
    public int validateAppointment(Appointment appointment) {
        Optional<?> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
        if (doctorOpt.isEmpty()) return -1;

        var availableTimes = doctorService.getDoctorAvailability(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate()
        );

        if (availableTimes.contains(appointment.getAppointmentTimeOnly().toString())) return 1;
        return 0;
    }

    // ---------------------------------------------------
    // Validate Patient Existence
    // ---------------------------------------------------
    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    // ---------------------------------------------------
    // Validate Patient Login
    // ---------------------------------------------------
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getIdentifier());

        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(patient.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Filter Patient Appointments
    // ---------------------------------------------------
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (condition != null && name != null) {
            return patientService.filterByDoctorAndCondition(condition, name, patient.getId());
        } else if (condition != null) {
            return patientService.filterByCondition(condition, patient.getId());
        } else if (name != null) {
            return patientService.filterByDoctor(name, patient.getId());
        } else {
            return patientService.getPatientAppointment(patient.getId(), token);
        }
    }
}
