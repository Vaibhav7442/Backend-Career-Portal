package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.payload.JobPostingDto;
import java.util.List;

public interface JobPostingService {
    JobPostingDto createJobPosting(String username, JobPostingDto jobPostingDto);
    List<JobPostingDto> getAllJobPostings();
    List<JobPostingDto> searchJobPostings(String keyword, String location, String experienceLevel);
    List<JobPostingDto> getJobPostingsByEmployer(String username);
    JobPostingDto getJobById(Long jobId);
    JobPostingDto updateJobPosting(Long jobId, String username, JobPostingDto jobPostingDto);
    void deleteJobPosting(Long jobId, String username);
}