package com.careerportal.career_portal_backend.service.impl;

import com.careerportal.career_portal_backend.entity.JobSeekerProfile;
import com.careerportal.career_portal_backend.repository.JobSeekerProfileRepository;
import com.careerportal.career_portal_backend.service.JobSeekerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    private static final Logger logger = LoggerFactory.getLogger(JobSeekerServiceImpl.class);
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    public JobSeekerServiceImpl(JobSeekerProfileRepository jobSeekerProfileRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
    }

    /**
     * Gets all job seeker profiles
     */
    @Override
    public List<JobSeekerProfile> getAllJobSeekers() {
        logger.info("Fetching all job seekers");
        
        List<JobSeekerProfile> jobSeekers = jobSeekerProfileRepository.findAll();
        
        logger.info("Returning {} job seekers", jobSeekers.size());
        return jobSeekers;
    }
}
