package com.management.clientinvoice.service;

import com.management.clientinvoice.domain.Country;
import com.management.clientinvoice.dto.CountryDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.CountryRequestWrapper;
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
