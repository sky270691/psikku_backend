package com.psikku.backend.repository;

import com.psikku.backend.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    List<TestResult> findAllByUser_Username(String userName);
}
