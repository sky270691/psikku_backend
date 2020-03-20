package com.psikku.backend.controller;

import com.psikku.backend.dto.test.FullTestDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.test.TestInsertResponse;
import com.psikku.backend.entity.Test;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tests")
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<TestInsertResponse> addNewTest(@RequestBody @Valid FullTestDto fullTestDto){
        testService.addNewTest(fullTestDto);
        List<Test> savedTests = testService.findAll();
        Test insertedTest = testService.findTestById(savedTests.get(savedTests.size()-1).getId());
        TestInsertResponse response = new TestInsertResponse();
        response.setTestId(insertedTest.getId());
        response.setTestName(insertedTest.getName());
        response.setMessage("Successfully added");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MinimalTestDto>> getAllTests() {

        List<Test> testList = testService.findAll();
        List<MinimalTestDto> minimalTestDtoList = new ArrayList<>();
//        for (test test : testList) {
//            testDtoList.add(testService.convertToTestDto(test));
//        }
        testList.forEach(test -> minimalTestDtoList.add(testService.convertToMinimalTestDto(test)));
        return new ResponseEntity<>(minimalTestDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullTestDto> getTestById(@PathVariable int id){
        Test test =  testService.findTestById(id);
        FullTestDto fullTestDto = testService.convertToFullTestDto(test);
        return new ResponseEntity<>(fullTestDto,HttpStatus.OK);
    }


}
