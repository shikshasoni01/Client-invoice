package com.management.clientinvoice.service.Impl;

import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.dao.InvoiceDAO;
import com.management.clientinvoice.domain.*;
import com.management.clientinvoice.dto.*;
import com.management.clientinvoice.enumerator.RoleType;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.*;
import com.management.clientinvoice.requestWrapper.*;
import com.management.clientinvoice.service.EmailService;
import com.management.clientinvoice.service.ICommonService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.management.clientinvoice.service.InvoiceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;


@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private UpdateHistoryRepository updateHistoryRepository;
	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private InvoiceDAO invoiceDAO;

	@Autowired
    private ICommonService commonService;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PaymentTermRepository paymentTermRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private UserIdentityRepository userIdentityRepository;

	@Autowired
	private PaymentStatusRepository paymentStatusRepository;

	@Autowired
	private PaymentModeRepository paymentModeRepository;

	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	@Autowired
	private BankRepository bankRepository;
	@Autowired
	private MasterDataRepository masterDataRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private MailContentBuilder mailContentBuilder;

	@Autowired
	private InvoiceSequenceNumberRepository invoiceSequenceNumberRepository;

	@Autowired
	private ClientTypeRepository clientTypeRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private  RoleRepository roleRepository;

	@Autowired
	private EmailService emailService;

	private static final DecimalFormat df = new DecimalFormat("0.00");


	/**
	 *
	 * @param request
	 * @return
	 */

	@Override
	public void createInvoice(InvoiceRequestWrapper request, String acceptLanguage) throws MessagingException {

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		Invoice newInvoice = new Invoice();
		newInvoice.setInvoiceDate(request.getInvoiceDate());
		newInvoice.setInvoiceStartDate(request.getInvoiceStartDate());
		System.out.println("date "+ request.getInvoiceStartDate());
		newInvoice.setInvoiceEndDate(request.getInvoiceEndDate());
		newInvoice.setInvoiceDescription (request.getInvoiceDescription ());
		newInvoice.setShippingMethod (request.getShippingMethod ());
		newInvoice.setShippingTerm (request.getShippingTerm ());
		newInvoice.setInvoiceSummary(request.getInvoiceSummary());
		newInvoice.setPaidDate(request.getPaidDate());
		newInvoice.setDueDate(request.getDueDate());
		newInvoice.setIsActive ( request.getIsActive () );
		newInvoice.setNote ( request.getNote () );
		newInvoice.setCreatedBy(loggedInUser.getId().toString());
		newInvoice.setSgst( request.getSgst());
		newInvoice.setCgst( request.getCgst());
		newInvoice.setGrandTotal( request.getGrandTotal());
		newInvoice.setInvoiceTotalAmount ((float) (Math.round(request.getInvoiceTotalAmount ( )*100.0)/100.0));


		Optional<PaymentMode> paymentMode = paymentModeRepository.findById(request.getPaymentMode ());
		if (paymentMode.isPresent()) {
			newInvoice.setPaymentMode ( paymentMode.get () );
		} else {
			throw new InvoiceManagementException(
					messageSource.getMessage("paymentModeIdNotFound", null, new Locale(acceptLanguage)), 400);
		}

		Optional<Country> country = countryRepository.findById(request.getCountryId ());
		if(country.isPresent()) {
			newInvoice.setCountry(country.get());
		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("countryIdNotFound", null, new Locale (acceptLanguage) )
					, 500);
		}

		Projects projects = projectRepository.findById (request.getProjectId ());
		if(projects == null){
			throw new InvoiceManagementException(messageSource.getMessage("projectIdNotFound", null, new Locale(acceptLanguage)),
					500);
		}
		newInvoice.setProject (projects);

		Optional<PaymentTerm> paymentTerm = paymentTermRepository.findById(request.getPaymentTerm ());
		if(paymentTerm.isPresent()) {
			newInvoice.setPaymentTerm (paymentTerm.get ());
		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("paymentTermIdNotFound", null, new Locale(acceptLanguage)), 400);
		}

		Optional<PaymentStatus> paymentStatus = paymentStatusRepository.findById(request.getPaymentStatus ());
		if(paymentStatus.isPresent()) {
			newInvoice.setPaymentStatus (paymentStatus.get ());
		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("paymentStatusIdNotFound", null, new Locale(acceptLanguage)), 400);
		}

		ClientType clientType = clientTypeRepository.findOneById(projects.getClient().getClientType().getId());

		Integer draftForProjectCount = invoiceRepository.findInvoiceForSpecificProjectAndDraftPaymentStatus(paymentStatus.get().getId().intValue(), projects.getId().intValue());

//		String locationCity = projects.getLocation().getCity();
		String locationCity =null;
		if(clientType.getDataType().equals("D")) {
			locationCity = newInvoice.getProject().getLocation().getCity();
		}
		if(draftForProjectCount ==0){
			newInvoice.setInvoiceNumber(getFormattedInvoiceNumber(clientType, request.getPaymentStatus().toString(),locationCity) );
		}else if(paymentStatus.get().getId().toString().equals("2")){
			newInvoice.setInvoiceNumber(getFormattedInvoiceNumber(clientType, "6",locationCity));
			emailService.sendInvoiceCreateMailPending(newInvoice.getProject().getUserIdentity().getEmail(),newInvoice.getProject().getUserIdentity().getFirstName(),newInvoice.getProject().getProjectName(),newInvoice.getProject().getClient().getFirstName(),newInvoice.getInvoiceNumber());
		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("draftInvoiceForProjectAlreadyPresent", null, new Locale(acceptLanguage)), 500);
		}
		newInvoice= invoiceRepository.save(newInvoice);

		saveInvoiceItems(  request.getInvoiceItems ( ), newInvoice );

	}


	private String getFormattedInvoiceNumber(ClientType clientType, String paymentStatus,String locationCity) {
		StringBuffer clientType1 = new StringBuffer("0");
		if (clientType.getDataType()!= null) {
			clientType1 = new StringBuffer(String.format(clientType.getDataType()));
		}

		LocalDate current_date = LocalDate.now();
		System.out.println("Current date: "+current_date);
		//getting the current year from the current_date
		 int current_Year = current_date.getYear();

		 if(clientType.getDataType().equals("L") && !paymentStatus.equals("6")) {
			 return clientType1.toString() + "" + invoiceRepository.findLLCInvoiceSequenceNumber() + "/" + current_Year;
		 }else if(clientType.getDataType().equals("D") && !paymentStatus.equals("6")){
			 if(locationCity.equals("Indore")){
				return clientType1 + invoiceRepository.findDomesticInvoiceSequenceNumberForIndoreLocation()+ "/IND/" + (current_Year) % 2000 + "-" + (current_Year + 1) % 2000;
			 }else {
				 return clientType1.toString() + "" + invoiceRepository.findDomesticInvoiceSequenceNumber() + "/" + (current_Year) % 2000 + "-" + (current_Year + 1) % 2000;
			 }
		 }else if(clientType.getDataType().equals("I") && !paymentStatus.equals("6")){
			 return clientType1.toString() + "" + invoiceRepository.findInternationalInvoiceSequenceNumber()+ "/" + (current_Year) % 2000 + "-" + (current_Year + 1) % 2000;
		 }else if(paymentStatus.equals("6")){
			 return "Draft-"+clientType1 +""+ invoiceRepository.findCommonDraftInvoiceSequenceNumber();
		 }

		 return null;
	}



	public String tentativeInvoiceNumber(ClientType clientType, String paymentStatus, Boolean readyToBilledToClient) {

		StringBuffer clientType1 = new StringBuffer("0");
		if (clientType.getDataType()!= null) {
			clientType1 = new StringBuffer(String.format(clientType.getDataType()));
		}

		LocalDate current_date = LocalDate.now();
		System.out.println("Current date: "+current_date);
		//getting the current year from the current_date
		int current_Year = current_date.getYear();

		if(clientType.getDataType().equals("L") && !paymentStatus.equals("6")) {

			Integer val= invoiceRepository.findLastValueOfLLCSequence()+1;
			return clientType1 + "" + val + "/" + current_Year+" (Provisional)";
		}else if(clientType.getDataType().equals("D") && !paymentStatus.equals("6")){

			Integer val= invoiceRepository.findLastValueOfDomesticSequence()+1;
			return clientType1 + "" + val+ "/" + (current_Year) % 2000 + "-" + (current_Year + 1) % 2000+" (Provisional)";
		}else if(clientType.getDataType().equals("I") && !paymentStatus.equals("6")){

			Integer val= invoiceRepository.findLastValueOfInternationalSequence()+1;
			return clientType1 + "" +val+ "/" + (current_Year) % 2000 + "-" + (current_Year + 1) % 2000+" (Provisional)";
		}else if(paymentStatus.equals("6")){
			return "Draft-"+clientType1 +""+ invoiceRepository.findCommonDraftInvoiceSequenceNumber();
		}
		return null;
	}

	private void saveInvoiceItems(List<InvoiceItemRequestWrapper> requestWrappers, Invoice newInvoice) {

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}
		for (InvoiceItemRequestWrapper invoiceItem:requestWrappers) {
			InvoiceItem invoiceItem1 = new InvoiceItem ();
			invoiceItem1.setItemNumber ( invoiceItem.getItemNumber ( ) );
			invoiceItem1.setInvoice (newInvoice);
			invoiceItem1.setItemAmount (invoiceItem.getItemAmount ());
			invoiceItem1.setItemDescription ( invoiceItem.getItemDescription ( ) );
			invoiceItem1.setSacCode ( invoiceItem.getSacCode () );
			invoiceItem1.setQuantity ( invoiceItem.getQuantity () );
			invoiceItem1.setHourlyRate (invoiceItem.getHourlyRate ());
			invoiceItem1.setHours (invoiceItem.getHours ());
			System.out.println("for create "+invoiceItem.getHours());
			System.out.println("for create set "+invoiceItem1.getHours());
			invoiceItem1.setIsActive ( invoiceItem.getIsActive () );
			invoiceItem1.setCreatedBy(loggedInUser.getId().toString());


			invoiceItemRepository.save ( invoiceItem1 );

		}

	}


	/**
	 *
	 * @return
	 */

	@Override
	public List<InvoiceDTO> getAllInvoices() {

		List<Invoice> invoices = invoiceRepository.findAllByIsActive();
		List<InvoiceDTO> invoiceDTOs = new ArrayList<>();


        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }


        for(Invoice invoice : invoices) {

			InvoiceDTO invoiceDTO = new InvoiceDTO ( );

			invoiceDTO.setId ( invoice.getId ( ) );
			invoiceDTO.setInvoiceStartDate ( invoice.getInvoiceStartDate ( ) );
			invoiceDTO.setInvoiceEndDate ( invoice.getInvoiceEndDate ( ) );
			invoiceDTO.setInvoiceDate ( invoice.getInvoiceDate ( ) );
			invoiceDTO.setInvoiceDescription ( invoice.getInvoiceDescription ( ) );
			invoiceDTO.setInvoiceSummary ( invoice.getInvoiceSummary ( ) );
			invoiceDTO.setProjectName ( invoice.getProject ( ).getProjectName ( ) );
			invoiceDTO.setProjectId ( invoice.getProject ( ).getId ( ) );
			invoiceDTO.setInvoiceTotalAmount (currencyConversion(invoice.getInvoiceTotalAmount(),invoice.getCountry().getCurrencyCode()));
			invoiceDTO.setPaidDate ( invoice.getPaidDate ( ) );
			invoiceDTO.setDueDate ( invoice.getDueDate ( ) );
			invoiceDTO.setUpdatedOn ( invoice.getUpdatedOn ( ) );
			invoiceDTO.setCountryId ( invoice.getCountry ( ).getId ( ) );
			invoiceDTO.setCountryName ( invoice.getCountry ( ).getCountryName ( ) );
			invoiceDTO.setCurrencyCode ( invoice.getCountry ( ).getCurrencyCode ( ) );
			invoiceDTO.setCurrencyName ( invoice.getCountry ( ).getCurrencyName ( ) );
			invoiceDTO.setClientId ( invoice.getProject ( ).getClient ( ).getId ( ) );
			invoiceDTO.setClientFirstName ( invoice.getProject ( ).getClient ( ).getFirstName ( ) );
			invoiceDTO.setClientLastName ( invoice.getProject ( ).getClient ( ).getLastName ( ) );
			invoiceDTO.setUpdatedOn ( invoice.getUpdatedOn ( ) );
			invoiceDTO.setIsActive ( invoice.getIsActive ( ) );
			invoiceDTO.setNote ( invoice.getNote ( ) );
			invoiceDTO.setShippingTerm ( invoice.getShippingTerm ( ) );
			invoiceDTO.setShippingMethod ( invoice.getShippingMethod ( ) );
			invoiceDTO.setUpdatedBy ( loggedInUser.getEmail ( ) );
			invoiceDTO.setPaymentStatus ( invoice.getPaymentStatus ( ).getValue ( ) );
			invoiceDTO.setPaymentStatusId ( invoice.getPaymentStatus ( ).getId ( ) );
			invoiceDTO.setPaymentTerm ( invoice.getPaymentTerm ( ).getValue ( ) );
			invoiceDTO.setPaymentTermId ( invoice.getPaymentTerm ( ).getId ( ) );
			invoiceDTO.setPaymentMode ( invoice.getPaymentMode ( ).getValue ( ) );
			invoiceDTO.setPaymentModeId ( invoice.getPaymentMode ( ).getId ( ) );
			invoiceDTO.setSgst(invoice.getSgst());
			invoiceDTO.setCgst(invoice.getCgst());
			invoiceDTO.setGrandTotal(currencyConversion(invoice.getGrandTotal(), invoice.getCountry().getCurrencyCode()));
			invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());

			List<InvoiceItem>invoiceItemList= invoiceItemRepository.findAllByInvoice (invoice);
			List<InvoiceItemDTO> invoiceItemDTOS = new ArrayList <> ();
			for (InvoiceItem invoiceItem: invoiceItemList){

				InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO ( );
				invoiceItemDTO.setId ( invoiceItem.getId () );
				invoiceItemDTO.setItemAmount (currencyConversion( invoiceItem.getItemAmount (),invoice.getCountry().getCurrencyCode()) );
				invoiceItemDTO.setItemDescription ( invoiceItem.getItemDescription () );
				invoiceItemDTO.setItemNumber ( invoiceItem.getItemNumber ( ) );
				invoiceItemDTO.setHours ( getInvoiceAmountInDecimal(invoiceItem.getHours ( )) );
				invoiceItemDTO.setHourlyRate ( currencyConversion(invoiceItem.getHourlyRate ( ),invoice.getCountry().getCurrencyCode()) );
				invoiceItemDTO.setQuantity ( invoiceItem.getQuantity () );
				invoiceItemDTO.setIsActive ( invoiceItem.getIsActive () );
				invoiceItemDTO.setSacCode ( invoiceItem.getSacCode () );

				invoiceItemDTOS.add ( invoiceItemDTO );
				invoiceDTO.setInvoiceItems ( invoiceItemDTOS);


			}
			invoiceDTOs.add ( invoiceDTO );


		}

		return invoiceDTOs;
	}




	/**
	 *
	 * @param request
	 * @return
	 */
	@Transactional(noRollbackFor = { org.springframework.mail.MailException.class })
	@Override
	public void updateInvoice(UpdateInvoiceRequest request, String acceptLanguage) throws MessagingException, FileNotFoundException {
			Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive (request.getId ());

		String oldInvoiceStatus= invoice.getPaymentStatus().getId().toString();

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		if(loggedInUser.getRole().getRoleType().equals(RoleType.ACCOUNT) || loggedInUser.getRole().getRoleType().equals(RoleType.ADMIN) || invoice.getProject().getCreatedBy().equals(loggedInUser.getId().toString()))
		{
			if (invoice != null)
			{
				invoice.setInvoiceDate(request.getInvoiceDate());
				invoice.setInvoiceStartDate(request.getInvoiceStartDate());
				invoice.setInvoiceEndDate(request.getInvoiceEndDate());
				invoice.setInvoiceDescription(request.getInvoiceDescription());
				invoice.setShippingMethod(request.getShippingMethod());
				invoice.setShippingTerm(request.getShippingTerm());
				invoice.setInvoiceSummary(request.getInvoiceSummary());
				invoice.setPaidDate(request.getPaidDate());
				invoice.setInvoiceTotalAmount(request.getInvoiceTotalAmount());
				invoice.setDueDate(request.getDueDate());
				invoice.setIsActive(request.getIsActive());
				invoice.setIsDelete(!request.getIsActive());
				invoice.setNote(request.getNote());
				invoice.setSgst(request.getSgst());
				invoice.setCgst(request.getCgst());
				invoice.setGrandTotal(request.getGrandTotal());
				invoice.setUpdatedBy(loggedInUser.getId().toString());
				invoice.setUpdateBy(loggedInUser);

				invoice.setInvoiceTotalAmount(request.getInvoiceTotalAmount());


				Optional<PaymentMode> paymentMode = paymentModeRepository.findById(request.getPaymentMode());
				if (paymentMode.isPresent())
				{
					invoice.setPaymentMode(paymentMode.get());
				}
				else
				{
					throw new InvoiceManagementException(
							messageSource.getMessage("paymentModeNotFound", null, new Locale(acceptLanguage)), 400);
				}


				Optional<Country> country = countryRepository.findById(request.getCountryId());
				if (country.isPresent())
				{
					invoice.setCountry(country.get());
				}
				else
				{
					throw new InvoiceManagementException(
							messageSource.getMessage("countryNotFound", null, new Locale(acceptLanguage)), 500);
				}


				Projects projects = projectRepository.findById(request.getProjectId());
				if (projects == null)
				{
					throw new InvoiceManagementException(messageSource.getMessage("projectNotFound", null, new Locale(acceptLanguage)),
							500);
				}
				invoice.setProject(projects);

				Optional<PaymentTerm> paymentTerm = paymentTermRepository.findById(request.getPaymentTerm());
				if (paymentTerm.isPresent())
				{
					invoice.setPaymentTerm(paymentTerm.get());
				}
				else
				{
					throw new InvoiceManagementException(
							messageSource.getMessage("paymentTermNotFound", null, new Locale(acceptLanguage)), 400);
				}

				Optional<PaymentStatus> paymentStatus = paymentStatusRepository.findById(request.getPaymentStatus());
				if (paymentStatus.isPresent())
				{
					invoice.setPaymentStatus(paymentStatus.get());
				}
				else
				{
					throw new InvoiceManagementException(
							messageSource.getMessage("paymentStatusNotFound", null, new Locale(acceptLanguage)), 400);
				}


			}
			else
			{
				throw new InvoiceManagementException(
						messageSource.getMessage("invoiceNotFound", null, new Locale(acceptLanguage)), 400);
			}

			ClientType clientType = clientTypeRepository.findOneById(invoice.getProject().getClient().getClientType().getId());


			//for invoice number generation permanent
			if (request.getPaymentStatus().toString().equals("1"))
			{
					String locationCity =null;
					if(clientType.getDataType().equals("D"))
					{
						 locationCity = invoice.getProject().getLocation().getCity();
					}
					invoice.setInvoiceNumber(getFormattedInvoiceNumber(clientType, "1", locationCity));
			}


			//for invoice number generation tentative that is tick on checkbox
			if(request.getReadyToBilledToClient().equals(true) && request.getPaymentStatus().toString().equals("2"))
			{
				invoice.setInvoiceNumber(tentativeInvoiceNumber(clientType,"1",true));
				File file = invoiceService.getInvoice(invoice.getId(),invoice.getInvoiceNumber(),"en");
				System.out.println("/////////////////////////*/*/"+invoice.getInvoiceNumber());
				emailService.sendReadyToBilledTOClientInvoice(invoice.getProject().getUserIdentity().getEmail(), file, invoice.getProject().getUserIdentity().getFirstName(), invoice.getInvoiceNumber(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getPaymentStatus().getId().toString());
			}

			invoice = invoiceRepository.save(invoice);
			if(request.getInvoiceItems() != null)
			{
				invoiceItemRepository.deleteAllByInvoice(invoice);
				saveInvoiceItems(request.getInvoiceItems(), invoice);
			}

			if(request.getReadyToBilledToClient().equals(false))
			{
		    	if (request.getPaymentStatus().toString().equals("2") && !request.getPaymentStatus().toString().equals(oldInvoiceStatus))
				{
			 		 emailService.sendInvoiceCreateMailPending(invoice.getProject().getUserIdentity().getEmail(), invoice.getProject().getUserIdentity().getFirstName(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getInvoiceNumber());
		  		}
				else if (request.getPaymentStatus().toString().equals("1") && !request.getPaymentStatus().toString().equals(oldInvoiceStatus))
				{
			  		emailService.sendInvoiceCreateMailBilledToClient(invoice.getProject().getUserIdentity().getEmail(), invoice.getProject().getUserIdentity().getFirstName(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getInvoiceNumber(), request.getPaymentStatus().toString());
				}
				else if (request.getPaymentStatus().toString().equals("3") || request.getPaymentStatus().toString().equals("4") && !request.getPaymentStatus().toString().equals(oldInvoiceStatus))
				{
					String paymentStatus = null;
			  		if (request.getPaymentStatus().toString().equals("3"))
			  		{
				  		paymentStatus = "full";
			  		}
			  		else
			  		{
				  		paymentStatus = "partial";
			  		}
			  		emailService.sendInvoiceCreateMailPartiallyOrFullyPaid(paymentStatus, invoice.getProject().getUserIdentity().getEmail(), invoice.getProject().getUserIdentity().getFirstName(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getInvoiceNumber());
				}
				else if (request.getPaymentStatus().toString().equals("5"))
				{
				  if (oldInvoiceStatus.equals("1") || oldInvoiceStatus.equals("3") || oldInvoiceStatus.equals("4") )
				  {
				  	emailService.sendInvoiceCreateMailVoidStatus(invoice.getProject().getUserIdentity().getEmail(), invoice.getProject().getUserIdentity().getFirstName(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getInvoiceNumber(), request.getPaymentStatus().toString());
				  }
				  else if (oldInvoiceStatus.equals("2") || oldInvoiceStatus.equals("6"))
				  {
				  	//when status pending or draft and in request it is void then hard delete invoice should perform
				  	emailService.sendInvoiceCreateMailVoidStatus(invoice.getProject().getUserIdentity().getEmail(), invoice.getProject().getUserIdentity().getFirstName(), invoice.getProject().getProjectName(), invoice.getProject().getClient().getFirstName(), invoice.getInvoiceNumber(), request.getPaymentStatus().toString());
				  	invoiceItemRepository.deleteInvoiceItemForInvoiceId(request.getId());
				  	Integer count = invoiceItemRepository.findAllByInvoiceCount(request.getId());
				  	if (count == 0)
				  	{
					  	invoiceRepository.deleteInvoiceById(request.getId());
				  	}
				  	else
				  	{
					  	throw new InvoiceManagementException(
							  messageSource.getMessage("invoiceWithInvoiceItemPresent", null, new Locale(acceptLanguage)), 400);
				  	}
				  }
				}
	  		}
		}
		else
		{
			throw new InvoiceManagementException(
					messageSource.getMessage("permissionDenied", null, new Locale(acceptLanguage)), 400);
		}

	}

	@Override
	public void updateInvoiceNumber(Long id, InvoiceNumberUpdateRequestWrapper requestWrapper) throws InvoiceManagementException, MessagingException
	{
		Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive (id);

		Integer count=0;
		
		count = updateHistoryRepository.findAllInvoiceNumberIgnoreIsActive(requestWrapper.getInvoiceNumber());

		UpdateHistory updateHistory=new UpdateHistory();

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null)
		{
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		if(loggedInUser.getRole().getRoleType().equals(RoleType.MANAGER) || loggedInUser.getRole().getRoleType().equals(RoleType.ADMIN) )
		{
			if (invoice != null)
			{
				if((!(invoice.getInvoiceNumber().equals(requestWrapper.getInvoiceNumber())))&&count<1)
				{
					updateHistory.setPreviousInvoiceNumber(invoice.getInvoiceNumber());
					invoice.setInvoiceNumber(requestWrapper.getInvoiceNumber());
					updateHistory.setUpdatedInvoiceNumber(invoice.getInvoiceNumber());
					updateHistory.setInvoiceId(invoice.getId());
					updateHistory.setUserId(loggedInUser.getId());
					updateHistory.setUpdatedOn(new Date());
					File file = invoiceService.getInvoice(invoice.getId(),invoice.getInvoiceNumber(),WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
					emailService.sendUpdateInvoiceNumberCreateMail(
							invoice.getProject().getUserIdentity().getEmail(),file, invoice.getProject().getProjectName(),
							invoice.getProject().getClient().getFirstName()+" "+invoice.getProject().getClient().getLastName(),updateHistory.getPreviousInvoiceNumber(),invoice.getInvoiceNumber());

				}
				else
				{
					throw new InvoiceManagementException(
							messageSource.getMessage("invoiceNumberAlreadyExist", null, new Locale(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE)), 400);
				}
			}
			else {
				throw new InvoiceManagementException(
						messageSource.getMessage("invoiceNotFound", null, new Locale(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE)), 400);
			}
		}
		else
		{
			throw new InvoiceManagementException(
					messageSource.getMessage("permissionDenied", null, new Locale(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE)), 400);
		}
		invoiceRepository.save(invoice);
		updateHistoryRepository.save(updateHistory);
	}

	@Override
	public InvoiceDTO getInvoice(Long invoiceId) throws ResourceNotFoundException {
		Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive(invoiceId);

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		if(invoice == null) {
			throw new InvoiceManagementException (
					messageSource.getMessage ( "invoiceNotFound", null, new Locale ( "en" ) ), 400 );

		}

			InvoiceDTO invoiceDTO = new InvoiceDTO ( );

			invoiceDTO.setId ( invoice.getId ( ) );
			invoiceDTO.setInvoiceStartDate ( invoice.getInvoiceStartDate ( ) );
			invoiceDTO.setInvoiceEndDate ( invoice.getInvoiceEndDate ( ) );
			invoiceDTO.setInvoiceDate ( invoice.getInvoiceDate ( ) );
			invoiceDTO.setInvoiceDescription ( invoice.getInvoiceDescription ( ) );
			invoiceDTO.setInvoiceSummary ( invoice.getInvoiceSummary ( ) );
			invoiceDTO.setProjectName ( invoice.getProject ( ).getProjectName ( ) );
			invoiceDTO.setProjectId ( invoice.getProject ( ).getId ( ) );
		if(invoice.getCountry().getCurrencyCode().equals("INR")) {
			invoiceDTO.setInvoiceTotalAmount("Rs."+" "+currencyConversion(invoice.getInvoiceTotalAmount(), invoice.getCountry().getCurrencyCode()));
		}else{
			invoiceDTO.setInvoiceTotalAmount(currencyConversion(invoice.getInvoiceTotalAmount(), invoice.getCountry().getCurrencyCode()));
		}
			invoiceDTO.setPaidDate ( invoice.getPaidDate ( ) );
			invoiceDTO.setDueDate ( invoice.getDueDate ( ) );
			invoiceDTO.setUpdatedOn ( invoice.getUpdatedOn ( ) );
			invoiceDTO.setCountryId ( invoice.getCountry ( ).getId ( ) );
			invoiceDTO.setCountryName ( invoice.getCountry ( ).getCountryName ( ) );
			invoiceDTO.setCurrencyCode ( invoice.getCountry ( ).getCurrencyCode ( ) );
			invoiceDTO.setCurrencyName ( invoice.getCountry ( ).getCurrencyName ( ) );
			invoiceDTO.setClientId ( invoice.getProject ( ).getClient ( ).getId ( ) );
			invoiceDTO.setClientFirstName ( invoice.getProject ( ).getClient ( ).getFirstName ( ) );
			invoiceDTO.setClientLastName ( invoice.getProject ( ).getClient ( ).getLastName ( ) );
			invoiceDTO.setUpdatedOn ( invoice.getUpdatedOn ( ) );
			invoiceDTO.setIsActive ( invoice.getIsActive ( ) );
			invoiceDTO.setNote ( invoice.getNote ( ) );
			invoiceDTO.setShippingTerm ( invoice.getShippingTerm ( ) );
			invoiceDTO.setShippingMethod ( invoice.getShippingMethod ( ) );
			invoiceDTO.setUpdatedBy ( loggedInUser.getEmail ( ) );
			invoiceDTO.setPaymentStatus ( invoice.getPaymentStatus ( ).getValue ( ) );
			invoiceDTO.setPaymentStatusId ( invoice.getPaymentStatus ( ).getId ( ) );
			invoiceDTO.setPaymentTerm ( invoice.getPaymentTerm ( ).getValue ( ) );
			invoiceDTO.setPaymentTermId ( invoice.getPaymentTerm ( ).getId ( ) );
			invoiceDTO.setPaymentMode ( invoice.getPaymentMode ( ).getValue ( ) );
			invoiceDTO.setPaymentModeId ( invoice.getPaymentMode ( ).getId ( ) );
			invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
//		    invoiceDTO.setReadyToBilledToClient(true);
			invoiceDTO.setRefDraftInvoiceNumber(invoice.getInvoiceNumber());

		Double grandTotal = 0.00;
		Double total = 0.00;
		Double cgst = 0.00;
		Double sgst = 0.00;

			List<InvoiceItem> invoiceItemList= invoiceItemRepository.findAllByInvoice (invoice );
			List<InvoiceItemDTO> invoiceItemDTOS = new ArrayList <> ();

				for (InvoiceItem invoiceItem : invoiceItemList) {

					InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO ( );
					invoiceItemDTO.setId ( invoiceItem.getId ( ) );
					invoiceItemDTO.setItemAmount (""+invoiceItem.getItemAmount());
					invoiceItemDTO.setItemDescription ( invoiceItem.getItemDescription ( ) );
					invoiceItemDTO.setItemNumber ( invoiceItem.getItemNumber ( ) );
					invoiceItemDTO.setHours ( getInvoiceAmountInDecimal(invoiceItem.getHours ( )) );
					invoiceItemDTO.setHourlyRate (""+invoiceItem.getHourlyRate());
					invoiceItemDTO.setQuantity ( invoiceItem.getQuantity ( ) );
					invoiceItemDTO.setIsActive ( invoiceItem.getIsActive ( ) );
					invoiceItemDTO.setSacCode ( invoiceItem.getSacCode ( ) );

					total = total + (invoiceItem.getItemAmount ());
					if(invoice.getProject().getClient().getClientType().getDataType().equals("D")) {
						sgst = total * 0.09;

						cgst = total * 0.09;

						grandTotal = sgst + cgst + total;
					}else{
						grandTotal = total;
					}
					invoiceDTO.setSgst(sgst);
					invoiceDTO.setCgst(cgst);
					if(invoice.getCountry().getCurrencyCode().equals("INR")) {
						invoiceDTO.setGrandTotal("Rs."+" "+currencyConversion(grandTotal, invoice.getCountry().getCurrencyCode()));
					}else{
						invoiceDTO.setGrandTotal(currencyConversion(grandTotal, invoice.getCountry().getCurrencyCode()));
					}

					invoiceItemDTOS.add ( invoiceItemDTO );

					invoiceDTO.setInvoiceItems ( invoiceItemDTOS );
			}
		return invoiceDTO;
	}


	@Override
	public List<PaymentStatusDTO> listInvoiceStatus() {
		List<PaymentStatus> paymentStatuses = paymentStatusRepository.findAll();
		List<PaymentStatusDTO> paymentStatusDTOS = new ArrayList<>();
		for(PaymentStatus paymentStatus : paymentStatuses){
			PaymentStatusDTO paymentStatusDTO = new PaymentStatusDTO ();

			paymentStatusDTO.setId (paymentStatus.getId ());
			paymentStatusDTO.setDataType(paymentStatus.getDataType());
			paymentStatusDTO.setValue(paymentStatus.getValue());

			paymentStatusDTOS.add(paymentStatusDTO);
		}
		return paymentStatusDTOS;

	}

	/**
	 *
	 * @param invoiceId
	 * @param acceptLanguage
	 * @throws InvoiceManagementException
	 */

	@Override
	public void deleteInvoice(Long invoiceId, String acceptLanguage) throws InvoiceManagementException, MessagingException {

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}
		Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive(invoiceId);
				List<InvoiceItem> invoiceItem= invoiceItemRepository.findAllByInvoice ( invoice );
				if(invoice!=null){
				invoice.setIsActive(false);
				invoice.setIsDelete(true);
				invoice.setDeletedAt(new Date());
				invoice.setDeletedBy(loggedInUser.getId().toString());
				    if (invoice.getIsActive ()==false){
						for (InvoiceItem item : invoiceItem) {
							item.setIsDelete ( true );
							item.setIsActive ( false );
							item.setDeletedAt(new Date());
							item.setDeletedBy(loggedInUser.getId().toString());
							invoiceItemRepository.save ( item );

						}
					}

				invoiceRepository.save(invoice);
				// for sending mail on soft deletion of invoice

					String subject=invoice.getProject().getProjectName()+" invoice deleted - Please review";
			//		String content="Hello,"+"\n"+"\n"+invoice.getProject().getProjectName()+"'s invoice has been deleted, please review.\n\n"+"Thanks\n" +
			//				"Invoice Management Team.";
			//
					String content="Hello,"+"\n"+"\n"+invoice.getProject().getProjectName()+"'s invoice ("+invoice.getInvoiceNumber()+","+"Dated "+invoice.getInvoiceDate()+ ") has been deleted, please review.\n\n"+"Thanks\n" +
							"Invoice Management Team.";

				// please do uncomment this call when you required mail functionality.
				//	sendEmail(subject,content);


				}else{
					throw new InvoiceManagementException(
							messageSource.getMessage("invoiceNotFound", null, new Locale(acceptLanguage)), 400);

				}

	}

	@Override
	public void enableInvoice(Long invoiceId, String acceptLanguage) throws InvoiceManagementException{


		Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive(invoiceId);
		List<InvoiceItem> invoiceItem= invoiceItemRepository.findAllByInvoice ( invoice );
		if(invoice!=null){
			invoice.setIsActive(true);
				for (InvoiceItem item : invoiceItem) {
					item.setIsActive ( true );
					invoiceItemRepository.save ( item );
				}
			invoiceRepository.save(invoice);

		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("invoiceNotFound", null, new Locale(acceptLanguage)), 400);

		}


	}



	@Override
	public PaginationDTO getAllInvoicesList(Pageable pageable, InvoiceFilterRequestWrapper request, String filter, String sort, String order) {

		Page<Invoice> invoicePage = Page.empty();

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		System.out.println("***************** "+loggedInUser.getRole().getRoleType());

		if(loggedInUser.getRole().getRoleType().equals(RoleType.ADMIN) || loggedInUser.getRole().getRoleType().equals(RoleType.ACCOUNT)) {
			invoicePage = invoiceDAO.findAllSearchTextContainingIgnoreCase(request, pageable, filter, sort);
		}else if(loggedInUser.getRole().getRoleType().equals(RoleType.MANAGER)){
			invoicePage = invoiceDAO.findAllSearchTextContainingIgnoreCaseAndRoleTypeManager(request, pageable, filter, sort);
		}
		List<Invoice> invoiceList = invoicePage.getContent();
		List<InvoiceDTO> invoiceDTOList = new ArrayList<>();


		if(!CollectionUtils.isEmpty(invoiceList)){
			for (Invoice invoice : invoiceList){
				InvoiceDTO invoiceDTO = new InvoiceDTO();
				invoiceDTO.setId(invoice.getId());
				invoiceDTO.setInvoiceStartDate(invoice.getInvoiceStartDate());
				invoiceDTO.setInvoiceEndDate(invoice.getInvoiceEndDate());
				invoiceDTO.setInvoiceDate(invoice.getInvoiceDate());
				invoiceDTO.setInvoiceDescription (invoice.getInvoiceDescription ());
				invoiceDTO.setInvoiceSummary(invoice.getInvoiceSummary());
				invoiceDTO.setUpdatedOn ( invoice.getUpdatedOn () );
				if(invoice.getCountry().getCurrencyCode().equals("INR")) {
					invoiceDTO.setInvoiceTotalAmount("Rs."+" "+currencyConversion(invoice.getInvoiceTotalAmount(), invoice.getCountry().getCurrencyCode()));
				}else{
					invoiceDTO.setInvoiceTotalAmount(currencyConversion(invoice.getInvoiceTotalAmount(), invoice.getCountry().getCurrencyCode()));
				}
//				invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
				invoiceDTO.setProjectId ( invoice.getProject ().getId () );
				invoiceDTO.setProjectName(invoice.getProject().getProjectName());
				invoiceDTO.setPaidDate(invoice.getPaidDate());
				invoiceDTO.setDueDate(invoice.getDueDate());
				invoiceDTO.setClientId ( invoice.getProject ().getClient ().getId () );
				invoiceDTO.setClientFirstName ( invoice.getProject ().getClient().getFirstName ());
				invoiceDTO.setClientLastName ( invoice.getProject ().getClient ().getLastName () );
				invoiceDTO.setShippingTerm ( invoice.getShippingTerm () );
				invoiceDTO.setShippingMethod ( invoice.getShippingMethod () );
				invoiceDTO.setIsActive ( invoice.getIsActive () );
				invoiceDTO.setNote(invoice.getNote());
				invoiceDTO.setPaymentMode ( invoice.getPaymentMode ().getValue () );
				invoiceDTO.setPaymentModeId ( invoice.getPaymentMode ().getId () );
				invoiceDTO.setPaymentTerm ( invoice.getPaymentTerm ().getValue () );
				invoiceDTO.setPaymentTermId ( invoice.getPaymentTerm ().getId () );
				invoiceDTO.setPaymentStatus ( invoice.getPaymentStatus ().getValue () );
				invoiceDTO.setPaymentStatusId ( invoice.getPaymentStatus ().getId () );
				invoiceDTO.setCountryId ( invoice.getCountry ().getId () );
				invoiceDTO.setCountryName ( invoice.getCountry ().getCountryName () );
				invoiceDTO.setCurrencyName ( invoice.getCountry ().getCurrencyName () );
				invoiceDTO.setCurrencyCode ( invoice.getCountry ().getCurrencyCode () );
				invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
				invoiceDTO.setUpdatedBy ( loggedInUser.getEmail ());
				invoiceDTO.setRefDraftInvoiceNumber(invoice.getInvoiceNumber());
//				invoiceDTO.setReadyToBilledToClient(true);
				List<InvoiceItem>invoiceItemList= invoiceItemRepository.findAllByInvoice (invoice);
				List<InvoiceItemDTO> invoiceItemDTOS = new ArrayList <> ();

				Double grandTotal = 0.00;
				Double total = 0.00;
				Double cgst = 0.00;
				Double sgst = 0.00;

				for (InvoiceItem invoiceItem: invoiceItemList){

					InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO ( );
					invoiceItemDTO.setId(invoiceItem.getId());
					if(invoice.getCountry().getCurrencyCode().equals("INR")) {
						invoiceItemDTO.setItemAmount("Rs."+" "+currencyConversion(invoiceItem.getItemAmount(), invoice.getCountry().getCurrencyCode()));
					}else{
						invoiceItemDTO.setItemAmount(currencyConversion(invoiceItem.getItemAmount(), invoice.getCountry().getCurrencyCode()));
					}
					invoiceItemDTO.setItemDescription ( invoiceItem.getItemDescription () );
					invoiceItemDTO.setItemNumber ( invoiceItem.getItemNumber ( ) );
					invoiceItemDTO.setHours ( getInvoiceAmountInDecimal(invoiceItem.getHours ( )) );
					invoiceItemDTO.setHourlyRate (currencyConversion(invoiceItem.getHourlyRate ( ),invoice.getCountry().getCurrencyCode()));
					invoiceItemDTO.setQuantity ( invoiceItem.getQuantity () );
					invoiceItemDTO.setIsActive ( invoiceItem.getIsActive () );
					invoiceItemDTO.setSacCode ( invoiceItem.getSacCode () );

					total = total + (invoiceItem.getItemAmount ());
					if(invoice.getProject().getClient().getClientType().getDataType().equals("D")) {
						sgst = total * 0.09;

						cgst = total * 0.09;

						grandTotal = sgst + cgst + total;
					}else{
						grandTotal = total;
					}
					invoiceDTO.setSgst(sgst);
					invoiceDTO.setCgst(cgst);
					if(invoice.getCountry().getCurrencyCode().equals("INR")) {
						invoiceDTO.setGrandTotal("Rs."+" "+currencyConversion(grandTotal, invoice.getCountry().getCurrencyCode()));
					}else{
						invoiceDTO.setGrandTotal(currencyConversion(grandTotal, invoice.getCountry().getCurrencyCode()));
					}


					invoiceItemDTOS.add ( invoiceItemDTO );
					invoiceDTO.setInvoiceItems ( invoiceItemDTOS);


				}

					invoiceDTOList.add(invoiceDTO);
			}
		}
		PaginationDTO<InvoiceDTO> paginationDTO = new PaginationDTO();
		paginationDTO.setList(invoiceDTOList);
		System.out.println("***********"+invoicePage.getTotalElements());
		paginationDTO.setTotalCount(invoicePage.getTotalElements());
		paginationDTO.setTotalPages(invoicePage.getTotalPages());
		return paginationDTO;
	}


	@Override
	public List<CompanyDTO> getAllCompany() {

		List<Company> companies =companyRepository.findAll ();


		List<CompanyDTO> companyDTOS = new ArrayList<>();

		for(Company company : companies) {
			CompanyDTO companyDTO = new CompanyDTO ();
     		companyDTO.setId (company.getId());
			companyDTO.setCompanyName ( company.getCompanyName ( ) );
			companyDTO.setCompanyType (company.getCompanyType ());
			companyDTO.setAddress1 ( company.getAddress1 ());
			companyDTO.setAddress2 ( company.getAddress2 ());
			companyDTO.setEin ( company.getEin ());
			companyDTO.setGstin (company.getGstin ());
			companyDTO.setPanNumber ( company.getPanNumber ());
			companyDTO.setWebsite ( company.getWebsite () );
			companyDTO.setLogoUrl ( company.getLogoUrl ());
			companyDTO.setPhoneNumber (company.getPhoneNumber ());

			companyDTOS.add(companyDTO);
		}
		return companyDTOS;
	}
	@Override
	public List<MasterDataDTO> getAllMasterDatas() {

		List<MasterData> masterDatas =masterDataRepository.findAll ();


		List<MasterDataDTO> masterDataDTOS = new ArrayList<>();

		for(MasterData masterData : masterDatas) {
			MasterDataDTO masterDataDTO = new MasterDataDTO ();
			masterDataDTO.setId (masterData.getId());
			masterDataDTO.setKey ( masterData.getKey ( ) );
			masterDataDTO.setValue (masterData.getValue ());
			masterDataDTOS.add(masterDataDTO);
		}
		return masterDataDTOS;
	}



	@Override
	public List<BankDTO> getAllBanks() {

		List<Bank> banks =bankRepository.findAll ();
		List<BankDTO> bankDTOS = new ArrayList<>();
		for(Bank bank : banks) {
			BankDTO bankDTO = new BankDTO ();
     		bankDTO.setId (bank.getId());
			bankDTO.setBankName ( bank.getBankName ( ) );
			bankDTO.setBankType (bank.getBankType ());
			bankDTO.setBankAccountNumber ( bank.getBankAccountNumber ());
			bankDTO.setAccountName ( bank.getAccountName ());
			bankDTO.setBranch (bank.getBranch ());
			bankDTO.setIFSCCode ( bank.getIFSCCode ());
			bankDTO.setSwiftCode ( bank.getCreditToSwiftCode() );
			bankDTO.setCompanyAccountNumber ( bank.getAccountNumberForCredit ());
			bankDTO.setWireRoutingNumber (bank.getWireRoutingNumber ());
			bankDTO.setAchRoutingNumber (bank.getAchRoutingNumber ());
			bankDTO.setCreditToSwiftCode(bank.getCreditToSwiftCode());
			bankDTO.setPayToSwiftCode(bank.getPayToSwiftCode());
			bankDTO.setAccountNumberForBeneficiary(bank.getAccountNumberForBeneficiary());
			bankDTO.setAccountNumberForCredit(bank.getAccountNumberForCredit());


			bankDTOS.add(bankDTO);
		}
		return bankDTOS;
	}


	@Override
	public File getInvoice(Long invoiceId, String invoiceNumber, String acceptLanguage) {
		Invoice invoice = invoiceRepository.findOneByIdIgnoreIsActive(invoiceId);

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException(commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		if (invoice == null) {
			throw new InvoiceManagementException(
					messageSource.getMessage("invoiceNotFound", null, new Locale("en")), 400);

		}

		InvoicePdfDTO invoiceDTO = new InvoicePdfDTO();

		invoiceDTO.setId(invoice.getId());
		invoiceDTO.setInvoiceStartDate(invoice.getInvoiceStartDate());
		invoiceDTO.setInvoiceEndDate(invoice.getInvoiceEndDate());
		invoiceDTO.setInvoiceDate(invoice.getInvoiceDate());
		invoiceDTO.setInvoiceDescription(invoice.getInvoiceDescription());
		invoiceDTO.setInvoiceSummary(invoice.getInvoiceSummary());
		invoiceDTO.setProjectName(invoice.getProject().getProjectName());
		invoiceDTO.setProjectId(invoice.getProject().getId());
		invoiceDTO.setInvoiceTotalAmount(invoice.getInvoiceTotalAmount());
		invoiceDTO.setPaidDate(invoice.getPaidDate());
		invoiceDTO.setDueDate(invoice.getDueDate());
		invoiceDTO.setUpdatedOn(invoice.getUpdatedOn());
		invoiceDTO.setCountryId(invoice.getCountry().getId());
		invoiceDTO.setCountryName(invoice.getCountry().getCountryName());
		invoiceDTO.setCurrencyCode(invoice.getCountry().getCurrencyCode());
		invoiceDTO.setCurrencyName(invoice.getCountry().getCurrencyName());
		invoiceDTO.setClientId(invoice.getProject().getClient().getId());
		invoiceDTO.setClientFirstName(invoice.getProject().getClient().getFirstName());
		invoiceDTO.setClientLastName(invoice.getProject().getClient().getLastName());
		invoiceDTO.setUpdatedOn(invoice.getUpdatedOn());
		invoiceDTO.setIsActive(invoice.getIsActive());
		invoiceDTO.setNote(invoice.getNote());
		invoiceDTO.setShippingTerm(invoice.getShippingTerm());
		invoiceDTO.setShippingMethod(invoice.getShippingMethod());
		invoiceDTO.setUpdatedBy(loggedInUser.getEmail());
		invoiceDTO.setPaymentStatus(invoice.getPaymentStatus().getValue());
		invoiceDTO.setPaymentStatusId(invoice.getPaymentStatus().getId());
		invoiceDTO.setPaymentTerm(invoice.getPaymentTerm().getValue());
		invoiceDTO.setPaymentTermId(invoice.getPaymentTerm().getId());
		invoiceDTO.setPaymentMode(invoice.getPaymentMode().getValue());
		invoiceDTO.setPaymentModeId(invoice.getPaymentMode().getId());
//		invoiceDTO.setReadyToBilledToClient(true);

		if(invoiceDTO.getReadyToBilledToClient().equals(true) && invoice.getPaymentStatus().toString().equals("2")) {
			invoiceDTO.setInvoiceNumber(tentativeInvoiceNumber(invoice.getProject().getClient().getClientType(), "1", true));
		}else{
			invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
		}
		invoiceDTO.setBillToAddress1(invoice.getProject().getClient().getBillToAddress1());
		invoiceDTO.setBillToZip(invoice.getProject().getClient().getBillToZip());
		invoiceDTO.setBillToAddress2(invoice.getProject().getClient().getBillToAddress2());
		invoiceDTO.setShipToAddress1(invoice.getProject().getClient().getShipToAddress1());
		invoiceDTO.setShipToAddress2(invoice.getProject().getClient().getShipToAddress2());
		invoiceDTO.setShipToZip(invoice.getProject().getClient().getShipToZip());
		System.out.println(invoice.getProject().getClient().getShipToZip());
		System.out.println(invoice.getProject().getClient().getShipToState());
		System.out.println(invoice.getProject().getClient().getShipToCity());


		if(invoice.getProject().getPoNo() != null || invoice.getProject().getPoNo() != "") {
			invoiceDTO.setPoNo(invoice.getProject().getPoNo());
		}else{
			invoiceDTO.setPoNo("");
		}
		invoiceDTO.setPanNo(invoice.getProject().getClient().getPanNo());
		invoiceDTO.setClientGstin(invoice.getProject().getClient().getGstin());
		if(invoice.getProject().getClient().getBillToState() != null) {
			invoiceDTO.setBillToStateName(invoice.getProject().getClient().getBillToState().getStateName());
		}else{
			invoiceDTO.setBillToStateName("");
		}
		if(invoice.getProject().getClient().getShipToState() != null) {
			invoiceDTO.setShipToStateName(invoice.getProject().getClient().getShipToState().getStateName());
		}else{
			invoiceDTO.setShipToStateName("");
		}
		if(invoice.getProject().getClient().getShipToCity() != null){
			invoiceDTO.setShipToCityName(invoice.getProject().getClient().getShipToCity().getCityName());
		}else{
			invoiceDTO.setShipToCityName("");
		}
		if(invoice.getProject().getClient().getBillToCity() != null){
			invoiceDTO.setBillToCityName(invoice.getProject().getClient().getBillToCity().getCityName());
		}else{
			invoiceDTO.setBillToCityName("");
		}
		if(invoice.getProject().getClient().getCountry() != null){
			invoiceDTO.setBillToCountryName(invoice.getProject().getClient().getCountry().getCountryName());
		}else{
			invoiceDTO.setBillToCountryName("");
		}
		if(invoice.getProject().getClient().getCountry() != null){
			invoiceDTO.setShipToCountryName(invoice.getProject().getClient().getCountry().getCountryName());
		}else{
			invoiceDTO.setShipToCountryName("");
		}

		if(invoice.getProject().getClient().getClientType().getDataType().equals("L")){
			Company company1 = companyRepository.findCompanyById(2l);
			invoiceDTO.setCompanyName(company1.getCompanyName());
			invoiceDTO.setCompanyAddress1(company1.getAddress1());
			invoiceDTO.setCompanyAddress2(company1.getAddress2());
			invoiceDTO.setEin(company1.getEin());
		}else if(invoice.getProject().getClient().getClientType().getDataType().equals("I") || invoice.getProject().getClient().getClientType().getDataType().equals("D")){
			List<Company> companyList = companyRepository.findAllDomesticCompanyList();
			for(Company company1 :companyList){
				String companyAddressWithWhiteSpaces = company1.getAddress1().replaceAll(",","");
				String companyAddressWithoutWhiteSpaces = companyAddressWithWhiteSpaces.replaceAll("[0-9]","");
				String locationAddressWithWhiteSpaces = invoice.getProject().getLocation().getArea().replaceAll(",","");
				String locationAddressWithoutWhiteSpaces = locationAddressWithWhiteSpaces.replaceAll("[0-9]","");

				if(StringUtils.trimAllWhitespace(companyAddressWithoutWhiteSpaces).equals(StringUtils.trimAllWhitespace(locationAddressWithoutWhiteSpaces))){

					invoiceDTO.setCompanyName(company1.getCompanyName());
					invoiceDTO.setCompanyAddress1(company1.getAddress1());
					invoiceDTO.setCompanyAddress2(company1.getAddress2());
					invoiceDTO.setCompanyCity(company1.getCity());
					invoiceDTO.setCompanyState(company1.getState());
					invoiceDTO.setCompanyZip(company1.getZip());
					invoiceDTO.setCompanyCountry(company1.getCountry());
					invoiceDTO.setEin(company1.getEin());
					invoiceDTO.setGstin(company1.getGstin());
					invoiceDTO.setCompanyPanNo(company1.getPanNumber());
					invoiceDTO.setCompanyPhoneNo(company1.getPhoneNumber());
					invoiceDTO.setCompanyWebsite(company1.getWebsite());

				}

			}
		}

		List<Bank> bankList = bankRepository.findAll();
		for (Bank bank1 : bankList) {

			if(invoice.getProject().getClient().getClientType().getDataType().equalsIgnoreCase(bank1.getBankType())) {
				invoiceDTO.setBankName(bank1.getBankName());
				invoiceDTO.setBranch(bank1.getBranch());
				invoiceDTO.setAccountName(bank1.getAccountName());
				invoiceDTO.setCreditToSwiftCode(bank1.getCreditToSwiftCode());
				invoiceDTO.setPayToSwiftCode(bank1.getPayToSwiftCode());
				invoiceDTO.setAchRoutingNumber(bank1.getAchRoutingNumber());
				invoiceDTO.setAccountNo(bank1.getBankAccountNumber());
				invoiceDTO.setAccountNoForBeneficiary(bank1.getAccountNumberForBeneficiary());
				invoiceDTO.setAccountNoForCredit(bank1.getAccountNumberForCredit());
				invoiceDTO.setIfsc(bank1.getIFSCCode());
				invoiceDTO.setWireRoutingNumber(bank1.getWireRoutingNumber());
				invoiceDTO.setPayToBankName(bank1.getPayToBankName());
			}

		}


		Double grandTotal = 0.00;
		Double total = 0.00;
		Double cgst = 0.00;
		Double sgst = 0.00;
		Double igst = 0.00;


		List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAllByInvoice(invoice);
		List<InvoicePdfItemDTO> invoiceItemDTOS = new ArrayList<>();

		for (InvoiceItem invoiceItem : invoiceItemList) {

			InvoicePdfItemDTO invoiceItemDTO = new InvoicePdfItemDTO();
			invoiceItemDTO.setId(invoiceItem.getId());
			invoiceItemDTO.setItemAmount(currencyConversion(invoiceItem.getItemAmount(),invoice.getCountry().getCurrencyCode()));
			invoiceItemDTO.setItemDescription(invoiceItem.getItemDescription());
			invoiceItemDTO.setItemNumber(invoiceItem.getItemNumber());
			if(invoiceItem.getHours()==0)
			{
				invoiceItemDTO.setHours("-");
				invoiceDTO.setHoursPresent(false);
			}
			else{
				invoiceItemDTO.setHours((getInvoiceAmountInDecimal(invoiceItem.getHours())));
				invoiceDTO.setHoursPresent(true);
			}
			if(invoiceItem.getHourlyRate()==0)
			{
				invoiceItemDTO.setHourlyRate("-");
				invoiceDTO.setRatePresent(false);
			}
			else{
				invoiceItemDTO.setHourlyRate(currencyConversion(invoiceItem.getHourlyRate(),invoice.getCountry().getCurrencyCode()));
				invoiceDTO.setRatePresent(true);

			}
			double qty = Double.parseDouble(invoiceItem.getQuantity());
			if(qty== 0.0 )
			{
				invoiceItemDTO.setQuantity("-");
				invoiceDTO.setQuantityPresent(false);
			}
			else
			{
				invoiceItemDTO.setQuantity(invoiceItem.getQuantity());
				invoiceDTO.setQuantityPresent(true);
			}

			invoiceItemDTO.setIsActive(invoiceItem.getIsActive());
			invoiceItemDTO.setSacCode(invoiceItem.getSacCode());
			invoiceItemDTO.setItemAmountDouble(invoiceItem.getItemAmount());

			invoiceDTO.setSgstValue(masterDataRepository.findValueByMasterDataId(3l));
			invoiceDTO.setCgstValue(masterDataRepository.findValueByMasterDataId(2l));
			invoiceDTO.setIgstValue(masterDataRepository.findValueByMasterDataId(4l));
			total = total + (invoiceItemDTO.getItemAmountDouble());
			if(invoice.getProject().getClient().getClientType().getDataType().equals("D")) {
				sgst = total * Double.parseDouble(invoiceDTO.getSgstValue())/100;

				cgst = total * Double.parseDouble(invoiceDTO.getCgstValue())/100;

				igst = total * Double.parseDouble(invoiceDTO.getIgstValue())/100;

				grandTotal = sgst + cgst + total;
			}else{
				grandTotal = total;
			}
			//if (invoiceItem.getInvoice ().getId ().equals ( invoiceId )){
			invoiceItemDTOS.add(invoiceItemDTO);
			//}
			invoiceDTO.setInvoiceItems(invoiceItemDTOS);
		}

		invoiceDTO.setTotal(currencyConversion(total, invoice.getCountry().getCurrencyCode()));

		invoiceDTO.setCgst(currencyConversion(cgst,invoice.getCountry().getCurrencyCode()));
		invoiceDTO.setSgst(currencyConversion(sgst,invoice.getCountry().getCurrencyCode()));
		System.out.println("***********"+ igst);
		invoiceDTO.setIgst(currencyConversion(igst,invoice.getCountry().getCurrencyCode()));
		System.out.println("***********"+ invoiceDTO.getIgst());
		invoiceDTO.setGrandTotal(currencyConversion(grandTotal,invoice.getCountry().getCurrencyCode()));


		Map<String, Object> data = new HashMap<>();
		data.put("invoiceData", invoiceDTO);
		String mailType = null;

		if(invoice.getProject().getClient().getClientType().getDataType().equals("D")){
			if(invoice.getProject().getClient().getBillToState().getStateName().equals(invoiceDTO.getCompanyState())){
				mailType = "DOMESTIC_SAME_STATE_ATTACHMENT";
			}else{
				mailType = "DOMESTIC_DIFF_STATE_ATTACHMENT";
			}
		}else if(invoice.getProject().getClient().getClientType().getDataType().equals("I")){
			mailType = "INTERNATIONAL_ATTACHMENT";
		}else if(invoice.getProject().getClient().getClientType().getDataType().equals("L")){
			mailType = "LLC_ATTACHMENT";
		}

			String processedHtml = mailContentBuilder.build(data, mailType);
			FileOutputStream os = null;
			File outputFile = null;
			try {
				outputFile = File.createTempFile(invoiceNumber, ".pdf");

				os = new FileOutputStream(outputFile);

				ITextRenderer renderer = new ITextRenderer();

				renderer.setDocumentFromString(processedHtml);

				renderer.layout();
				renderer.createPDF(os, false);
				renderer.finishPDF();
				System.out.println("PDF created successfully");

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
					}
				}
			}
			return outputFile;
		}


	public String getInvoiceAmountInDecimal(Double amount) {
		String text = Double. toString(Math. abs(amount));
		int integerPlaces = text.indexOf('.');
		int decimalPlaces = text.length() - integerPlaces - 1;

		String value = "";
		if(decimalPlaces >2)
		{
			value = String.valueOf(Math.round(amount*100.0)/100.0);
		}else if(decimalPlaces ==2)
		{
			value = String.valueOf(amount);
		}else if(decimalPlaces == 1){
			value = text+"0";
		}else if(decimalPlaces == 0){
			value = text +"00";
		}
		return value;
	}


	public String currencyConversion(double amount, String outputCurrency){
		Locale locale;

		String result = null;

		if(outputCurrency.equals("EUR")) {
			locale = new Locale("de", "DE");
		} else if (outputCurrency.equals("USD")) {
			locale = new Locale("en", "US");
		} else if (outputCurrency.equals("INR")){

			double aboveThousands = amount / 1000;
			double thousands = amount % 1000;

			if (aboveThousands > 1) {
				DecimalFormat formatter = new DecimalFormat("##,##");
				formatter.setRoundingMode(RoundingMode.DOWN); //will round towards zero whether negative or positive. Same as truncating.
				String one = formatter.format(aboveThousands);

				formatter.applyPattern("#,##,##,##,000.00");
				formatter.setRoundingMode(RoundingMode.HALF_EVEN); //default rounding mode of DecimalFormat
				String two = formatter.format(thousands);

				result = one + "," + two;
				return result;
			} else {
				DecimalFormat formatter = new DecimalFormat("#,##,##,##,##0.00");
				result = formatter.format(amount);
				return result;
			}
		}else{
			locale = new Locale("de", "DE");
		}

		Currency currency = Currency.getInstance(locale);
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		System.out.println(currency.getDisplayName() + ": " + numberFormat.format(amount));

		if(outputCurrency.equals("INR")){
			return result;
		}else {
			return numberFormat.format(amount);
		}
	}

	private void sendEmail(String subject,String content) throws MessagingException {
		String s1 = "client.invoice@biz4solutions.com";
		//String s1="accounts@biz4solutions.com";

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username=null;
		if (principal instanceof UserDetails) {
			 username = ((UserDetails)principal).getUsername();
		} else {
			 username = principal.toString();
		}

		//System.out.println(username+"**************************************************");

		String s2=username;
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("client.invoice@biz4solutions.com");



		//helper.setTo(toAddress);

		helper.setTo(new String[]{s1,s2});
		helper.setSubject(subject);

		//content = content.replace("sagar", "sagar");
		//  String verifyURL = serverURL + "/api/v1/users/verify?code=" + user.getVerificationCode();

		//content = content.replace("sagar", "sagar");

		//System.out.println("***********************"+content);
		helper.setText(content);


			mailSender.send(message);

		//	throw new javax.mail.AuthenticationFailedException();


	}


}








