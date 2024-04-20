package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.domain.City;
import com.biz4solutions.clientinvoice.dto.CityDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CityRequestWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface CityService {

    void createCity(CityRequestWrapper request,String acceptLanguage) throws InvoiceManagementException;

    List<CityDTO> getAllCity(Long id) throws InvoiceManagementException;

    City getASingleCity(Long id) throws InvoiceManagementException;

    Optional<City> updateCity(Long cityId, CityRequestWrapper request);

    void deleteCity(Long cityId, String acceptLanguage) throws InvoiceManagementException;


}
