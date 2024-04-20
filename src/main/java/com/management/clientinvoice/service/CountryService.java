package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.domain.Country;
import com.biz4solutions.clientinvoice.dto.CountryDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CountryRequestWrapper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Service
public interface CountryService {

    List<CountryDTO> getAllCountry();

   Country getASingleCountry(Long id) throws InvoiceManagementException;

    Optional<Country> updateCountry(Long countryId,@Valid CountryRequestWrapper request);

    void deleteCountry(Long countryId, String acceptLanguage) throws InvoiceManagementException;

   void createCountry(@Valid CountryRequestWrapper request, String acceptLanguage) throws InvoiceManagementException, MessagingException;

}
