package com.careerportal.career_portal_backend.repository;

import com.careerportal.career_portal_backend.entity.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {
    Optional<DBFile> findByFileName(String fileName);
}
