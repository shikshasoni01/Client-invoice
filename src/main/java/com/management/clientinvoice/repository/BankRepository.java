package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,String> {
}
