package com.psikku.backend.dto.voucher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateVoucherDto {

    private String voucher;
    @JsonProperty("package_id")
    private int testPackageId;

    public int getTestPackageId() {
        return testPackageId;
    }

    public void setTestPackageId(int testPackageId) {
        this.testPackageId = testPackageId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }
}
