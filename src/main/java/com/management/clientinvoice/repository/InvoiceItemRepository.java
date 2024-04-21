package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.Invoice;
import com.management.clientinvoice.domain.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem,Long> {

    @Query(value = "select * from InvoiceItem ids where ids.id = :id ", nativeQuery = true)
    InvoiceItem findOneByIdIgnoreIsActive(@Param("id") Long id);

     Optional <InvoiceItem> findById(Long id);

    List <InvoiceItem> findAll();

   List<InvoiceItem> findAllByInvoice(Invoice invoice);

   void deleteAllByInvoice(Invoice invoice);

   @Query(value = "select count(*) FROM public.invoice_item WHERE invoice_id=:invoiceId",nativeQuery = true)
    Integer findAllByInvoiceCount(@Param("invoiceId") Long invoiceId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM invoice_item where invoice_id=:invoiceId",nativeQuery = true)
    void deleteInvoiceItemForInvoiceId(@Param("invoiceId") Long invoiceId);

}
