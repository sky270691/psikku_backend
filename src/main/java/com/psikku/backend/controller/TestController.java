package com.psikku.backend.controller;

import com.psikku.backend.dto.test.FullTestDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.test.TestInsertResponseDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.voucher.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/tests")
public class TestController {

    private final Logger logger;
    private final TestService testService;
    private final VoucherService voucherService;

    public TestController(TestService testService,
                          @Lazy VoucherService voucherService) {
        this.voucherService = voucherService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.testService = testService;
    }

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

//    @GetMapping
//    public ResponseEntity<List<MinimalTestDto>> getAllMinimalTests(){
//
//        List<MinimalTestDto> minimalTestDtosList = testService.getAllMinTestList();
//        if(minimalTestDtosList.isEmpty()){
//            throw new TestException("no tests found on server");
//        }
//
//        return new ResponseEntity<>(minimalTestDtosList,HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTestById(@PathVariable int id, @RequestHeader(value = "Voucher",required = false) String voucher){
        if(voucher == null || voucher.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        if(voucherService.getVoucherByCode(voucher).isRest()){
            return new ResponseEntity<>("rest:Waktu istirahat sedang berlangsung, silahkan mengerjakan kembali setelah waktu istirahat selesai!",HttpStatus.BAD_REQUEST);
        }

        if(!voucherService.validateUrutan(voucher,id)){
            return new ResponseEntity<>("stop:Tes belum bisa dilanjutkan silahkan tunggu instruksi!",HttpStatus.BAD_REQUEST);
        }
        Test test =  testService.findTestById(id);

        FullTestDto fullTestDto = testService.convertToFullTestDto(test);
        logger.info("username: '"+getUsername()+"' getting test: '"+fullTestDto.getInternalName()+"'");
        return new ResponseEntity<>(fullTestDto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MinimalTestDto>> getAllMinimalTestsByVoucher(@RequestHeader("Voucher") String voucher){
        List<MinimalTestDto> minimalTestDtoList = testService.getMinTestByVoucher(voucher);
        return new ResponseEntity<>(minimalTestDtoList,HttpStatus.OK);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
