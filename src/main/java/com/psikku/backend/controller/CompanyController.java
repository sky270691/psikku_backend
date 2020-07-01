package com.psikku.backend.controller;

import com.psikku.backend.dto.company.CompanyDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final Logger logger;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping
    public ResponseEntity<String> addNewCompany(@RequestBody CompanyDto companyDto){
        Company company = companyService.saveCompany(companyDto);
        return new ResponseEntity<>(company.getName()+" saved successfully", HttpStatus.CREATED);
    }

}
