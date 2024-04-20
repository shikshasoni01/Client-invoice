package com.biz4solutions.clientinvoice.controller;

import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.domain.Country;
import com.biz4solutions.clientinvoice.dto.CountryDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.CountryRepository;
import com.biz4solutions.clientinvoice.requestWrapper.CountryRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.util.ResponseFormatter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.biz4solutions.clientinvoice.service.CountryService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/v1/user/country")
public class CountryController {

	private static final Logger LOGGER = Logger.getLogger(CountryController.class);

	@Autowired
	private CountryService countryService;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;


	@CrossOrigin(origins = "")

	@RequestMapping(value = "/getAllCountry", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllCountry(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
			                                        	@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("get all country Start");

		List<CountryDTO> countryDTO = countryService.getAllCountry();
		String message = messageSource.getMessage("allCountryGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message,countryDTO);

		LOGGER.info("get all country end line number 60"+data.toString());
		System.out.println("line number 61"+data.toString());
		System.out.println("**************************************************");



		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getCountryById(@PathVariable("id") Long id,
														 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
														 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("get country by id Start");

		Country country = countryService.getASingleCountry(id);
		String message = messageSource.getMessage("countryGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, country);

		LOGGER.info("get country by id end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateCountry( @PathVariable Long id,
														 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
														 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
														 @Valid @RequestBody CountryRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("update country Start");

		Optional<Country> updateCountry = countryService.updateCountry(id,request);
		String message = messageSource.getMessage("countryUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, updateCountry);

		LOGGER.info("update country end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/delete/country", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> deleteCountry(@RequestParam Long id,
													@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {

		LOGGER.info("delete country Start");

		countryService.deleteCountry(id, acceptLanguage);
		String message = messageSource.getMessage("countryDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("delete country end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/addCountry", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addCountry(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													 @Valid @RequestBody CountryRequestWrapper request
	) throws InvoiceManagementException, MessagingException {
		LOGGER.info("add country Start");

		countryService.createCountry(request,acceptLanguage);
		String message = messageSource.getMessage("CountryAddedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("add country end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<JSONObject> processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex
//	) throws  InvoiceManagementException {
//
//		BindingResult result = ex.getBindingResult();
//		String localizedErrorMessage = "";
//		String lang = req.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
//
//		if (lang == null || lang.isEmpty()) {
//			lang = "en";
//		}
//
//		if (!result.getAllErrors().isEmpty()) {
//			localizedErrorMessage = result.getAllErrors().get(0).getDefaultMessage();
//
//		}
//
//		localizedErrorMessage = messageSource.getMessage(localizedErrorMessage, null, new Locale(lang));
//		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, localizedErrorMessage);
//		return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//	}


}