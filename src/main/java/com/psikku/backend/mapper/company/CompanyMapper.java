package com.psikku.backend.mapper.company;

import com.psikku.backend.dto.company.CompanyDto;
import com.psikku.backend.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company convertToCompany(CompanyDto companyDto){

        Company company = new Company();
        company.setId(companyDto.getId());
        company.setName(companyDto.getName());
        company.setAddress(companyDto.getAddress());
        company.setCity(companyDto.getCity());
        company.setProvince(companyDto.getProvince());
        company.setPhoneNumber(companyDto.getPhoneNumber());
        company.setEmail(companyDto.getEmail());
        company.setDisplayResult(companyDto.isDisplayResult());
        return company;
    }

}
