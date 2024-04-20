package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository <PaymentStatus, Long> {
    Optional <PaymentStatus> findById(Long id);

    List <PaymentStatus> findAll();
}
