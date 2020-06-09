package com.psikku.backend.service.filestorage;

import com.psikku.backend.config.properties.StorageProperties;
import com.psikku.backend.entity.FileData;
import com.psikku.backend.exception.DataAlreadyExistException;
import com.psikku.backend.exception.FileStorageException;
import com.psikku.backend.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NonUniqueResultException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    private Path fileStorageLocation;
    private final StorageProperties storageProperties;
    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileStorageServiceImpl(StorageProperties storageProperties,FileDataRepository fileDataRepository) {
        this.storageProperties = storageProperties;
        this.fileStorageLocation = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileDataRepository = fileDataRepository;
    }

    public void reset(){
        this.fileStorageLocation = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public void setFileStorageLocation(Path fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    @Override
    public boolean storeFile(MultipartFile file, String subFolder) {
        String fileName = file.getOriginalFilename();
        Path targetLocation;
        FileData fileData = new FileData();
        try{
            if(subFolder==null){
                setFileStorageLocation(Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize());
            }
            else{
                if(subFolder.equalsIgnoreCase("picture")||
                subFolder.equalsIgnoreCase("general")||
                subFolder.equalsIgnoreCase("data")){
                    setFileStorageLocation(Paths.get(storageProperties.getUploadDir(),"/"+subFolder).toAbsolutePath().normalize());
                }else{
                    throw new FileStorageException("category not valid");
                }
            }
            targetLocation = fileStorageLocation.resolve(fileName);
            Files.createDirectories(this.fileStorageLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileData.setFilePath(subFolder);
            fileData.setFileName(fileName);
            if(fileDataRepository.findByFilePathAndFileName(subFolder,fileName).isPresent()){
                throw new DataAlreadyExistException("File with the current name and category already exist");
            }
            fileDataRepository.save(fileData);

            //return the fileStorageLocation into starting state
            reset();

            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Resource loadFileAsResource(String category, String filename) {
        FileData fileData = fileDataRepository.findByFilePathAndFileName(category,filename).orElseThrow(()->new FileStorageException("File Not Found"));
        System.out.println(fileData.getFileName());
        try {
            Path filePath;
            if(category==null) {
                filePath = this.fileStorageLocation.resolve(filename).normalize();
            }else{
                filePath = this.fileStorageLocation.resolve(fileData.getFilePath()+"/").resolve(fileData.getFileName()).normalize();
            }
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileStorageException("file not found");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Todo
    // implement the deleting uploaded file method


}
