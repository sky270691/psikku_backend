package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/result")
public class TestResultController {

    @Autowired
    TestResultService testResultService;

//    @GetMapping("/{user-id}")
//    public List<TestResultDto> getTestResultByUserId(@PathVariable("user-id") long userId){
//        List<TestResult> testResultList = testResultService.findAllResultByUserId(userId);
//        List<TestResultDto> testResultDtoList = new ArrayList<>();
//        testResultList.forEach(testResult -> {
//            testResultDtoList.add(testResultService.convertToTestResultDto(testResult));
//        });
//        if(!testResultDtoList.isEmpty()){
//            return testResultDtoList;
//        }else{
//            throw new TestResultException("No test result found");
//        }
//    }

    @GetMapping("/{user-id}")
    public List<TestResultDto> getTestResultByUserIdAndDateOfTest(@PathVariable("user-id") long userId,
                                                                  @RequestParam String date,
                                                                  @RequestParam String time){
        List<TestResult> testResultList = testResultService.findAllResultByUserIdAndDateOfTest(userId, date, time);
        List<TestResultDto> testResultDtoList = new ArrayList<>();
        if(!testResultList.isEmpty()){
            testResultList.forEach(testResult -> {
                testResultDtoList.add(testResultService.convertToTestResultDto(testResult));
            });
            return testResultDtoList;
        }else{
            throw new TestResultException("No test found");
        }
    }
}
