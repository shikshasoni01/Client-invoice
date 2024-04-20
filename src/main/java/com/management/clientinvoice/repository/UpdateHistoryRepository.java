package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory,Long> {
    @Query(value = "select count(*) from invoice i where i.invoice_number = :invoiceNumber", nativeQuery = true)
    Integer findAllInvoiceNumberIgnoreIsActive(@Param("invoiceNumber") String invoiceNumber);

}
