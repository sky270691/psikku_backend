package com.psikku.backend.controller;

import com.psikku.backend.dto.testpackage.CreatePackageResponseDto;
import com.psikku.backend.dto.testpackage.PackageDto;
import com.psikku.backend.entity.Package;
import com.psikku.backend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class TestPackageController {

//    public PackageDto getPackage()
    @Autowired
    PackageService packageService;

    @PostMapping
    public ResponseEntity<CreatePackageResponseDto> createNewPackage(@RequestBody PackageDto packageDto){
        System.out.println("test  1 2 3");
        Package newPackage = packageService.convertToPackageEntity(packageDto);
        return new ResponseEntity<>(packageService.createPackage(newPackage), HttpStatus.OK);
    }

}
