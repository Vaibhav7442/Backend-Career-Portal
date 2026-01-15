package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.entity.JobSeekerProfile;
import com.careerportal.career_portal_backend.entity.User;
import com.careerportal.career_portal_backend.payload.JobSeekerProfileDto;
import com.careerportal.career_portal_backend.payload.JobSeekerRegisterDto;

public interface JobSeekerProfileService {
    JobSeekerProfile createJobSeekerProfile(User user, JobSeekerRegisterDto registerDto);
    JobSeekerProfile getOrCreateJobSeekerProfile(User user);
    JobSeekerProfileDto getProfileByUsername(String username);
    JobSeekerProfileDto updateProfile(String username, JobSeekerProfileDto profileDto);
}
