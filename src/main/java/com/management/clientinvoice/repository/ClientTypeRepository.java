package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientTypeRepository extends JpaRepository <ClientType,Long> {
    List <ClientType> findAll();

    ClientType findOneById(Long id);

}
