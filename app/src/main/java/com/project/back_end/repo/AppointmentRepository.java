package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        WHERE d.id = ?1
        AND a.appointmentTime BETWEEN ?2 AND ?3
    """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        LEFT JOIN FETCH a.patient p
        WHERE d.id = ?1
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', ?2, '%'))
        AND a.appointmentTime BETWEEN ?3 AND ?4
    """)
    List<Appointment> findByDoctorIdAndPatientNameAndDateRange(
            Long doctorId,
            String patientName,
            LocalDateTime start,
            LocalDateTime end
    );

    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
            Long patientId,
            int status
    );

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN a.doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', ?1, '%'))
        AND a.patient.id = ?2
    """)
    List<Appointment> filterByDoctorNameAndPatientId(
            String doctorName,
            Long patientId
    );

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN a.doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', ?1, '%'))
        AND a.patient.id = ?2
        AND a.status = ?3
    """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            String doctorName,
            Long patientId,
            int status
    );
}
