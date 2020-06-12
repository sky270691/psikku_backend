package com.psikku.backend.service.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    boolean storeFile(MultipartFile file,String subFolder);
    Resource loadFileAsResource(String filename,String category);
    void deleteByFilePathAndFileName(String filePath, String fileName);

}
