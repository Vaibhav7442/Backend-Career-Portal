package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.entity.JobSeekerProfile;
import java.util.List;

public interface JobSeekerService {
    List<JobSeekerProfile> getAllJobSeekers();
}