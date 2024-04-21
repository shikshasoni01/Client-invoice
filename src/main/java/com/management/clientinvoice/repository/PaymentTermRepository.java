package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.PaymentTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTermRepository extends JpaRepository<PaymentTerm, Long> {
    Optional<PaymentTerm> findById(Long id);

    @Query(value="select * from payment_term order by id",nativeQuery = true)
    List<PaymentTerm> findAllPaymentTermOrderById();
}
