package com.project.back_end.services;

import com.project.back_end.models.ApplicationStatus;
import com.project.back_end.models.JobApplication;
import com.project.back_end.repo.JobApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplications(ApplicationStatus status) {
        if (status == null) {
            return jobApplicationRepository.findAll();
        }
        return jobApplicationRepository.findByStatusOrderByAppliedDateDesc(status);
    }

    @Transactional(readOnly = true)
    public Optional<JobApplication> getApplication(Long id) {
        return jobApplicationRepository.findById(id);
    }

    @Transactional
    public JobApplication createApplication(JobApplication application) {
        if (application.getStatus() == null) {
            application.setStatus(ApplicationStatus.DRAFT);
        }
        return jobApplicationRepository.save(application);
    }

    @Transactional
    public Optional<JobApplication> updateApplication(Long id, JobApplication application) {
        return jobApplicationRepository.findById(id).map(existing -> {
            existing.setCompanyName(application.getCompanyName());
            existing.setRoleTitle(application.getRoleTitle());
            existing.setLocation(application.getLocation());
            existing.setStatus(application.getStatus());
            existing.setAppliedDate(application.getAppliedDate());
            existing.setNextStepDate(application.getNextStepDate());
            existing.setSource(application.getSource());
            existing.setSalaryRange(application.getSalaryRange());
            existing.setContactName(application.getContactName());
            existing.setContactEmail(application.getContactEmail());
            existing.setNotes(application.getNotes());
            return jobApplicationRepository.save(existing);
        });
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateStatus(Long id, ApplicationStatus status) {
        Map<String, String> response = new HashMap<>();
        Optional<JobApplication> applicationOpt = jobApplicationRepository.findById(id);
        if (applicationOpt.isEmpty()) {
            response.put("message", "Job application not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        JobApplication application = applicationOpt.get();
        application.setStatus(status);
        jobApplicationRepository.save(application);
        response.put("message", "Job application status updated");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteApplication(Long id) {
        Map<String, String> response = new HashMap<>();
        if (!jobApplicationRepository.existsById(id)) {
            response.put("message", "Job application not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        jobApplicationRepository.deleteById(id);
        response.put("message", "Job application deleted");
        return ResponseEntity.ok(response);
    }
}
