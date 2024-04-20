package com.biz4solutions.clientinvoice.service.Impl;

import com.biz4solutions.clientinvoice.domain.Client;
import com.biz4solutions.clientinvoice.domain.Country;
import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.dto.CountryDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.CountryRepository;
import com.biz4solutions.clientinvoice.requestWrapper.CountryRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.biz4solutions.clientinvoice.service.CountryService;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.*;

@Service
public class CountryServiceImpl implements CountryService{


    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ICommonService commonService;


    @Override
    public List<CountryDTO> getAllCountry() {
        Iterable<Country> countrys = countryRepository.findAllByIsActive();

        List<CountryDTO> countryDTOs = new ArrayList<>();

        for(Country country : countrys) {
            CountryDTO countryDTO = new CountryDTO();

            countryDTO.setId(country.getId());
            countryDTO.setCountryName(country.getCountryName());
            countryDTO.setCountryCode(country.getCountryCode());
            countryDTO.setCurrencyName(country.getCurrencyName());
            countryDTO.setCurrencyCode(country.getCurrencyCode());

            countryDTOs.add(countryDTO);
        }
//		System.out.println(invoice.size());
        return countryDTOs;
    }

    @Transactional(readOnly = true)
    public Country getCountry(String id ) {
        Optional<Country> countryResponse =  countryRepository.findById(id);
        Country country = countryResponse.get();
        return country;
    }

    @Override
    public Country getASingleCountry(Long id) throws InvoiceManagementException {
        Country country = countryRepository.findOneByIdIgnoreIsActive(id);
        if(country != null) {
            return country;
        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("countryNotFound", null, new Locale("en")));

        }
    }

    @Override
    public Optional<Country> updateCountry(Long countryId,@Valid CountryRequestWrapper request) {
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        Optional<Country> country = countryRepository.findById(countryId);
        Country country1 = countryRepository.findByCountryNameIgnoreCase(request.getCountryName());

        if (country1 != null && country1.getIsActive().equals(false)) {
            country1.setIsActive(true);
            country1.setIsDelete(false);
            countryRepository.save(country1);
        }

        country.get().setUpdatedBy(loggedInUser.getId().toString());
        if(!country.isEmpty()) {
            if (!request.getCountryCode().isEmpty()) {
                country.get().setCountryCode(request.getCountryCode());
            } else {
                country.get().getCountryCode();
            }
            if (!request.getCountryName().isEmpty()) {
                country.get().setCountryName(request.getCountryName());
            } else {
                country.get().getCountryName();
            }
            if (!request.getCurrencyName().isEmpty()) {
                country.get().setCurrencyName(request.getCurrencyName());
            } else {
                country.get().getCurrencyName();
            }
            if (!request.getCurrencyCode().isEmpty()) {
                country.get().setCurrencyCode(request.getCurrencyCode());
            } else {
                country.get().getCurrencyCode();
            }
        }else if(country.isEmpty()){
            throw new InvoiceManagementException(
                    messageSource.getMessage("countryNotFound", null, new Locale("en")));
        }else if(country1 != null){
            throw new InvoiceManagementException(messageSource.getMessage("countryAlreadyExists", null, new Locale("en")),
                    400);
        }

        countryRepository.save(country.get());
        return country;
    }

    @Override
    public void deleteCountry(Long countryId, String acceptLanguage) throws InvoiceManagementException {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        Country country = countryRepository.findOneByIdIgnoreIsActive(countryId);
        if(country != null) {
            country.setIsActive(false);
            country.setIsDelete(true);
            country.setDeletedAt(new Date());
            country.setDeletedBy(loggedInUser.getId().toString());
            countryRepository.save(country);
        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("countryNotFound", null, new Locale("en")));

        }
    }

    @Override
    public void createCountry(@Valid CountryRequestWrapper request, String acceptLanguage) throws InvoiceManagementException, MessagingException {
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }

        Country newCountry = new Country();

        // email from 1and1 testing purpose

        //    sendEmail();

            Country country = countryRepository.findByCountryNameIgnoreCase(request.getCountryName());

            if (country != null && country.getIsActive().equals(false)) {
                    country.setIsActive(true);
                    country.setIsDelete(false);
                    countryRepository.save(country);
//                else if (country.getIsActive().equals(true)) {
//                    throw new InvoiceManagementException(messageSource.getMessage("countryAlreadyExists", null, new Locale(acceptLanguage)),
//                            500);
//                }
                }else if(request.getCountryName().isEmpty() && request.getCountryCode().isEmpty() && request.getCurrencyCode().isEmpty() && request.getCurrencyName().isEmpty()) {
                    throw new InvoiceManagementException(messageSource.getMessage("requiredFieldIsMissing", null, new Locale(acceptLanguage)),
                            400);
            }else if(country == null){
                    newCountry.setCountryName(request.getCountryName());
                    newCountry.setCountryCode(request.getCountryCode());
                    newCountry.setCurrencyName(request.getCurrencyName());
                    newCountry.setCurrencyCode(request.getCurrencyCode());
                    newCountry.setCreatedBy(loggedInUser.getId().toString());
                    countryRepository.save(newCountry);

            }else if((country.getIsActive().equals(true))) {
                throw new InvoiceManagementException(messageSource.getMessage("countryAlreadyExists", null, new Locale(acceptLanguage)),
                        400);
            }


    }

    private void sendEmail() throws MessagingException {
        String toAddress = "sagar.dalvi@biz4solutions.com";



        String subject = "Country is added for project";
        String content = " sagar,Client Invoice Team.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("client.invoice@biz4solutions.com");


        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("sagar", "sagar");
      //  String verifyURL = serverURL + "/api/v1/users/verify?code=" + user.getVerificationCode();

        content = content.replace("sagar", "sagar");

        helper.setText(content, true);

        mailSender.send(message);

    }

}
