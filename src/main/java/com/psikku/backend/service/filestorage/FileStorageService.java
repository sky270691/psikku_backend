package com.psikku.backend.service.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    boolean storeFile(MultipartFile file,String subFolder);
    boolean storeUserPicture(MultipartFile file);
    String storeUserAnswerPicture(MultipartFile pictFile);
    Resource loadFileAsResource(String category,String filename);
    void deleteByFilePathAndFileName(String filePath, String fileName);
    Resource downloadSingleFile(String filePath);

}
