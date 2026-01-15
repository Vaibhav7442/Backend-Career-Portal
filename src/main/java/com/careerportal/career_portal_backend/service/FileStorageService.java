package com.careerportal.career_portal_backend.service;

import com.careerportal.career_portal_backend.entity.DBFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    DBFile getFile(String fileId);

    void deleteFile(String fileId);
}