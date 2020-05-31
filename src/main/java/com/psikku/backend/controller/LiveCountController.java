package com.psikku.backend.controller;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.service.livecount.LiveCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LiveCountController {

    private LiveCountService liveCountService;

    @Autowired
    public LiveCountController(LiveCountService liveCountService) {
        this.liveCountService = liveCountService;
    }

    @GetMapping("/api/internal/live_count/{testId}")
    public ResponseEntity<?> getRealTimeCount(@PathVariable int testId, @RequestHeader("Voucher") String voucher){

        List<TestFinalResultDto> testFinalResultDtoList = liveCountService.getAllTestResultByTestIdAndVoucher(testId,voucher);
        return new ResponseEntity<>(testFinalResultDtoList, HttpStatus.OK);
    }

}
