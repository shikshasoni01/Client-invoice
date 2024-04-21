package com.management.clientinvoice.service;

import com.management.clientinvoice.dto.*;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.InvoiceFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.InvoiceNumberUpdateRequestWrapper;
import com.management.clientinvoice.requestWrapper.InvoiceRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateInvoiceRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public interface InvoiceService {

	List<InvoiceDTO> getAllInvoices();

    List<MasterDataDTO> getAllMasterDatas();
    void updateInvoice(UpdateInvoiceRequest request, String acceptLanguage) throws MessagingException, FileNotFoundException;

    void updateInvoiceNumber(Long id, InvoiceNumberUpdateRequestWrapper requestWrapper) throws InvoiceManagementException,MessagingException;

    List<BankDTO> getAllBanks();

    List<CompanyDTO> getAllCompany();

    InvoiceDTO getInvoice(Long invoiceId);

    List<PaymentStatusDTO> listInvoiceStatus();

    void createInvoice(InvoiceRequestWrapper request, String acceptLanguage) throws MessagingException;

    void enableInvoice(Long invoiceId, String acceptLanguage) throws InvoiceManagementException;

	void deleteInvoice(Long invoiceId, String acceptLanguage) throws InvoiceManagementException, MessagingException;

    PaginationDTO<InvoiceDTO> getAllInvoicesList(Pageable pageable, InvoiceFilterRequestWrapper request, String filter, String sort, String order) throws InvoiceManagementException;


    File getInvoice(Long invoiceId, String invoiceNumber, String acceptLanguage);

}