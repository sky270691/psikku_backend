package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.KraepelinResultDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.service.submitanswer.SubmitAnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-answers")
public class SubmitAnswerController {

    public final Logger logger;
    private final SubmitAnswerService submitAnswerService;

    @Autowired
    public SubmitAnswerController(SubmitAnswerService submitAnswerService){
        this.submitAnswerService = submitAnswerService;
        logger = LoggerFactory.getLogger(SubmitAnswerController.class);
    }

    @PostMapping
    public ResponseEntity<TestFinalResultDto> submitAnswers(@RequestBody UserAnswerDto userAnswerDto,
                                                            @RequestHeader("Voucher") String voucher){

        System.out.println("body: "+userAnswerDto);

        logger.info("username: '"+getUsername()+"' try to submit answer with voucher: '"+voucher+"'");
        TestFinalResultDto testFinalResultDto= submitAnswerService.calculateResultTestV2(userAnswerDto,voucher.trim());

        logger.info("username: '"+getUsername()+"' answer's for test:'"+testFinalResultDto.getInternalName()+"' calculated successfully");
        return new ResponseEntity<>(testFinalResultDto,HttpStatus.OK);
    }

    @PostMapping("/generic")
    public ResponseEntity<TestFinalResultDto> submitAnswersGeneric(@RequestBody UserAnswerDto userAnswerDto,
                                                                   @RequestHeader("Voucher") String voucher){
        logger.info("username: '"+getUsername()+"' try to submit answer");
        TestFinalResultDto testFinalResultDto= submitAnswerService.calculateGenericTest(userAnswerDto,voucher.trim());

        logger.info("username: '"+getUsername()+"' answer's for test:'"+testFinalResultDto.getInternalName()+"' calculated successfully");
        return new ResponseEntity<>(testFinalResultDto,HttpStatus.OK);
    }

    @PostMapping("/kraepelin")
    public ResponseEntity<?> submitKraepelinAnswer(@RequestBody KraepelinResultDto dto){
        logger.info("username: '"+getUsername()+"' try to submit kraepelin answer");
        submitAnswerService.saveKraepelinResult(dto);
        Map<String,String> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        logger.info("username: '"+getUsername()+"' result submitted successfully");
        return ResponseEntity.ok(returnBody);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
