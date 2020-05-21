package com.psikku.backend.mapper.voucher;

import com.psikku.backend.dto.voucher.VoucherDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoucherMapper {

    public VoucherDto convertEntityToVoucherDto(Voucher voucher){
        VoucherDto voucherDto = new VoucherDto();
        voucherDto.setId(voucher.getId());
        voucherDto.setVoucherCode(voucher.getVoucherCode());
        voucherDto.setValid(voucher.isValid());
        voucherDto.setUserCount(voucher.getUserCount());
        voucherDto.setCreateTime(voucher.getCreateTime());
        voucherDto.setValidUntil(voucher.getValidUntil());
        voucherDto.setCompanyName(voucher.getCompany().getName());
        return voucherDto;
    }

}
