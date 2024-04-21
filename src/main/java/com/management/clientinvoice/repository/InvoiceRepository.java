package com.management.clientinvoice.repository;



import com.management.clientinvoice.domain.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.management.clientinvoice.domain.Invoice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

	@Query(value = "select * from Invoice ids where ids.id = :id ", nativeQuery = true)
	Invoice findOneByIdIgnoreIsActive(@Param("id") Long id);


	@Query(value = "select * from invoice i where i.is_active = true order by i.id ASC ",nativeQuery = true)
	List<Invoice> findAllByIsActive();


	List<Invoice> findByProject(Projects projects);

	@Query(value = "select nextval('DOMESTIC_INVOICE_SEQ')",nativeQuery = true)
	String findDomesticInvoiceSequenceNumber();

	@Query(value = "select nextval('INTERNATIONAL_INVOICE_SEQ')",nativeQuery = true)
	String findInternationalInvoiceSequenceNumber();

	@Query(value = "select nextval('LLC_INVOICE_SEQ')",nativeQuery = true)
	String findLLCInvoiceSequenceNumber();

	@Query(value = "select nextval('COMMON_DRAFT_INVOICE_SEQ')",nativeQuery = true)
	String findCommonDraftInvoiceSequenceNumber();

	@Query(value = "select count(*) FROM public.invoice WHERE payment_status_id =:paymentStatusId and project_id=:projectId",nativeQuery = true)
	Integer findInvoiceForSpecificProjectAndDraftPaymentStatus(@Param("paymentStatusId") Integer paymentStatusId, @Param("projectId") Integer projectId);

	@Modifying
	@Transactional
	@Query(value =" DELETE FROM invoice WHERE id=:invoiceId",nativeQuery = true)
	void deleteInvoiceById(@Param("invoiceId") Long invoiceId);

	@Query(value = "SELECT last_value FROM LLC_INVOICE_SEQ",nativeQuery = true)
	Integer findLastValueOfLLCSequence();

	@Query(value = "SELECT last_value FROM DOMESTIC_INVOICE_SEQ",nativeQuery = true)
	Integer findLastValueOfDomesticSequence();

	@Query(value = "SELECT last_value FROM INTERNATIONAL_INVOICE_SEQ",nativeQuery = true)
	Integer findLastValueOfInternationalSequence();

	@Query(value = "select nextval('DOMESTIC_INVOICE_INDORE_SEQ')",nativeQuery = true)
	String findDomesticInvoiceSequenceNumberForIndoreLocation();

}



