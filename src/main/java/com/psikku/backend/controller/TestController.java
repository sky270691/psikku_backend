package com.psikku.backend.controller;

import com.psikku.backend.dto.test.FullTestDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.test.TestInsertResponseDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<TestInsertResponseDto> addNewTest(@RequestBody @Valid FullTestDto fullTestDto){
        testService.addNewTest(fullTestDto);
        List<Test> savedTests = testService.findAll();
        Test insertedTest = testService.findTestById(savedTests.get(savedTests.size()-1).getId());
        TestInsertResponseDto response = new TestInsertResponseDto();
        response.setTestId(insertedTest.getId());
        response.setTestName(insertedTest.getName());
        response.setMessage("Successfully added");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FullTestDto>> getAllTests() {

        List<Test> testList = testService.findAll();
        List<FullTestDto> fullTestDtoList = new ArrayList<>();
//        for (test test : testList) {
//            testDtoList.add(testService.convertToTestDto(test));
//        }
        testList.forEach(test -> fullTestDtoList.add(testService.convertToFullTestDto(test)));
        return new ResponseEntity<>(fullTestDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullTestDto> getTestById(@PathVariable int id){
        Test test =  testService.findTestById(id);
        FullTestDto fullTestDto = testService.convertToFullTestDto(test);
        return new ResponseEntity<>(fullTestDto,HttpStatus.OK);
    }


}
