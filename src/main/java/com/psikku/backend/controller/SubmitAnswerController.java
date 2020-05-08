package com.psikku.backend.controller;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.SubmitAnswerService;
import com.psikku.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user-answers")
public class SubmitAnswerController {

    public final Logger logger = LoggerFactory.getLogger(SubmitAnswerController.class);

    @Autowired
    UserService userService;

    @Autowired
    TestService testService;

    @Autowired
    SubmitAnswerService submitAnswerService;

//    @PostMapping
//    public ResponseEntity submitAnswers(@RequestBody List<SubmittedAnswerDto> submittedAnswerDto,
//                                                @RequestHeader("Authorization") String auth){
//        String[] authHeader = auth.split(" ");
//        String token = authHeader[1];
//        String username = userService.getUserNameFromToken(token);
//        User user = userService.findUserByUsername(username);
//        List<SubmittedAnswer> submittedAnswerList = submitAnswerService.convertToSubmittedAnswerList(submittedAnswerDto,user);
//        List<SubmittedAnswerDto> answerDtoList = submitAnswerService.convertToSubmittedAnswerDtoList(
//                submitAnswerService.saveUserAnswer(submittedAnswerList));
//        ResponseEntity responseEntity = new ResponseEntity(answerDtoList, HttpStatus.OK);
//        return responseEntity;
//    }

    @PostMapping
    public ResponseEntity<String> submitAnswers(@RequestBody List<SubmittedAnswerDto> submittedAnswerDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        logger.info("username: '"+username+"' try to submit answer");
//        User user = userService.findByUsername(username);
//        List<SubmittedAnswer> submittedAnswerList = submitAnswerService.convertToSubmittedAnswerList(submittedAnswerDto,user);
//        submitAnswerService.saveUserAnswer(submittedAnswerList);
         submitAnswerService.calculateResultTest(submittedAnswerDto);
//        return new ArrayList<>();

        logger.info("username: '"+username+"' answer's calculated successfully");
        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    @PostMapping("/v2")
    public ResponseEntity<String> submitAnswers2(@RequestBody UserAnswerDto userAnswerDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        logger.info("username: '"+username+"' try to submit answer");
//        User user = userService.findByUsername(username);
//        List<SubmittedAnswer> submittedAnswerList = submitAnswerService.convertToSubmittedAnswerList(submittedAnswerDto,user);
//        submitAnswerService.saveUserAnswer(submittedAnswerList);
        submitAnswerService.calculateResultTestV2(userAnswerDto);
//        return new ArrayList<>();

        logger.info("username: '"+username+"' answer's calculated successfully");
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
