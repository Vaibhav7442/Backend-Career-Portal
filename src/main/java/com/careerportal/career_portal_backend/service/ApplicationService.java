package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.payload.ApplicationResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    ApplicationResponseDto applyForJob(String username, Long jobId, MultipartFile resumeFile);
    ApplicationResponseDto applyForJob(String username, Long jobId);
    List<ApplicationResponseDto> getMyApplications(String username);
    List<ApplicationResponseDto> getApplicationsForJob(Long jobId, String employerUsername);
    ApplicationResponseDto updateApplicationStatus(Long applicationId, String newStatus, String recruiterNotes, String employerUsername);
    Long getApplicationCountForJob(Long jobId);
    Map<Long, Long> getApplicationCountsForJobs(List<Long> jobIds);
    Resource getApplicationResume(Long applicationId, String employerUsername);
    String getApplicationResumeFileName(Long applicationId, String employerUsername);
}
