package com.biz4solutions.clientinvoice.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.dto.StateDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.StateRepository;
import com.biz4solutions.clientinvoice.requestWrapper.StateRequestWrapper;
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
import com.biz4solutions.clientinvoice.domain.State;
import com.biz4solutions.clientinvoice.service.StateService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/user/state")
public class StateController {

	private static final Logger LOGGER = Logger.getLogger(StateController.class);

	@Autowired
	private StateService stateService;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getStateById(@PathVariable("id") Long id,
													   @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													   @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	) throws InvoiceManagementException {

		LOGGER.info("get state by id Start");

		State state = stateService.getASingleState(id);
		String message = messageSource.getMessage("stateGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, state);

		LOGGER.info("get state by id end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value =  "/getAllState", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllState(@RequestParam Long id,
			                                 		 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
			                                     	 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	) throws InvoiceManagementException {

		LOGGER.info("get all state Start");

		List<StateDTO> stateDTO = stateService.getAllState(id);
		String message = messageSource.getMessage("allStateGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message,stateDTO);

		LOGGER.info("get all state end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/addState", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addState(@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
												   @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												   @RequestBody StateRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("add state start");

		stateService.createState(request,acceptLanguage);
		String message = messageSource.getMessage("StateCreatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("add state end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateState( @PathVariable Long id,
													  @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													  @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													  @RequestBody StateRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("update state Start");

		Optional<State> updateState = stateService.updateState(id, request);
		String message = messageSource.getMessage("stateUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, updateState);

		LOGGER.info("update state end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/delete/state", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> deleteState(@RequestParam Long id,
													 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {

		LOGGER.info("delete state Start");

		stateService.deleteState(id, acceptLanguage);
		String message = messageSource.getMessage("stateDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("delete state end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<JSONObject> processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex
//	) throws InvoiceManagementException {
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
//
//		return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//	}


}
