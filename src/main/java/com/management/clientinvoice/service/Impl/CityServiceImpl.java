package com.management.clientinvoice.service.Impl;


import com.management.clientinvoice.domain.City;
import com.management.clientinvoice.domain.State;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.CityDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.CityRepository;
import com.management.clientinvoice.repository.StateRepository;
import com.management.clientinvoice.requestWrapper.CityRequestWrapper;
import com.management.clientinvoice.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.management.clientinvoice.service.CityService;

import java.util.*;

@Service
public class CityServiceImpl implements CityService{


    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private StateRepository stateRepository;


    @Override
    public void createCity(CityRequestWrapper request,String acceptLanguage) throws InvoiceManagementException {
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }

        City newCity = new City();
        City city = cityRepository.findByCityNameIgnoreCase(request.getCityName());

        if(city != null && city.getIsActive().equals(false)) {
            city.setIsActive(true);
            city.setIsDelete(false);
            cityRepository.save(city);
        }else if(city == null) {

            newCity.setCityName(request.getCityName());
            newCity.setCreatedBy(loggedInUser.getId().toString());

            Optional<State> state = stateRepository.findById(request.getStateId());
            if(state.isPresent()) {
                newCity.setState(state.get());
            }else{
                throw new InvoiceManagementException(
                        messageSource.getMessage("stateNotFound", null, new Locale(acceptLanguage)), 400);
            }
            cityRepository.save(newCity);
        }else{
            throw new InvoiceManagementException(messageSource.getMessage("cityAlreadyExists", null, new Locale(acceptLanguage)),
                    500);
        }

    }


    @Override
    public List<CityDTO> getAllCity(Long id) throws InvoiceManagementException {

        List<City> cities = cityRepository.findAllByStateId(id);
        List<CityDTO> cityDTOS = new ArrayList<>();


        if (cities != null) {
            for (City city : cities) {
                CityDTO cityDTO = new CityDTO();
                cityDTO.setId(city.getId());
                cityDTO.setCityName(city.getCityName());
                cityDTO.setStateId(city.getState().getId());
                cityDTOS.add(cityDTO);
            }
        } else {
            throw new InvoiceManagementException(commonService.getMessage("cityNotPresentWithThisStateId"),
                    400);
        }
//		System.out.println(invoice.size());
        return cityDTOS;
    }

    @Override
    public City getASingleCity(Long id) throws InvoiceManagementException {
        City city = cityRepository.findOneByIdIgnoreIsActive(id);
        if(city != null) {
            return city;
        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("cityNotFound", null, new Locale("en")));

        }
    }

    @Override
    public Optional<City> updateCity(Long cityId, CityRequestWrapper request) {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        Optional<City> city= cityRepository.findById(cityId);
   //     City city1 = cityRepository.findByCityNameIgnoreCase(request.getCityName());
        if(!city.isEmpty()) {
//        City city1= cityRepository.findByCityNameIgnoreCase(request.getCityName());
//
//        if(city1 != null && city.get().getIsActive().equals(false)) {
//            city.get().setIsActive(true);
//            city.get().setIsDelete(false);
//            cityRepository.save(city1);
//        }

            city.get().setUpdatedBy(loggedInUser.getId().toString());
            if (!request.getCityName().isEmpty()) {
                city.get().setCityName(request.getCityName());
            } else {
                city.get().getCityName();
            }

            State state = stateRepository.findOneByIdIgnoreIsActive(request.getStateId());
            if(state != null) {
                state.setId(request.getStateId());
                city.get().getState();
            }else{
                throw new InvoiceManagementException(messageSource.getMessage("stateNotFound", null, new Locale("en")),
                        500);
            }

        }else if(city.isEmpty()){
            throw new InvoiceManagementException(messageSource.getMessage("cityNotFound", null, new Locale("en")),
                    500);
        }else{
            throw new InvoiceManagementException(messageSource.getMessage("cityAlreadyExists", null, new Locale("en")),
                    500);
        }
        cityRepository.save(city.get());

        return city;
    }

    @Override
    public void deleteCity(Long cityId, String acceptLanguage) throws InvoiceManagementException {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }

        City city = cityRepository.findOneByIdIgnoreIsActive(cityId);
        if(city != null) {
            city.setIsActive(false);
            city.setIsDelete(true);
            city.setDeletedAt(new Date());
            city.setDeletedBy(loggedInUser.getId().toString());
            cityRepository.save(city);
        }else{
            throw new InvoiceManagementException(messageSource.getMessage("cityNotFound", null, new Locale("en")),
                    500);
        }
    }
}
