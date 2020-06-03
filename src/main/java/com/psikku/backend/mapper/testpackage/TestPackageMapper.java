package com.psikku.backend.mapper.testpackage;

import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestPackage;
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

    @Autowired
    public TestPackageMapper(TestService testService) {
        this.testService = testService;
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

}
