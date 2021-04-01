package com.psikku.backend.mapper.testpackage;

import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.TestPackageTest;
import com.psikku.backend.mapper.test.TestMapper;
import com.psikku.backend.service.test.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
        testPackage.setViewType(testPackageCreationDto.getViewType());
        testPackage.setCategory(testPackageCreationDto.getCategory());
        testPackage.setRequiredPreRegister(testPackageCreationDto.isRequiredPreRegister());
        testPackage.setTestPackageTestList(new ArrayList<>());
        testPackageCreationDto.getTestListId().forEach(testId -> {
            Test tempTest = testService.findTestById(testId);
            TestPackageTest newTestPackageTest = new TestPackageTest();
            newTestPackageTest.setTest(tempTest);
            newTestPackageTest.setTestPackage(testPackage);
            testPackage.getTestPackageTestList().add(newTestPackageTest);
        });
        return testPackage;
    }

    public TestPackageDto convertToTestPackageDto(TestPackage testPackage){

        TestPackageDto testPackageDto = new TestPackageDto();
        testPackageDto.setId(testPackage.getId());
        testPackageDto.setName(testPackage.getName());
        testPackageDto.setDescription(testPackage.getDescription());
        testPackageDto.setPrice(testPackage.getPrice());
        testPackageDto.setViewType(testPackage.getViewType());
        testPackageDto.setCategory(testPackage.getCategory());
        testPackageDto.setRequiredPreRegister(testPackage.isRequiredPreRegister());
        List<MinimalTestDto> minimalTestDtoLinkedList = new LinkedList<>();
        if(testPackage.getVoucher() != null && !testPackage.getVoucher().isEmpty()) {
            testPackageDto.setVoucherValidUntil(testPackage.getVoucher().get(0).getValidUntil());
        }
        testPackage.getTestPackageTestList().forEach(test-> minimalTestDtoLinkedList.add(testMapper.convertToMinTestDto(test.getTest())));
        testPackageDto.setMinimalTestDtoList(minimalTestDtoLinkedList);
        testPackageDto.getMinimalTestDtoList().sort(Comparator.comparingInt(MinimalTestDto::getPriority));
        return testPackageDto;
    }


}
