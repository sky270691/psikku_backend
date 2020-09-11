package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.service.report.ReportService;
import com.psikku.backend.service.testresult.TestResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/result")
public class TestResultController {

    public final Logger logger = LoggerFactory.getLogger(TestResultController.class);

    @Autowired
    TestResultService testResultService;

    @Autowired
    ReportService reportService;

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
        List<TestResult> testResultList = testResultService.findAllByUserName(getUsername());
        List<TestFinalResultDto> testFinalResultDtoList = new ArrayList<>();
        if(!testResultList.isEmpty()){
            testResultList.forEach(testResult -> {
                TestFinalResultDto dto = testResultService.convertToTestResultDto(testResult);
                dto.setHide(!testResult.getVoucher().getCompany().isDisplayResult());
                testFinalResultDtoList.add(dto);
            });
            logger.info("username: '"+getUsername()+"' getting test result success");
            return testFinalResultDtoList;
        }else{
            logger.error("username: '"+getUsername()+"' no test found");
            throw new TestResultException("No test found");
        }
    }

//    @GetMapping("/{voucher}/{username}")
//    public ResponseEntity<Resource> getPdfReport(@PathVariable String voucher, @PathVariable String username, HttpServletRequest request){
////        List<TestFinalResultDto> testFinalResultDtoList = testResultService.findAllResultByVoucherAndUsername(voucher,username);
//        Resource resource = reportService.generateReportByUsernameAndVoucher(username,voucher);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"");
//        try {
//            headers.add(HttpHeaders.CONTENT_TYPE,request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()));
//            return new ResponseEntity<>(resource, headers,HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    @GetMapping("/{voucher}/{username}")
    public ResponseEntity<?> getPdfReport(@PathVariable String voucher, @PathVariable String username, HttpServletRequest request){
        Resource resource = reportService.generateReportByUsernameAndVoucher(username,voucher);
        try {
            HttpHeaders headers = new HttpHeaders();
            byte[] data = Files.readAllBytes(resource.getFile().toPath());
            Files.deleteIfExists(resource.getFile().toPath());
            headers.add(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+resource.getFile().getName());
            headers.add(HttpHeaders.CONTENT_TYPE,"application/pdf");
            return new ResponseEntity<>(data,headers,HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            
            return null;
        }
    }

//    @GetMapping("/{voucher}")
//    public void getAllPdfReportByVoucher(@PathVariable String voucher, HttpServletResponse response, HttpServletRequest request){
//        Resource resource = reportService.generateAllReportByVoucher(voucher);
//        try {
//            response.addHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+resource.getFile().getName());
//            response.addHeader(HttpHeaders.CONTENT_TYPE,request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()));
//            OutputStream out = response.getOutputStream();
//            out.write(Files.readAllBytes(resource.getFile().toPath()));
//            out.close();
//            Files.deleteIfExists(resource.getFile().toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @GetMapping(value = "/{voucher}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getAllPdfReportByVoucher(@PathVariable String voucher, HttpServletRequest request){
        Resource resource = reportService.generateAllReportByVoucher(voucher);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,"inline; filename="+resource.getFile().getName());
            headers.add(HttpHeaders.CONTENT_TYPE,request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()));
            byte[] data = Files.readAllBytes(resource.getFile().toPath());
            Files.deleteIfExists(resource.getFile().toPath());
            return new ResponseEntity<>(data,headers,HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("reach here");
            return null;
        }
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
