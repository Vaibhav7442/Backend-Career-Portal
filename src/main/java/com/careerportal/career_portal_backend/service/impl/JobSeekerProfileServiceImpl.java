package com.careerportal.career_portal_backend.service.impl;

import com.careerportal.career_portal_backend.entity.EducationDetail;
import com.careerportal.career_portal_backend.entity.JobSeekerProfile;
import com.careerportal.career_portal_backend.entity.User;
import com.careerportal.career_portal_backend.execption.ResourceNotFoundException;
import com.careerportal.career_portal_backend.payload.EducationDetailDto;
import com.careerportal.career_portal_backend.payload.JobSeekerProfileDto;
import com.careerportal.career_portal_backend.payload.JobSeekerRegisterDto;
import com.careerportal.career_portal_backend.repository.EducationDetailRepository;
import com.careerportal.career_portal_backend.repository.JobSeekerProfileRepository;
import com.careerportal.career_portal_backend.repository.UserRepository;
import com.careerportal.career_portal_backend.service.FileStorageService;
import com.careerportal.career_portal_backend.service.JobSeekerProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobSeekerProfileServiceImpl implements JobSeekerProfileService {

    private static final Logger logger = LoggerFactory.getLogger(JobSeekerProfileServiceImpl.class);
    private final JobSeekerProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final EducationDetailRepository educationDetailRepository;
    private final FileStorageService fileStorageService;

    public JobSeekerProfileServiceImpl(JobSeekerProfileRepository profileRepository,
                                   UserRepository userRepository,
                                   EducationDetailRepository educationDetailRepository,
                                   FileStorageService fileStorageService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.educationDetailRepository = educationDetailRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Creates a job seeker profile from registration data
     */
    @Override
    public JobSeekerProfile createJobSeekerProfile(User user, JobSeekerRegisterDto registerDto) {
        logger.info("Creating job seeker profile for user: {}", user.getUsername());
        
        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setUser(user);
        profile.setName(registerDto.getName());
        profile.setEmail(registerDto.getEmail());
        profile.setMobile(registerDto.getMobile());
        profile.setStatus(JobSeekerProfile.ExperienceStatus.valueOf(registerDto.getStatus()));
        profile.setGender(JobSeekerProfile.Gender.valueOf(registerDto.getGender()));
        profile.setDob(registerDto.getDob());
        profile.setEducation(registerDto.getEducation());
        profile.setWorkExperience(registerDto.getWorkExperience());
        profile.setSkills(registerDto.getSkills());
        profile.setResumeFilePath(registerDto.getResumeFilePath());
        profile.setPhotoFilePath(registerDto.getPhotoFilePath());
        
        JobSeekerProfile savedProfile = profileRepository.save(profile);
        logger.info("Created job seeker profile with ID: {} for user: {}", savedProfile.getId(), user.getUsername());
        
        return savedProfile;
    }

    /**
     * Gets or creates a job seeker profile for a user
     */
    @Override
    public JobSeekerProfile getOrCreateJobSeekerProfile(User user) {
        return profileRepository.findByUser_Id(user.getId())
                .orElseGet(() -> createDefaultJobSeekerProfile(user));
    }

    /**
     * Creates a default job seeker profile for existing users
     */
    private JobSeekerProfile createDefaultJobSeekerProfile(User user) {
        logger.info("Creating default job seeker profile for user: {}", user.getUsername());
        
        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setUser(user);
        profile.setName("Please Update Name");
        profile.setEmail(user.getEmail() != null ? user.getEmail() : "Please Update Email");
        profile.setMobile("Please Update Mobile");
        profile.setStatus(JobSeekerProfile.ExperienceStatus.FRESHER);
        profile.setGender(JobSeekerProfile.Gender.OTHER);
        profile.setDob(LocalDate.of(1990, 1, 1)); // Default DOB
        profile.setEducation("Please Update Education");
        profile.setWorkExperience("Please Update Work Experience");
        profile.setSkills("Please Update Skills");
        
        return profileRepository.save(profile);
    }

    // --- Mapper Methods (Updated for new structure) ---
    private JobSeekerProfileDto mapToDTO(JobSeekerProfile profile) {
        JobSeekerProfileDto dto = new JobSeekerProfileDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setEmail(profile.getEmail());
        dto.setMobile(profile.getMobile());
        dto.setStatus(profile.getStatus() != null ? profile.getStatus().toString() : null);
        dto.setGender(profile.getGender() != null ? profile.getGender().toString() : null);
        dto.setDob(profile.getDob());
        dto.setEducation(profile.getEducation());
        dto.setWorkExperience(profile.getWorkExperience());
        dto.setSkills(profile.getSkills());
        dto.setResumeFilePath(profile.getResumeFilePath());
        dto.setPhotoFilePath(profile.getPhotoFilePath());

        // Map Education Details if they exist
        if (profile.getEducationDetails() != null) {
            List<EducationDetailDto> educationDtos = profile.getEducationDetails().stream()
                    .map(this::mapEducationToDto)
                    .collect(Collectors.toList());
            dto.setEducationDetails(educationDtos);
        }

        return dto;
    }

    private EducationDetailDto mapEducationToDto(EducationDetail education) {
        EducationDetailDto dto = new EducationDetailDto();
        dto.setId(education.getId());
        dto.setQualification(education.getQualification());
        dto.setSpecialization(education.getSpecialization());
        dto.setYearOfPassing(education.getYearOfPassing());
        return dto;
    }

    // --- Core Business Logic Methods ---

    /** Fetches the logged-in user's profile */
    @Override
    public JobSeekerProfileDto getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        JobSeekerProfile profile = profileRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job Seeker Profile", "User ID", user.getId().toString()));

        return mapToDTO(profile);
    }

    /** Creates or updates the logged-in user's profile */
    @Override
    @Transactional
    public JobSeekerProfileDto updateProfile(String username, JobSeekerProfileDto profileDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Find existing profile or create a new one if it doesn't exist yet
        JobSeekerProfile profile = profileRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    JobSeekerProfile newProfile = new JobSeekerProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        // 1. Update Profile fields
        profile.setName(profileDto.getName());
        profile.setEmail(profileDto.getEmail());
        profile.setMobile(profileDto.getMobile());
        if (profileDto.getStatus() != null) {
            profile.setStatus(JobSeekerProfile.ExperienceStatus.valueOf(profileDto.getStatus()));
        }
        if (profileDto.getGender() != null) {
            profile.setGender(JobSeekerProfile.Gender.valueOf(profileDto.getGender()));
        }
        profile.setDob(profileDto.getDob());
        profile.setEducation(profileDto.getEducation());
        profile.setWorkExperience(profileDto.getWorkExperience());
        profile.setSkills(profileDto.getSkills());

        // Handle Resume Update - Delete old one if changed
        if (profileDto.getResumeFilePath() != null && !profileDto.getResumeFilePath().equals(profile.getResumeFilePath())) {
            // Delete old file if it exists
            if (profile.getResumeFilePath() != null && !profile.getResumeFilePath().isEmpty()) {
                String oldPath = profile.getResumeFilePath();
                String fileId = oldPath.contains("/") ? oldPath.substring(oldPath.lastIndexOf("/") + 1) : oldPath;
                fileStorageService.deleteFile(fileId);
            }
            profile.setResumeFilePath(profileDto.getResumeFilePath());
        }

        // Handle Photo Update - Delete old one if changed
        if (profileDto.getPhotoFilePath() != null && !profileDto.getPhotoFilePath().equals(profile.getPhotoFilePath())) {
             // Delete old file if it exists
            if (profile.getPhotoFilePath() != null && !profile.getPhotoFilePath().isEmpty()) {
                String oldPath = profile.getPhotoFilePath();
                String fileId = oldPath.contains("/") ? oldPath.substring(oldPath.lastIndexOf("/") + 1) : oldPath;
                fileStorageService.deleteFile(fileId);
            }
            profile.setPhotoFilePath(profileDto.getPhotoFilePath());
        }

        // 2. Update Education Details (Complex logic - simple update/add here)
        // Clear old education details to avoid orphans and replace with new ones
        if (profile.getEducationDetails() != null) {
            profile.getEducationDetails().clear();
        }

        if (profileDto.getEducationDetails() != null) {
            List<EducationDetail> newEducationList = profileDto.getEducationDetails().stream()
                    .map(dto -> {
                        EducationDetail education = new EducationDetail();
                        education.setQualification(dto.getQualification());
                        education.setSpecialization(dto.getSpecialization());
                        education.setYearOfPassing(dto.getYearOfPassing());
                        education.setJobSeekerProfile(profile); // Set the parent relationship
                        return education;
                    }).collect(Collectors.toList());

            profile.setEducationDetails(newEducationList);
        }

        JobSeekerProfile updatedProfile = profileRepository.save(profile);
        return mapToDTO(updatedProfile);
    }
}
