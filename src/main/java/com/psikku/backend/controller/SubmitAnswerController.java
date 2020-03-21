package com.psikku.backend.controller;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.SubmitAnswerService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user-answers")
public class SubmitAnswerController {

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
    public List<String> submitAnswers(@RequestBody List<SubmittedAnswerDto> submittedAnswerDto,
                                        @RequestHeader("Authorization") String auth){
        String[] authHeader = auth.split(" ");
        String token = authHeader[1];
        String username = userService.getUserNameFromToken(token);
        User user = userService.findUserByUsername(username);
        List<SubmittedAnswer> submittedAnswerList = submitAnswerService.convertToSubmittedAnswerList(submittedAnswerDto,user);
        submitAnswerService.saveUserAnswer(submittedAnswerList);
        submitAnswerService.calculateResultTest(submittedAnswerList);
        return new ArrayList<>();
    }
}
