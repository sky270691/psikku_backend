package com.psikku.backend.controller;

import com.psikku.backend.dto.TestDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tests")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/")
    public Test addNewTest(@RequestBody TestDto testDto){
        return testService.addNewTest(testDto);
    }

}
