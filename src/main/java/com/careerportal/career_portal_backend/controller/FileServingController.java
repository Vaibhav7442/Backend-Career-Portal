package com.careerportal.career_portal_backend.controller;

import com.careerportal.career_portal_backend.entity.DBFile;
import com.careerportal.career_portal_backend.service.FileStorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/uploads")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FileServingController {

    private static final Logger logger = LoggerFactory.getLogger(FileServingController.class);
    
    private final FileStorageService fileStorageService;

    public FileServingController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/resumes/{filename:.+}")
    public ResponseEntity<Resource> serveResumeFile(@PathVariable String filename) {
        // Filename here corresponds to the DBFile ID
        return serveFile(filename);
    }

    @GetMapping("/photos/{filename:.+}")
    public ResponseEntity<Resource> servePhotoFile(@PathVariable String filename) {
        // Filename here corresponds to the DBFile ID
        return serveFile(filename);
    }

    @GetMapping("/debug/files")
    public ResponseEntity<String> debugFiles() {
        return ResponseEntity.ok("File storage is now Database-backed. Filesystem debug no longer available.");
    }

    private ResponseEntity<Resource> serveFile(String fileId) {
        try {
            logger.info("Attempting to serve file with ID: {}", fileId);
            
            DBFile dbFile = fileStorageService.getFile(fileId);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + dbFile.getFileName() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(new ByteArrayResource(dbFile.getData()));

        } catch (Exception e) {
            logger.error("Error serving file with ID: {}", fileId, e);
            return ResponseEntity.notFound().build();
        }
    }
}