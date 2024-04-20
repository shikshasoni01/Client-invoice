package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.PaymentMode;
import com.biz4solutions.clientinvoice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode,Long> {

    Optional <PaymentMode> findById(Long id);

}
