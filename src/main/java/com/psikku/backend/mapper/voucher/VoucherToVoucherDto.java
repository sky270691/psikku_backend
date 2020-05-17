package com.psikku.backend.mapper.voucher;

import com.psikku.backend.dto.voucher.VoucherDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoucherToVoucherDto {

    //Todo implement this voucher mapper

    static CompanyRepository companyRepository;

    public static Voucher convertToVoucher(VoucherDto voucherDto){
        Voucher voucher = new Voucher();
        voucher.setId(voucherDto.getId());
        voucher.setVoucherCode(voucherDto.getVoucherCode());
        voucher.setValidUntil(voucherDto.getValidUntil());
//        voucher.setCompany();
        return null;
    }

    private static Company getCompany(String companyName){
        return null;
    }

}
