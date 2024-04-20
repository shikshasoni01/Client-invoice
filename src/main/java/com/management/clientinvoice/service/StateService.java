package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.domain.Country;
import com.biz4solutions.clientinvoice.domain.State;
import com.biz4solutions.clientinvoice.dto.CountryDTO;
import com.biz4solutions.clientinvoice.dto.StateDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CountryRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.StateRequestWrapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface StateService {

    List<StateDTO> getAllState(Long id) throws InvoiceManagementException;

   State getASingleState(Long id) throws InvoiceManagementException;

   void createState(StateRequestWrapper request,String acceptLanguage) throws InvoiceManagementException;

   Optional<State> updateState(Long stateId, StateRequestWrapper request);

   void deleteState(Long stateId, String acceptLanguage) throws InvoiceManagementException;

}
