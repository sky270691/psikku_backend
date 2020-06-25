package com.psikku.backend.repository;

import com.psikku.backend.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    List<TestResult> findAllByUser_Id(long userId);
    List<TestResult> findAllByUser_username(String username);
    List<TestResult> findAllByUser_UsernameAndDateOfTest(String username, LocalDateTime dateTime);
    List<TestResult> findAllByVoucher_IdAndUser_Username(long voucherId, String userName);
    List<TestResult> findAllByTest_IdAndVoucher_Id(int testId, long voucherId);
    List<TestResult> findAllByVoucher_VoucherCodeAndUser_Username(String voucher, String username);
    List<TestResult> findAllByVoucher_VoucherCode(String voucher);
}
