package com.psikku.backend.service.testresult;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<TestResult> findAllByUserName(String username) {
        return testResultRepository.findAllByUser_username(username);
    }

    @Override
    public TestFinalResultDto convertToTestResultDto(TestResult testResult) {
        TestFinalResultDto testResultDto = new TestFinalResultDto();
        if(testResult.getId() != 0){
            testResultDto.setInternalName(testResult.getTest().getInternalName());
            testResultDto.setTestName(testResult.getTest().getName());
            testResultDto.setTestDateTime(testResult.getDateOfTest());
            testResultDto.setResult(testResult.getResult());
        }
        return testResultDto;
    }

    @Override
    public List<TestResult> findAllResultByTestId(int id) {
        return null;
    }

    @Override
    public List<TestResult> findAllResultByVoucherIdAndUsername(long voucherId, String userName) {

        return testResultRepository.findAllByVoucher_IdAndUser_Username(voucherId,userName);
    }

    @Override
    public List<TestResult> findAllResultByTestIdAndVoucherId(int id, long voucherId) {
        return testResultRepository.findAllByTest_IdAndVoucher_Id(id,voucherId);
    }
}
