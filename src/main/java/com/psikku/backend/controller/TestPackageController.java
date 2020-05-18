package com.psikku.backend.controller;

import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class TestPackageController {

    // Todo impl
    @PostMapping
    public ResponseEntity addPackage(TestPackageCreationDto testPackageCreationDto){

        return null;
    }


}
