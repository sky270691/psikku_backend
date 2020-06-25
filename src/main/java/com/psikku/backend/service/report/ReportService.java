package com.psikku.backend.service.report;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {

    void generateReportByUsernameAndVoucher(String username, String voucher);
    void generateAllReportByVoucher(String voucher);
}
