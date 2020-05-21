package com.psikku.backend.service.company;

import com.psikku.backend.dto.company.CompanyDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.exception.CompanyException;
import com.psikku.backend.mapper.company.CompanyMapper;
import com.psikku.backend.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final Logger logger;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Company findById(long id) {
        return companyRepository.findById(id).orElseThrow(()-> {
            logger.error("company find by id error");
            return new CompanyException("Company not found");
        });
    }

    @Override
    public Company saveCompany(CompanyDto companyDto) {
        Company company = companyMapper.convertToCompany(companyDto);
        return companyRepository.save(company);
    }
}
