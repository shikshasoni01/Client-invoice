package com.management.clientinvoice.controller;

import com.management.clientinvoice.constant.UrlConstant;
import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.dto.*;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.InvoiceRepository;
import com.management.clientinvoice.requestWrapper.InvoiceFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.InvoiceNumberUpdateRequestWrapper;
import com.management.clientinvoice.requestWrapper.InvoiceRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateInvoiceRequest;
import com.management.clientinvoice.service.ICommonService;
import com.management.clientinvoice.service.InvoiceService;
import com.management.clientinvoice.util.ResponseFormatter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping(value = "/api/v1/user/invoice")
public class InvoiceController {

	private static final Logger LOGGER = Logger.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;


	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addInvoice(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
						                         @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
						                         @RequestBody InvoiceRequestWrapper request
	) throws InvoiceManagementException, MessagingException {
		LOGGER.info("add invoice Start");

		 invoiceService.createInvoice(request,acceptLanguage);
		System.out.println(request);
		String message = messageSource.getMessage("invoiceCreatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("add invoice end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getInvoiceById(@PathVariable("id") Long invoiceId,
					                                 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
					                                 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage)
			throws InvoiceManagementException {
		LOGGER.info("get invoice by id Start");

		InvoiceDTO invoice = invoiceService.getInvoice(invoiceId);
		String message = messageSource.getMessage("invoiceGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, invoice);

		LOGGER.info("get invoice by id end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

//	@RequestMapping(method=RequestMethod.PUT, value="/add/{name}/{id}")
//	public void addCourse(@PathVariable("name") String name, @RequestBody String course) {
//		invoiceService.add(name ,course);
//	}
	@PreAuthorize("hasRole('ACCOUNT')")
	@RequestMapping(value = "/getAllInvoices", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllInvoices(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
		                                            @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {
		LOGGER.info("get all invoices Start");

		List<InvoiceDTO> invoiceDTOs = invoiceService.getAllInvoices();
		String message = messageSource.getMessage("allInvoiceGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, invoiceDTOs);

		LOGGER.info("get all invoices end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}



	@RequestMapping(value = UrlConstant.UPDATE_OPERATION, method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateInvoice(
													 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
													 @RequestBody UpdateInvoiceRequest request
	) throws InvoiceManagementException, MessagingException, FileNotFoundException {
		LOGGER.info("update invoice  Start");
		invoiceService.updateInvoice( request,acceptLanguage);
		String message = messageSource.getMessage("invoiceUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("update invoice end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	@RequestMapping(value = "/update/InvoiceNumber", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> updateInvoiceNumber(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
			@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
			@RequestParam Long id,
			@RequestBody InvoiceNumberUpdateRequestWrapper request
	) throws InvoiceManagementException, MessagingException{
		LOGGER.info("update invoice  Start");
		invoiceService.updateInvoiceNumber( id,request);
		String message = messageSource.getMessage("invoiceNumberUpdatedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("update invoice end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/invoice", method = RequestMethod.PUT)
	public ResponseEntity<JSONObject> deleteInvoice(@RequestParam Long id,
													@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													@RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException, MessagingException {

		LOGGER.info("delete invoice start");
		invoiceService.deleteInvoice(id, acceptLanguage);
		String message = messageSource.getMessage("invoiceDeletedSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("delete invoice end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/enable/invoice", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> enableInvoice(@RequestParam Long invoiceId,
												   @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
												   @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws  InvoiceManagementException {
		LOGGER.info("enable invoice Start");

		invoiceService.enableInvoice(invoiceId, acceptLanguage);
		String message = messageSource.getMessage("invoiceEnableSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

		LOGGER.info("enable invoice end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}




//	@PreAuthorize("hasRole('ACCOUNT')"+ "||"+ "hasRole('MANAGER')")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> getAllInvoicesList(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
			Pageable pageable,
			@RequestBody InvoiceFilterRequestWrapper request,
			@RequestParam(required = false, defaultValue = "ALL") String filter,
			@RequestParam(required = false, defaultValue = "INVOICEDATE") String sort, //NAME
			@RequestParam(required = false, defaultValue = "DESC") String order// ASC/DESC
	) throws InvoiceManagementException {

		LOGGER.info("list of invoices START");
		PaginationDTO<InvoiceDTO> list = invoiceService.getAllInvoicesList(pageable, request , filter, sort, order);
		String message = commonService.getMessage("listRetrievedSuccessfully");
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);
		LOGGER.info("list of invoices END");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

//	@RequestMapping(value = "/list", method = RequestMethod.POST)
//	public ResponseEntity<JSONObject> getAllInvoicesList(
//			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
//			Pageable pageable,
//			@RequestBody InvoiceFilterRequestWrapper request,
//			@RequestParam(required = false, defaultValue = "ALL") String filter,
//			@RequestParam(required = false, defaultValue = "ID") String sort, //NAME
//			@RequestParam(required = false, defaultValue = "ASC") String order// ASC/DESC
//	) throws InvoiceManagementException {
//
//		LOGGER.info("list of invoices START");
//		PaginationDTO<InvoiceDTO> list = invoiceService.getAllInvoicesList(pageable, request , filter, sort, order);
//		String message = commonService.getMessage("listRetrievedSuccessfully");
//		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);
//		LOGGER.info("list of invoices END");
//		return new ResponseEntity<>(data, HttpStatus.OK);
//	}

//	@RequestMapping(value = "/list", method = RequestMethod.POST)
//	public ResponseEntity<JSONObject> getAllInvoiceList(
//			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
//			Pageable pageable,
//			@RequestBody InvoiceFilterRequestWrapper request,
//			@RequestParam(required = false, defaultValue = "ALL") String filter,
//			@RequestParam(required = false, defaultValue = "NAME") String sort, //NAME
//			@RequestParam(required = false, defaultValue = "ASC") String order// ASC/DESC
//	) throws InvoiceManagementException {
//
//		PaginationDTO list = invoiceService.getAllInvoiceList(pageable, request , filter, sort, order);
//		String message = commonService.getMessage("listRetrievedSuccessfully");
//		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);
//		return new ResponseEntity<>(data, HttpStatus.OK);
//	}

	@RequestMapping(value = "/invoiceStatus/list", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> listInvoiceStatus(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) throws InvoiceManagementException {
		LOGGER.info("list of invoice status START");
		List<PaymentStatusDTO> paymentStatusList = invoiceService.listInvoiceStatus();

		String message = commonService.getMessage("invoiceStatusSuccess");
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, paymentStatusList);
		LOGGER.info("StoreController.listPaymentModes() END");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public ResponseEntity<JSONObject> getAllInvoiceList(
//			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
//			Pageable pageable,
//			@RequestParam(required = false, defaultValue = "") String searchText,
//			@RequestParam(required = false, defaultValue = "ALL") String filter,
//			@RequestParam(required = false, defaultValue = "NAME") String sort, //NAME
//			@RequestParam(required = false, defaultValue = "ASC") String order,// ASC/DESC
//			@RequestParam(required = false, defaultValue = "yyyy-MM-dd" ) String date1,
//			@RequestParam(required = false, defaultValue = "yyyy-MM-dd" ) String date2,
//			@RequestParam(required = false,defaultValue = "") Float amount
//	) throws InvoiceManagementException {
//		LOGGER.info("filter invoice Start");
//
//		PaginationDTO list = invoiceService.getAllInvoiceList(pageable, searchText, filter, sort, order,date1,date2,amount);
//		String message = commonService.getMessage("listRetrievedSuccessfully");
//		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);
//		LOGGER.info("filter invoice end");
//		return new ResponseEntity<>(data, HttpStatus.OK);
//
//	}

	@RequestMapping(value = "/getAllCompanies", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllCompany(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {
		LOGGER.info("get all company Start");

		List<CompanyDTO> companyDTOS = invoiceService.getAllCompany ();
		String message = messageSource.getMessage("allCompanyGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, companyDTOS);

		LOGGER.info("get all company end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	@RequestMapping(value = "/getAllMasterData", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllMasterData(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {
		LOGGER.info("get all master data Start");

		List<MasterDataDTO> masterDataDTOS = invoiceService.getAllMasterDatas ();
		String message = messageSource.getMessage("allMasterDataGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, masterDataDTOS);

		LOGGER.info("get all master data end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	@RequestMapping(value = "/getAllBanks", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getAllBank(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
													 @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
	) throws InvoiceManagementException {
		LOGGER.info("get all banks Start");

		List<BankDTO> bankDTOS = invoiceService.getAllBanks ();
		String message = messageSource.getMessage("allBankGetSuccess", null, new Locale(acceptLanguage));
		JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, bankDTOS);

		LOGGER.info("get all banks end");
		return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "/api/v1/invoice/download", method = RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
	})
	public ResponseEntity<Resource> downloadTicket(@RequestParam(value="invoiceId") Long invoiceId, @RequestParam(value="invoiceNumber")String invoiceNumber,
												   @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage) throws IOException {

		File file = invoiceService.getInvoice(invoiceId,invoiceNumber,acceptLanguage);

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+invoiceNumber+".pdf");
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		return ResponseEntity.ok()
				.headers(header)
				.contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/pdf"))
				.body(resource);
	}





}



