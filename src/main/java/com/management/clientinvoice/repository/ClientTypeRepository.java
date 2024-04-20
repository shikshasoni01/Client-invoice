package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.ClientType;
import com.biz4solutions.clientinvoice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientTypeRepository extends JpaRepository <ClientType,Long> {
    List <ClientType> findAll();

    ClientType findOneById(Long id);

}
