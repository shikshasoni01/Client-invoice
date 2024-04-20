package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,String> {
}
