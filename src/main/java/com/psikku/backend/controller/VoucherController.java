package com.psikku.backend.controller;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.dto.voucher.VoucherParticipantAddDto;
import com.psikku.backend.service.voucher.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;
    private final Logger logger;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping("/redeem")
    public ResponseEntity<String> validateVoucher(@RequestBody ValidateVoucherDto validateVoucherDto){
        logger.info("username:'"+getUsername()+"' trying to redeem voucher:'"+validateVoucherDto.getVoucher()+"'");
        boolean status = voucherService.validateStatus(validateVoucherDto);
        if(status){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/redeem/company")
    public ResponseEntity<?> validateVoucherV2(@RequestParam String voucher, @RequestParam String category){
        TestPackageDto dto = voucherService.validateStatusV2(voucher,category);

        Map<String,Object> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        returnBody.put("test_package",dto);

        return ResponseEntity.ok(returnBody);
    }


    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneratedPaymentDetailDto> generateVoucherWithExistingPackage(Integer packageId,
                                                                                         Integer totalUser,
                                                                                         Long companyId){
        GeneratedPaymentDetailDto paymentDetailDto = voucherService.generateVoucherCurrentPackage(packageId,totalUser,companyId);
        return new ResponseEntity<>(paymentDetailDto,HttpStatus.OK);
    }


    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }



    @PostMapping("/add-participant")
    public ResponseEntity<?> addNewParticipant(@RequestBody VoucherParticipantAddDto emailList,
                                               @RequestParam String voucher){

        Map<String,String> returnBody = new LinkedHashMap<>();


        voucherService.registerUserToVoucher(emailList.getEmailList(),voucher);

        returnBody.put("status","success");
        return ResponseEntity.ok(returnBody);
    }

}
