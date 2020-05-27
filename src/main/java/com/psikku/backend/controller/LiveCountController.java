package com.psikku.backend.controller;

import com.psikku.backend.service.testresult.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveCountController {

    private TestResultService testResultService;

    @Autowired
    public LiveCountController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

//    @GetMapping("/api/internal/live_count/{testId}")
//    public ResponseEntity<?> getRealTimeCount(@PathVariable int testId){
//
//    }

}
