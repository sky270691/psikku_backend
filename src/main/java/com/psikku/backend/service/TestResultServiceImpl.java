package com.psikku.backend.service;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public List<TestResult> findAllResultByUserNameAndDateOfTest(String username, String date, String time) {
        String completeDateTime = date+" "+time;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime ldt;
        try {
            ldt = LocalDateTime.parse(completeDateTime,dtf);
        } catch (DateTimeParseException e) {
            throw new TestResultException("date or time format is not valid (date -> 'dd-MM-yyyy' time -> 'HH:mm:ss')");
        }
        List<TestResult> testResultList = testResultRepository.findAllByUser_usernameAndDateOfTest(username, ldt);
        if(!testResultList.isEmpty()){
            return testResultList;
        }else{
            throw new TestResultException("test result not found on the submitted date");
        }
    }

    @Override
    public List<TestResult> findAllByUserName(String username) {
        return testResultRepository.findAllByUser_username(username);
    }

    @Override
    public TestFinalResultDto convertToTestResultDto(TestResult testResult) {
        TestFinalResultDto testResultDto = new TestFinalResultDto();
        if(testResult.getId() != 0){
            testResultDto.setTestDescription(testResult.getTest().getDescription());
            testResultDto.setTestDateTime(testResult.getDateOfTest());
            testResultDto.setResult(testResult.getResult());
        }
        return testResultDto;
    }
}
