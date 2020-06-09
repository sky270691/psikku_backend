package com.psikku.backend.controller;

import com.psikku.backend.service.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/content")
public class FileController {

    @Autowired
    FileStorageService storageService;

    @PostMapping("/{category}")
    public String uploadFile(@RequestPart("file") MultipartFile file,@PathVariable("category") @Nullable String category){
        storageService.storeFile(file,category);
        return "success";
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

}
