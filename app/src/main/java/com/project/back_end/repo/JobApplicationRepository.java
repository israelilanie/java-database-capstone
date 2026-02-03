package com.project.back_end.repo;

import com.project.back_end.models.ApplicationStatus;
import com.project.back_end.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatusOrderByAppliedDateDesc(ApplicationStatus status);
}
