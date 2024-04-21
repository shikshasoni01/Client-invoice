package com.management.clientinvoice.service.Impl;

import java.util.*;

import com.management.clientinvoice.domain.Country;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.StateDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.CountryRepository;
import com.management.clientinvoice.requestWrapper.StateRequestWrapper;
import com.management.clientinvoice.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.management.clientinvoice.domain.State;
import com.management.clientinvoice.repository.StateRepository;
import com.management.clientinvoice.service.StateService;

@Service
public class StateServiceImpl implements StateService{


    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateService stateService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private CountryRepository countryRepository;


    @Override
    public List<StateDTO> getAllState(Long id) throws InvoiceManagementException {

        List<State> states = stateRepository.findAllByCountryId(id);
        List<StateDTO> stateDTOs = new ArrayList<>();

        if (states != null) {
            for (State state2 : states) {
                StateDTO stateDTO = new StateDTO();
                stateDTO.setId(state2.getId());
                stateDTO.setStateName(state2.getStateName());
                stateDTO.setCountryId(state2.getCountry().getId());
                stateDTOs.add(stateDTO);
            }
        }else{
            throw new InvoiceManagementException(commonService.getMessage("StateNotPresentWithThisCountryId"),
                    400);
        }

        return stateDTOs;
    }


    @Override
    public State getASingleState(Long id) throws InvoiceManagementException {

         State state = stateRepository.findOneByIdIgnoreIsActive(id);

        if(state != null){
            return state;
        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("stateNotFound", null, new Locale("en")));
        }

    }

    @Override
    public void createState(StateRequestWrapper request,String acceptLanguage) throws InvoiceManagementException {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        State newState = new State();
        State state = stateRepository.findByStateNameIgnoreCase (request.getStateName());

        newState.setCreatedBy(loggedInUser.getId().toString());
      //  if (request.getStateName ())
        if(state!= null && state.getIsActive().equals(false)) {
            state.setIsActive(true);
            state.setIsDelete(false);
            stateRepository.save(state);
        }else if(request.getStateName ().isEmpty()) {
            throw new InvoiceManagementException(messageSource.getMessage("requiredFieldIsMissing", null, new Locale(acceptLanguage)),
                    400);}
        else if(state == null) {
          //  if (request.getStateName ().equals (stateRepository ))
            newState.setStateName(request.getStateName());

            Optional<Country> country = countryRepository.findById(request.getCountryId());
            if (country.isPresent()) {
                newState.setCountry(country.get());
            } else {
                throw new InvoiceManagementException(
                        messageSource.getMessage("countryNotFound", null, new Locale(acceptLanguage)), 400);
            }
            stateRepository.save(newState);
        }else{

            throw new InvoiceManagementException(messageSource.getMessage("stateAlreadyExists", null, new Locale(acceptLanguage)),
                    500);
        }
    }


    @Override
    public Optional<State> updateState(Long stateId, StateRequestWrapper request) {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        Optional<State> state = stateRepository.findById(stateId);

        state.get().setUpdatedBy(loggedInUser.getId().toString());
            if (!state.isEmpty ( )) {
                if (!request.getStateName ( ).isEmpty ( )) {
                    state.get ( ).setStateName ( request.getStateName ( ) );
                } else {
                    state.get ( ).getStateName ( );
                }

                Country country = countryRepository.findOneByIdIgnoreIsActive ( request.getCountryId ( ) );
                if (country != null) {
                    country.setId ( request.getCountryId ( ) );
                    state.get ( ).getCountry ( );

                } else {
                    throw new InvoiceManagementException ( messageSource.getMessage ( "countryNotFound", null, new Locale ( "en" ) ),
                            500 );
                }
            } else if (state.isEmpty ( )) {
                throw new InvoiceManagementException ( messageSource.getMessage ( "stateNotFound", null, new Locale ( "en" ) ),
                        500 );
            } else {
                throw new InvoiceManagementException ( messageSource.getMessage ( "stateAlreadyExists", null, new Locale ( "en" ) ),
                        500 );
            }


        stateRepository.save(state.get());
        return state;

    }

    @Override
    public void deleteState(Long stateId, String acceptLanguage) throws InvoiceManagementException {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        State state = stateRepository.findOneByIdIgnoreIsActive(stateId);
        if(state != null) {
            state.setIsActive(false);
            state.setIsDelete(true);
            state.setDeletedAt(new Date());
            state.setDeletedBy(loggedInUser.getId().toString());


            stateRepository.save(state);
        }else{
            throw new InvoiceManagementException(messageSource.getMessage("stateNotFound", null, new Locale("en")),
                    500);
        }

    }

}
