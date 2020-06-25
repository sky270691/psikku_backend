package com.psikku.backend.repository;

import com.psikku.backend.entity.User;
import com.psikku.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher,Long> {

    Optional<Voucher> findVoucherByVoucherCode(String voucherCode);
    void deleteById(long id);
    Optional<Voucher> findByPayment_Id(long paymentId);

}
