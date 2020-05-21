package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.service.testresult.TestResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/result")
public class TestResultController {

    public final Logger logger = LoggerFactory.getLogger(TestResultController.class);

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

//    @GetMapping
//    public List<TestResultDto> getTestResultByUserIdAndDateOfTest(@RequestParam String date,
//                                                                  @RequestParam String time){
//        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        List<TestResult> testResultList = testResultService.findAllResultByUserNameAndDateOfTest(username, date, time);
//        List<TestResultDto> testResultDtoList = new ArrayList<>();
//        if(!testResultList.isEmpty()){
//            testResultList.forEach(testResult -> testResultDtoList.add(testResultService.convertToTestResultDto(testResult)));
//            return testResultDtoList;
//        }else{
//            throw new TestResultException("No test found");
//        }
//    }

    @GetMapping
    public List<TestFinalResultDto> getTestResultByUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<TestResult> testResultList = testResultService.findAllByUserName(username);
        List<TestFinalResultDto> testFinalResultDtoList = new ArrayList<>();
        if(!testResultList.isEmpty()){
            testResultList.forEach(testResult -> testFinalResultDtoList.add(testResultService.convertToTestResultDto(testResult)));
            logger.info("username: '"+username+"' getting test result success");
            return testFinalResultDtoList;
        }else{
            logger.error("username: '"+username+"' no test found");
            throw new TestResultException("No test found");
        }
    }

}
