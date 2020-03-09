package com.psikku.backend.service;

import com.psikku.backend.dto.TestDto;
import com.psikku.backend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestService {
    Test addNewTest(TestDto testDto);
    Test convertToTestEntity(TestDto testDto);
}
