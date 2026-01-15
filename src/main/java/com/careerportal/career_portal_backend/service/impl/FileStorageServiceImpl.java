package com.careerportal.career_portal_backend.service.impl;

import com.careerportal.career_portal_backend.entity.DBFile;
import com.careerportal.career_portal_backend.repository.DBFileRepository;
import com.careerportal.career_portal_backend.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final DBFileRepository dbFileRepository;

    public FileStorageServiceImpl(DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());

            DBFile savedFile = dbFileRepository.save(dbFile);
            
            logger.info("File stored in database successfully with ID: {}", savedFile.getId());
            return savedFile.getId();
        } catch (IOException ex) {
            logger.error("Could not store file {}. Please try again!", fileName, ex);
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            if (fileId != null && dbFileRepository.existsById(fileId)) {
                dbFileRepository.deleteById(fileId);
                logger.info("File deleted successfully from database: {}", fileId);
            } else {
                 logger.warn("File not found or ID is null, could not delete: {}", fileId);
            }
        } catch (Exception ex) {
            logger.error("Could not delete file with ID: {}", fileId, ex);
        }
    }
}
