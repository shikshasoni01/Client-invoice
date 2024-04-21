package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.sequence.InvoiceSequenceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceSequenceNumberRepository extends JpaRepository<InvoiceSequenceNumber, Long> {

}