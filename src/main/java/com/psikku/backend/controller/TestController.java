package com.psikku.backend.controller;

import com.psikku.backend.dto.test.FullTestDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.test.TestInsertResponseDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tests")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

//    private String username = SecurityContextHolder.getContext().getAuthentication().getName();

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<TestInsertResponseDto> addNewTest(@RequestBody @Valid FullTestDto fullTestDto){
        testService.addNewTest(fullTestDto);
        List<Test> savedTests = testService.findAll();
        Test insertedTest = testService.findTestById(savedTests.get(savedTests.size()-1).getId());
        TestInsertResponseDto response = new TestInsertResponseDto();
        response.setTestId(insertedTest.getId());
        response.setTestName(insertedTest.getName());
        response.setMessage("Successfully added");

//        logger.info();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<List<FullTestDto>> getAllTests() {
//        List<Test> testList = testService.findAll();
//        List<FullTestDto> fullTestDtoList = new ArrayList<>();
//        testList.forEach(test -> fullTestDtoList.add(testService.convertToFullTestDto(test)));
//        return new ResponseEntity<>(fullTestDtoList, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<MinimalTestDto>> getAllMinimalTests(){

        List<MinimalTestDto> minimalTestDtosList = testService.getAllMinTestList();
        if(minimalTestDtosList.isEmpty()){
            throw new TestException("no tests found on server");
        }

        return new ResponseEntity<>(minimalTestDtosList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullTestDto> getTestById(@PathVariable int id){
        Test test =  testService.findTestById(id);
        FullTestDto fullTestDto = testService.convertToFullTestDto(test);
        return new ResponseEntity<>(fullTestDto,HttpStatus.OK);
    }


}
