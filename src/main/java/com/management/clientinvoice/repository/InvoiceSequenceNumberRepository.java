package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.sequence.InvoiceSequenceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceSequenceNumberRepository extends JpaRepository<InvoiceSequenceNumber, Long> {

}