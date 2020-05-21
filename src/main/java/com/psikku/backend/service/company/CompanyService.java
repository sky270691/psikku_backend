package com.psikku.backend.service.company;

import com.psikku.backend.dto.company.CompanyDto;
import com.psikku.backend.entity.Company;

public interface CompanyService {
    Company findById(long id);
    Company saveCompany(CompanyDto companyDto);
}
