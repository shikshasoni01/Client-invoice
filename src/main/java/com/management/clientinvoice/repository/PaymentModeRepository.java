package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode,Long> {

    Optional <PaymentMode> findById(Long id);

}
