package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.entity.Employer;
import com.careerportal.career_portal_backend.entity.User;
import com.careerportal.career_portal_backend.payload.EmployerRegisterDto;
import java.util.List;

public interface EmployerService {
    Employer createEmployerProfile(User user, EmployerRegisterDto registerDto);
    Employer createDefaultEmployerProfile(User user);
    Employer getOrCreateEmployerProfile(User user);
    Employer updateEmployerProfile(User user, com.careerportal.career_portal_backend.payload.EmployerDto employerDto);
    List<Employer> getAllEmployers(String companyName, String industry, Integer foundedAfter);
}