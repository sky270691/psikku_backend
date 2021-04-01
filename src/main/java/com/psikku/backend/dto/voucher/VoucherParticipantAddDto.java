package com.psikku.backend.dto.voucher;

import java.util.List;

public class VoucherParticipantAddDto {

    private List<String> emailList;

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }
}
