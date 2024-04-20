package com.biz4solutions.clientinvoice.controller;


import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.domain.City;
import com.biz4solutions.clientinvoice.dto.CityDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.CityRepository;
import com.biz4solutions.clientinvoice.requestWrapper.CityRequestWrapper;
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
import com.biz4solutions.clientinvoice.service.CityService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/user/city")
public class CityController {

	private static final Logger LOGGER = Logger.getLogger(CityController.class);

	@Autowired
	private CityService cityService;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/addCity", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addCity(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
												 @RequestBody CityRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("add city Start");

	    cityService.createCity(request,acceptLanguage);
		String message = messageSource.getMessage("CityAddedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("add city end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getCityById(@PathVariable("id") Long id,
													 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("get city by id Start");

		City city = cityService.getASingleCity(id);
		String message = messageSource.getMessage("cityGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, city);

		LOGGER.info("get city by id end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllCity", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllCity(@RequestParam Long id,
												  	@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("get all city Start");

		List<CityDTO> cityDTO = cityService.getAllCity(id);
		String message = messageSource.getMessage("allCityGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, cityDTO);

		LOGGER.info("get all city end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateCity( @PathVariable Long id,
													 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													 @RequestBody CityRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("update city Start");

		Optional<City> updateCity = cityService.updateCity(id, request);
		String message = messageSource.getMessage("cityUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, updateCity);

		LOGGER.info("update city end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/delete/city", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> deleteCity(@RequestParam Long id,
													@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {

		LOGGER.info("delete city Start");

		cityService.deleteCity(id, acceptLanguage);
		String message = messageSource.getMessage("cityDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("delete city end");

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
//		}
//
//		localizedErrorMessage = messageSource.getMessage(localizedErrorMessage, null, new Locale(lang));
//		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, localizedErrorMessage);
//
//		return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//	}

}
