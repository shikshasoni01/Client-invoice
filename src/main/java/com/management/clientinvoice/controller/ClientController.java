package com.management.clientinvoice.controller;

import com.management.clientinvoice.constant.UrlConstant;
import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.dto.ClientDTO;
import com.management.clientinvoice.dto.ClientTypeDTO;
import com.management.clientinvoice.dto.PaginationDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.ClientRepository;
import com.management.clientinvoice.requestWrapper.ClientFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.ClientRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateClientRequestWrapper;
import com.management.clientinvoice.service.ICommonService;
import com.management.clientinvoice.util.ResponseFormatter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.management.clientinvoice.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;



@RestController
@RequestMapping(value = "/api/v1/user/client")
public class ClientController {

	private static final Logger LOGGER = Logger.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;


	@RequestMapping(value ="/addClient", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addClient(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													@RequestBody ClientRequestWrapper request
	) throws InvoiceManagementException {

		LOGGER.info("add client Start");

		clientService.createClient(request,acceptLanguage);
		String message = messageSource.getMessage("clientAddedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("add client end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getClientById(@PathVariable("id") Long id,
														@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
														@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	) throws InvoiceManagementException {

		LOGGER.info("get client by id  Start");

		ClientDTO client = clientService.getASingleClient(id);
		String message = messageSource.getMessage("clientGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, client);

		LOGGER.info("get client by id end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllClient", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllClient(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												  		 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("get all client Start");

		List<ClientDTO> clientDTO = clientService.getAllClient();
		String message = messageSource.getMessage("allClientGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, clientDTO);

		LOGGER.info("get all client end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = UrlConstant.UPDATE_OPERATION, method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateClient(@RequestBody UpdateClientRequestWrapper request,
														@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
														@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {

		LOGGER.info("update client Start");

		clientService.updateClient(request,acceptLanguage);
		String message = messageSource.getMessage("clientUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("update client end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/clientType/list", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> listClientTypeList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	) throws InvoiceManagementException {

		LOGGER.info("list of Client List status START");

		List<ClientTypeDTO> clientTypeDTOList = clientService.listClientType ();

		String message = commonService.getMessage("clientTypeSuccess");
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, clientTypeDTOList);

		LOGGER.info("list of Client Type END");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}




	@RequestMapping(value = "/delete/client", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> deleteClient(@RequestParam Long id,
												 	  @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												 	  @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {

		LOGGER.info("delete client Start");

		clientService.deleteClient(id, acceptLanguage);
		String message = messageSource.getMessage("clientDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("delete client end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}



	@RequestMapping(value = "/enable/client", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> enableClient(@RequestParam Long id,
												   		@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												   		@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {

		LOGGER.info("enable client Start");

		clientService.enableClient(id, acceptLanguage);
		String message = messageSource.getMessage("clientDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("enable client end");

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> getAllClientList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
														Pageable pageable,
															@RequestBody ClientFilterRequestWrapper request,
															@RequestParam(required = false, defaultValue = "ALL") String filter,
															@RequestParam(required = false, defaultValue = "FIRSTNAME") String sort, //NAME
															@RequestParam(required = false, defaultValue = "ASC") String order// ASC/DESC
	) throws InvoiceManagementException {

		LOGGER.info("filter client Start");

		PaginationDTO<ClientDTO> list = clientService.getAllClientList(pageable, request, filter, sort, order);
		String message = commonService.getMessage("listRetrievedSuccessfully");
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);

		LOGGER.info("filter client end");

		return new ResponseEntity<>(data, HttpStatus.OK);

	}
//
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
//
//		return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//	}

}