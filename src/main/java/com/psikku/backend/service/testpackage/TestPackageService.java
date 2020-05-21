package com.psikku.backend.service.testpackage;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Test;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TestPackageService {
    GeneratedPaymentDetailDto generatePackage(TestPackageCreationDto testPackageCreationDto);
}
