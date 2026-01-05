package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByEmail(String email);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Doctor> findByNameLike(String name);

    @Query("""
        SELECT d FROM Doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', ?1, '%'))
        AND LOWER(d.speciality) = LOWER(?2)
    """)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
            String name,
            String speciality
    );

    List<Doctor> findBySpecialityIgnoreCase(String speciality);
}
