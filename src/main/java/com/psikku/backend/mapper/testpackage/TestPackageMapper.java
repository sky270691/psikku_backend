package com.psikku.backend.mapper.testpackage;

import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.mapper.test.TestMapper;
import com.psikku.backend.service.test.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TestPackageMapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TestService testService;
    private TestMapper testMapper;

    @Autowired
    public TestPackageMapper(TestService testService, TestMapper testMapper) {
        this.testService = testService;
        this.testMapper = testMapper;
    }

    public TestPackage convertToTestPackage(TestPackageCreationDto testPackageCreationDto){
        TestPackage testPackage = new TestPackage();
        testPackage.setName(testPackageCreationDto.getName());
        testPackage.setPrice(testPackageCreationDto.getPrice());
        testPackage.setDescription(testPackageCreationDto.getDescription());
        testPackage.setTestList(new ArrayList<>());
        testPackageCreationDto.getTestIdList().forEach((testId)-> {
            Test tempTest = testService.findTestById(testId);
            testPackage.getTestList().add(tempTest);
        });
        return testPackage;
    }

    public TestPackageDto convertToTestPackageDto(TestPackage testPackage){

        TestPackageDto testPackageDto = new TestPackageDto();
        testPackageDto.setId(testPackage.getId());
        testPackageDto.setName(testPackage.getName());
        testPackageDto.setDescription(testPackage.getDescription());
        testPackageDto.setPrice(testPackage.getPrice());
        testPackageDto.setMinimalTestDtoList(new ArrayList<>());
        testPackage.getTestList().forEach(test-> testPackageDto.getMinimalTestDtoList().add(testMapper.convertToMinTestDto(test)));
        return testPackageDto;
    }


}
