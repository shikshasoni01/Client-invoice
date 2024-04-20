package com.biz4solutions.clientinvoice.service;

import java.util.List;

import com.biz4solutions.clientinvoice.dto.ClientDTO;
import com.biz4solutions.clientinvoice.dto.ClientTypeDTO;
import com.biz4solutions.clientinvoice.dto.PaginationDTO;
import com.biz4solutions.clientinvoice.dto.PaymentStatusDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.ClientFilterRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.ClientRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.UpdateClientRequestWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface ClientService {


    List<ClientDTO> getAllClient();

    void updateClient(UpdateClientRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

    ClientDTO getASingleClient(Long id) throws InvoiceManagementException;

    List<ClientTypeDTO> listClientType();
    void createClient(ClientRequestWrapper request,String acceptLanguage) throws InvoiceManagementException;

    void enableClient(Long  clientId, String acceptLanguage) throws InvoiceManagementException;

    void deleteClient(Long clientId, String acceptLanguage) throws InvoiceManagementException;

    PaginationDTO<ClientDTO> getAllClientList(Pageable pageable, ClientFilterRequestWrapper request, String filter, String sort, String order) throws InvoiceManagementException;

}