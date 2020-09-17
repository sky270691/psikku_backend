package com.psikku.backend.dto.voucher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateVoucherDto {

    private String voucher;
    @JsonProperty("package_id")
    private int testPackageId;
    private String category;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
