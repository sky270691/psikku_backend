package com.psikku.backend.controller;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.service.testpackage.TestPackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class TestPackageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TestPackageService testPackageService;

    @Autowired
    public TestPackageController(TestPackageService testPackageService) {
        this.testPackageService = testPackageService;
    }

    @PostMapping
    public ResponseEntity<GeneratedPaymentDetailDto> generatePackage(@RequestBody TestPackageCreationDto testPackageCreationDto){
        logger.info("try to generate the package");
        GeneratedPaymentDetailDto generatedPaymentDetailDto = testPackageService.generatePackage(testPackageCreationDto);
        logger.info("package generate successfully");
        return new ResponseEntity<>(generatedPaymentDetailDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MinimalTestDto>> getTestDescriptionList(@PathVariable int id){
        return new ResponseEntity<>(testPackageService.getAllTestDescByPackageId(id),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TestPackageDto>> getAllPackage(){
        return new ResponseEntity<>(testPackageService.getAllPackage(),HttpStatus.OK);
    }


}
