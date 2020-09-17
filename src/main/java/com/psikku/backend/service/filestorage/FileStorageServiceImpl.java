package com.psikku.backend.service.filestorage;

import com.psikku.backend.config.properties.StorageProperties;
import com.psikku.backend.entity.FileData;
import com.psikku.backend.entity.User;
import com.psikku.backend.exception.DataAlreadyExistException;
import com.psikku.backend.exception.FileStorageException;
import com.psikku.backend.repository.FileDataRepository;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional
public class FileStorageServiceImpl implements FileStorageService {


    private Path fileStorageLocation;
    private final StorageProperties storageProperties;
    private final FileDataRepository fileDataRepository;
    private final UserService userService;

    @Autowired
    public FileStorageServiceImpl(StorageProperties storageProperties, FileDataRepository fileDataRepository,
                                  @Lazy UserService userService) {
        this.storageProperties = storageProperties;
        this.fileStorageLocation = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileDataRepository = fileDataRepository;
        this.userService = userService;
    }


    // method to reset the fileStorageLocation pointer to default file allocation directory
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
    public boolean storeUserPicture(MultipartFile file) {
        String credential = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(credential);
        FileData fileData = new FileData();
        fileData.setType("picture");
        String[] fileNameArray = file.getOriginalFilename().split("\\.");
        fileData.setFileName(UUID.randomUUID().toString()+"."+fileNameArray[fileNameArray.length-1]);
        setFileStorageLocation(Paths.get(storageProperties.getUploadDir()+"/picture").toAbsolutePath());
        fileData.setFilePath(getFileStorageLocation().toString());
        Path targetLocation = fileStorageLocation.resolve(fileData.getFileName());
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileData fileData1 = fileDataRepository.save(fileData);
        user.setProfilPicture(fileData1);
        userService.saveOrUpdateUserEntity(user);
        return true;
    }


    @Override
    public Resource loadFileAsResource(String category, String filename) {
        FileData fileData = fileDataRepository.findByFileNameAndType(filename,category).orElseThrow(()->new FileStorageException("File Not Found"));
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
                reset();
                return resource;
            }else{
                reset();
                throw new FileStorageException("file not found");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            reset();
            return null;
        }
    }

    @Override
    public void deleteByFilePathAndFileName(String filePath, String fileName) {
        Path fileToRemove = this.fileStorageLocation.resolve(filePath).resolve(fileName);
        try {
            Files.deleteIfExists(fileToRemove);
            fileDataRepository.findByFilePathAndFileName(filePath,fileName).ifPresent(fileDataRepository::delete);
            reset();
        } catch (IOException e) {
            throw new FileStorageException("Error deleting file");
        }
    }

    @Override
    public Resource downloadSingleFile(String filePath) {
        try {
//            Paths.get(filePath).toAbsolutePath();
            Resource resource = new FileUrlResource(filePath);
            return resource;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new FileStorageException("File Not Found");
        }
    }


}
