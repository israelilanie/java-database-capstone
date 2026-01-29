package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    // ---------------------------------------------------
    // Book Appointment
    // ---------------------------------------------------
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------
    // Update Appointment
    // ---------------------------------------------------
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existingOpt =
                appointmentRepository.findById(appointment.getId());

        if (existingOpt.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Basic validation example (can be expanded later)
        if (!doctorRepository.existsById(appointment.getDoctor().getId())) {
            response.put("message", "Invalid doctor ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Cancel Appointment
    // ---------------------------------------------------
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Appointment appointment = appointmentOpt.get();

        Long patientIdFromToken = tokenService.extractPatientId(token);
        if (!appointment.getPatient().getId().equals(patientIdFromToken)) {
            response.put("message", "You are not allowed to cancel this appointment");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        appointmentRepository.delete(appointment);
        response.put("message", "Appointment cancelled successfully");
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // Get Appointments (Doctor + Date + Optional Patient Name)
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public Map<String, Object> getAppointment(String pname,
                                              LocalDate date,
                                              String token) {

        Map<String, Object> response = new HashMap<>();

        Long doctorId = tokenService.extractDoctorId(token);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments;

        if (pname != null && !pname.isBlank()) {
            appointments =
                    appointmentRepository.findByDoctorIdAndPatientNameAndDateRange(
                            doctorId, pname, start, end
                    );
        } else {
            appointments =
                    appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                            doctorId, start, end
                    );
        }

        response.put("appointments", appointments);
        response.put("count", appointments.size());
        return response;
    }

    // ---------------------------------------------------
    // Change Appointment Status
    // ---------------------------------------------------
    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(long appointmentId, int status) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointmentOpt =
                appointmentRepository.findById(appointmentId);

        if (appointmentOpt.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Appointment appointment = appointmentOpt.get();
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        response.put("message", "Appointment status updated");
        return ResponseEntity.ok(response);
    }
}
