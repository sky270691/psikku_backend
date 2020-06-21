package com.psikku.backend.controller;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.service.testpackage.TestPackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class TestPackageController {
    private final Logger logger;
    private final TestPackageService testPackageService;

    @Autowired
    public TestPackageController(TestPackageService testPackageService) {
        this.testPackageService = testPackageService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping
    public ResponseEntity<GeneratedPaymentDetailDto> generatePackage(@RequestBody TestPackageCreationDto testPackageCreationDto){
        logger.info("try to generate the package");
        GeneratedPaymentDetailDto generatedPaymentDetailDto = testPackageService.generatePackage(testPackageCreationDto);
        logger.info("package generate successfully");
        return new ResponseEntity<>(generatedPaymentDetailDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MinimalTestDto>> getPackageById(@PathVariable int id){
        logger.info("username:'"+getUsername()+"' getting test package by id");
        return new ResponseEntity<>(testPackageService.getAllTestDescByPackageId(id),HttpStatus.OK);
    }

    @GetMapping("/voucher-status/{id}")
    public ResponseEntity<Boolean> validateVoucherUsage(@PathVariable int id){
        boolean valid = testPackageService.validateVoucherPackage(id);
        return new ResponseEntity<>(valid,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TestPackageDto>> getAllPackage(){
        return new ResponseEntity<>(testPackageService.getAllPackage(),HttpStatus.OK);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
