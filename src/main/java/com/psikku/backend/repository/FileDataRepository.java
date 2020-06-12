package com.psikku.backend.repository;

import com.psikku.backend.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Integer> {

    Optional<FileData> findByFileName(String fileName);
    Optional<FileData> findByFilePathAndFileName(String path,String fileName);
    void deleteByFilePathAndFileName(String filePath, String fileName);
}
