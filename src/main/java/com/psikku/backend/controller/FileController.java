package com.psikku.backend.controller;

import com.psikku.backend.service.filestorage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class FileController {


    private final FileStorageService storageService;
    private final Logger logger;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping("/{category}")
    public String uploadFile(@RequestPart("file") MultipartFile file,@PathVariable("category") @Nullable String category){
        storageService.storeFile(file,category);
        return "success";
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilPicture(MultipartFile file){
        storageService.storeUserPicture(file);
        Map<String,String> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        return ResponseEntity.ok(returnBody);
    }

    @GetMapping("/{category}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, @PathVariable @Nullable String category, HttpServletRequest request){
        Resource resource = storageService.loadFileAsResource(category,filename);
        String contentType = "";
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(IOException e){
            contentType = null;
            e.printStackTrace();
        }

        if(contentType == null){
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"");
        headers.add(HttpHeaders.CONTENT_TYPE,contentType);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestPart("category") String fileCategory, @RequestPart("filename") String fileName){
        storageService.deleteByFilePathAndFileName(fileCategory,fileName);
        return new ResponseEntity<>("File deleted Successfully",HttpStatus.OK);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
