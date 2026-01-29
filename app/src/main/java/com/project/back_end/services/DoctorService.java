package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // ---------------------------------------------------
    // Get Doctor Availability
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<String> availableSlots = new ArrayList<>(doctor.getAvailableTimes());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end
                );

        Set<String> bookedSlots = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toSet());

        availableSlots.removeIf(bookedSlots::contains);
        return availableSlots;
    }

    // ---------------------------------------------------
    // Save Doctor
    // ---------------------------------------------------
    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // already exists
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------
    // Update Doctor
    // ---------------------------------------------------
    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------
    // Get All Doctors
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // ---------------------------------------------------
    // Delete Doctor
    // ---------------------------------------------------
    @Transactional
    public int deleteDoctor(long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                return -1;
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------
    // Validate Doctor Login
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Map<String, String> response = new HashMap<>();

        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateDoctorToken(doctor.getId());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Find Doctor By Name
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {

        List<Doctor> doctors = doctorRepository.findByNameLike(name);

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        response.put("count", doctors.size());

        return response;
    }

    // ---------------------------------------------------
    // Filter: Name + Specialty + Time
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(
            String name, String speciality, String amOrPm) {

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
                        name, speciality
                );

        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // Filter: Name + Time
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(
            String name, String amOrPm) {

        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        doctors = filterDoctorByTime(doctors, amOrPm);

        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // Filter: Name + Specialty
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecility(
            String name, String speciality) {

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
                        name, speciality
                );

        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // Filter: Specialty + Time
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecility(
            String speciality, String amOrPm) {

        List<Doctor> doctors =
                doctorRepository.findBySpecialityIgnoreCase(speciality);

        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // Filter: Specialty Only
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecility(String speciality) {

        List<Doctor> doctors =
                doctorRepository.findBySpecialityIgnoreCase(speciality);

        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // Filter: Time Only
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {

        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);

        return Map.of("doctors", doctors, "count", doctors.size());
    }

    // ---------------------------------------------------
    // PRIVATE: Filter Doctor By AM / PM
    // ---------------------------------------------------
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {

        return doctors.stream()
                .filter(d -> d.getAvailableTimes() != null &&
                        d.getAvailableTimes().stream().anyMatch(time -> {
                            int hour = Integer.parseInt(time.split(":")[0]);
                            return amOrPm.equalsIgnoreCase("AM")
                                    ? hour < 12
                                    : hour >= 12;
                        }))
                .collect(Collectors.toList());
    }
}
