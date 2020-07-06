package com.psikku.backend.service.report;

import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;

public interface ReportService {

    Resource generateReportByUsernameAndVoucher(String username, String voucher);
    Resource generateAllReportByVoucher(String voucher);
}
