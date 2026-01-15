package com.careerportal.career_portal_backend.controller;

import com.careerportal.career_portal_backend.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload/resume")
    public ResponseEntity<Map<String, String>> uploadResume(@RequestParam("file") MultipartFile file) {
        logger.info("Received resume upload request for file: {}", file.getOriginalFilename());
        return uploadFile(file, "resumes", new String[]{"pdf", "doc", "docx"});
    }

    @PostMapping("/upload/photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        logger.info("Received photo upload request for file: {}", file.getOriginalFilename());
        return uploadFile(file, "photos", new String[]{"jpg", "jpeg", "png", "gif"});
    }

    private ResponseEntity<Map<String, String>> uploadFile(MultipartFile file, String subDir, String[] allowedExtensions) {
        Map<String, String> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("error", "Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file extension
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                response.put("error", "Invalid file name");
                return ResponseEntity.badRequest().body(response);
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            boolean validExtension = false;
            for (String ext : allowedExtensions) {
                if (ext.equals(fileExtension)) {
                    validExtension = true;
                    break;
                }
            }

            if (!validExtension) {
                response.put("error", "Invalid file type. Allowed types: " + String.join(", ", allowedExtensions));
                return ResponseEntity.badRequest().body(response);
            }

            // Store file in DB using service
            String fileId = fileStorageService.storeFile(file);
            
            // Construct relative path for frontend compatibility
            // Frontend expects "resumes/filename" or similar
            String relativePath = subDir + "/" + fileId;
            
            response.put("filePath", relativePath);
            response.put("originalName", originalFilename);
            response.put("message", "File uploaded successfully");

            logger.info("File uploaded successfully with ID: {}", fileId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error uploading file", e);
            response.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}