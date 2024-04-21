package com.management.clientinvoice.service;

import com.management.clientinvoice.domain.State;
import com.management.clientinvoice.dto.StateDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.StateRequestWrapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface StateService {

    List<StateDTO> getAllState(Long id) throws InvoiceManagementException;

   State getASingleState(Long id) throws InvoiceManagementException;

   void createState(StateRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

   Optional<State> updateState(Long stateId, StateRequestWrapper request);

   void deleteState(Long stateId, String acceptLanguage) throws InvoiceManagementException;

}
