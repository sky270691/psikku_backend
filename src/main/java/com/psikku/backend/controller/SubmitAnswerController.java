package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.submitanswer.SubmitAnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        logger.info("username: '"+username+"' try to submit answer");
        TestFinalResultDto testFinalResultDto= submitAnswerService.calculateResultTestV2(userAnswerDto,voucher);

        logger.info("username: '"+username+"' answer's calculated successfully");
        return new ResponseEntity<>(testFinalResultDto,HttpStatus.OK);
    }
    @PostMapping("/generic")
    public ResponseEntity<TestFinalResultDto> submitAnswersGeneric(@RequestBody UserAnswerDto userAnswerDto,
                                                            @RequestHeader("Voucher") String voucher){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        logger.info("username: '"+username+"' try to submit answer");
        TestFinalResultDto testFinalResultDto= submitAnswerService.calculateGenericTest(userAnswerDto,voucher);

        logger.info("username: '"+username+"' answer's calculated successfully");
        return new ResponseEntity<>(testFinalResultDto,HttpStatus.OK);
    }
}
