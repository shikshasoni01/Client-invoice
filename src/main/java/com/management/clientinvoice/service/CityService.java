package com.management.clientinvoice.service;

import com.management.clientinvoice.domain.City;
import com.management.clientinvoice.dto.CityDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.CityRequestWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface CityService {

    void createCity(CityRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

    List<CityDTO> getAllCity(Long id) throws InvoiceManagementException;

    City getASingleCity(Long id) throws InvoiceManagementException;

    Optional<City> updateCity(Long cityId, CityRequestWrapper request);

    void deleteCity(Long cityId, String acceptLanguage) throws InvoiceManagementException;


}
