package com.psikku.backend.controller;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/redeem")
    public ResponseEntity<String> validateVoucher(@RequestBody ValidateVoucherDto validateVoucherDto){
        boolean status = voucherService.validateStatus(validateVoucherDto);
        if(status){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/generate",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneratedPaymentDetailDto> generateVoucherWithExistingPackage(int packageId,int totalUser,long companyId){
        GeneratedPaymentDetailDto paymentDetailDto = voucherService.generateVoucherCurrentPackage(packageId,totalUser,companyId);
        return new ResponseEntity<>(paymentDetailDto,HttpStatus.OK);
    }

}
