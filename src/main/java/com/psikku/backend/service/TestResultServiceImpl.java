package com.psikku.backend.service;

import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class TestResultServiceImpl implements TestResultService {

    @Autowired
    TestResultRepository testResultRepository;

    @Override
    public boolean saveTestResult(TestResult testResult) {
        testResultRepository.save(testResult);
        return true;
    }

    @Override
    public List<TestResult> findAllResultByUserId(long userId) {
        List<TestResult> testResultList =  testResultRepository.findAllByUser_Id(userId);
        if(!testResultList.isEmpty()){
            return testResultList;
        }else{
            throw new TestResultException("test result not found");
        }
    }

    @Override
    public List<TestResult> findAllResultByUserIdAndDateOfTest(long userId, String date, String time) {
        String completeDateTime = date +" "+time;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(completeDateTime,dtf);
        List<TestResult> testResultList = testResultRepository.findAllByUser_idAndDateOfTest(userId, ldt);
        if(!testResultList.isEmpty()){
            return testResultList;
        }else{
            throw new TestResultException("test result not found on the submitted date");
        }
    }

    @Override
    public TestResultDto convertToTestResultDto(TestResult testResult) {
        TestResultDto testResultDto = new TestResultDto();
        if(testResult.getId() != 0){
            testResultDto.setId(testResult.getId());
            testResultDto.setTestId(testResult.getTest().getId());
            testResultDto.setTotalRightAnswer(testResult.getTotalRightAnswer());
            testResultDto.setSurveyCategoryAnswer(testResult.getSurveyCategoryAnswer());
            testResultDto.setUserId(testResult.getUser().getId());
            testResultDto.setTestDate(testResult.getDateOfTest());
        }
        return testResultDto;
    }
}
