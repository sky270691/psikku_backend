package com.psikku.backend.controller;

import com.psikku.backend.dto.Test.FullTestDto;
import com.psikku.backend.dto.Test.TestDto;
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
    public Test addNewTest(@RequestBody @Valid FullTestDto fullTestDto){
        return testService.addNewTest(fullTestDto);
    }

    @GetMapping
    public ResponseEntity<List<TestDto>> getAllTests() {

        List<Test> testList = testService.findAll();
        List<TestDto> testDtoList = new ArrayList<>();
//        for (Test test : testList) {
//            testDtoList.add(testService.convertToTestDto(test));
//        }
        testList.forEach(test -> testDtoList.add(testService.convertToMinimalTestDto(test)));
        return new ResponseEntity<>(testDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public TestDto getTestById(@PathVariable int id){
        Test test =  testService.findTestById(id);
        TestDto testDto = testService.convertToMinimalTestDto(test);
        return testDto;
    }


}
