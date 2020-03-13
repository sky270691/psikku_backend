package com.psikku.backend.controller;

import com.psikku.backend.dto.Test.SubmittedAnswerDto;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.TestService;
import com.psikku.backend.service.SubmitAnswerService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-answers")
public class SubmitAnswerController {

    @Autowired
    UserService userService;

    @Autowired
    TestService testService;

    @Autowired
    SubmitAnswerService submitAnswerService;


    @PostMapping
    public ResponseEntity<List<SubmittedAnswerDto>> submitAnswers(@RequestBody List<SubmittedAnswerDto> submittedAnswerDto,
                                                @RequestHeader("Authorization") String auth){
        String[] authHeader = auth.split(" ");
        String token = authHeader[1];
        String username = userService.getUserNameFromToken(token);
        User user = userService.findUserByUsername(username);
        List<SubmittedAnswer> submittedAnswerList = submitAnswerService.convertToSubmittedAnswerList(submittedAnswerDto,user);
        List<SubmittedAnswerDto> answerDtoList = submitAnswerService.convertToSubmittedAnswerDtoList(
                submitAnswerService.saveUserAnswer(submittedAnswerList));
        ResponseEntity<List<SubmittedAnswerDto>> responseEntity = new ResponseEntity(answerDtoList, HttpStatus.OK);
        return responseEntity;
    }
}
