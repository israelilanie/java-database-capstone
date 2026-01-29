
package com.project.back_end.dto;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;

    // Computed fields
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    // Constructor
    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId, String patientName,
                          String patientEmail, String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Automatically compute these fields
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();

        Doctor doctor = appointment.getDoctor();
        if (doctor != null) {
            this.doctorId = doctor.getId();
            this.doctorName = doctor.getName();
        }

        Patient patient = appointment.getPatient();
        if (patient != null) {
            this.patientId = patient.getId();
            this.patientName = patient.getName();
            this.patientEmail = patient.getEmail();
            this.patientPhone = patient.getPhone();
            this.patientAddress = patient.getAddress();
        }

        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();

        if (this.appointmentTime != null) {
            this.appointmentDate = appointmentTime.toLocalDate();
            this.appointmentTimeOnly = appointmentTime.toLocalTime();
            this.endTime = appointmentTime.plusHours(1);
        }
    }

    // Getters for each field
    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
