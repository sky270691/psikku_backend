package com.psikku.backend.controller;

import com.psikku.backend.dto.company.CompanyDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<String> addNewCompany(@RequestBody CompanyDto companyDto){
        Company company = companyService.saveCompany(companyDto);
        return new ResponseEntity<>(company.getName()+" saved successfully", HttpStatus.CREATED);
    }

}
