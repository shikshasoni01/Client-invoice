package com.management.clientinvoice.service.Impl;

import java.util.*;

import com.management.clientinvoice.dao.ClientDAO;
import com.management.clientinvoice.domain.*;
import com.management.clientinvoice.dto.ClientDTO;
import com.management.clientinvoice.dto.ClientTypeDTO;
import com.management.clientinvoice.dto.PaginationDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.*;
import com.management.clientinvoice.requestWrapper.ClientFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.ClientRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateClientRequestWrapper;
import com.management.clientinvoice.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.management.clientinvoice.service.ClientService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class ClientServiceImpl implements ClientService {


	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private UserIdentityValidator userIdentityValidator;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private PaymentModeRepository paymentModeRepository;

	@Autowired
	private ClientDAO clientDAO;

	@Autowired
	private ClientTypeRepository clientTypeRepository;

	@Autowired
	private ProjectRepository projectRepository;


	@Override
	public List<ClientDTO> getAllClient() {

		List<Client> clients = clientRepository.findAllByIsActive();

		List<ClientDTO> clientDTOs = new ArrayList<>();

		for(Client client : clients) {
			ClientDTO clientDTO = new ClientDTO();
			clientDTO.setId(client.getId());
			clientDTO.setBillToAddress1(client.getBillToAddress1());
			clientDTO.setBillToAddress2(client.getBillToAddress2());
			clientDTO.setAlternateName(client.getAlternateName());
			clientDTO.setClientId(client.getClientId());
			clientDTO.setContactNo(client.getContactNo());
			clientDTO.setGstin(client.getGstin());
			clientDTO.setContactPersonFirstName(client.getContactPersonFirstName());
			clientDTO.setContactPersonLastName(client.getContactPersonLastName());
			clientDTO.setFirstName(client.getFirstName());
			clientDTO.setLastName(client.getLastName());
			clientDTO.setFaxNumber(client.getFaxNumber());
			clientDTO.setGstin(client.getGstin());
			clientDTO.setBillToZip(client.getBillToZip());
			clientDTO.setEmail(client.getEmail());
			clientDTO.setWebsite ( client.getWebsite () );
			clientDTO.setUpdatedOn(client.getUpdatedOn());
			clientDTO.setIsActive(client.getIsActive());
			clientDTO.setPaymentModeId(client.getPaymentMode().getId());
			clientDTO.setCountryId(client.getCountry().getId());
			clientDTO.setPaymentMode(client.getPaymentMode().getValue());
			clientDTO.setCountryName(client.getCountry().getCountryName());
			if(client.getBillToState() != null) {
				clientDTO.setBillToStateId(client.getBillToState().getId());
			}else{
				clientDTO.setBillToStateId(null);
			}
			if(client.getBillToCity() != null) {
				clientDTO.setBillToCityId(client.getBillToCity().getId());
			}else{
				clientDTO.setBillToCityId(null);
			}
			if(client.getBillToState() != null) {
				clientDTO.setBillToStateName(client.getBillToState().getStateName());
			}else{
				clientDTO.setBillToStateName(null);
			}
			if(client.getBillToCity() != null) {
				clientDTO.setBillToCityName(client.getBillToCity().getCityName());
			}else{
				clientDTO.setBillToCityName(null);
			}
			if(client.getShipToState() != null) {
				clientDTO.setShipToStateId(client.getShipToState().getId());
			}else{
				clientDTO.setShipToStateId(null);
			}
			if(client.getShipToCity() !=null) {
				clientDTO.setShipToCityId(client.getShipToCity().getId());
			}else{
				clientDTO.setShipToCityId(null);
			}
			if(client.getShipToState() != null) {
				clientDTO.setShipToStateName(client.getShipToState().getStateName());
			}else{
				clientDTO.setShipToStateName(null);
			}
			if(client.getShipToCity() != null) {
				clientDTO.setShipToCityName(client.getShipToCity().getCityName());
			}else{
				clientDTO.setShipToCityName(null);
			}
			clientDTO.setClientTypeName ( client.getClientType ().getValue ());
			clientDTO.setClientTypeId ( client.getClientType ().getId ());
			clientDTO.setClientTypeValue ( client.getClientType ().getDataType ());
			clientDTO.setPanNo(client.getPanNo());
			clientDTO.setShipToZip(client.getShipToZip());
			clientDTO.setShipToAddress1(client.getShipToAddress1());
			clientDTO.setShipToAddress2(client.getShipToAddress2());
			clientDTOs.add(clientDTO);
		}
//		System.out.println(invoice.size());
		return clientDTOs;
	}

	@Override
	public void updateClient(UpdateClientRequestWrapper request, String acceptLanguage) throws InvoiceManagementException {

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		Client client = clientRepository.findOneByIdIgnoreIsActive(request.getId());
		if(client != null) {

			client.setContactNo(request.getContactNo());
			client.setContactPersonFirstName(request.getContactPersonFirstName());
			client.setContactPersonLastName(request.getContactPersonLastName());
			client.setClientId(request.getClientId());
			client.setFirstName(request.getFirstName());
			client.setLastName(request.getLastName());
			client.setAlternateName(request.getAlternateName());
			client.setBillToAddress1(request.getBillToAddress1());
			client.setBillToAddress2(request.getBillToAddress2());
			client.setBillToZip(request.getBillToZip());
			client.setEmail(request.getEmail());
			client.setFaxNumber(request.getFaxNumber());
			client.setGstin(request.getGstin());
			client.setWebsite(request.getWebsite());
			client.setIsActive(request.getIsActive());
			client.setIsDelete(!request.getIsActive());
			client.setUpdatedOn(request.getUpdatedOn());
			client.setPanNo(request.getPanNo());
			client.setShipToZip(request.getShipToZip());
			client.setShipToAddress1(request.getShipToAddress1());
			client.setShipToAddress2(request.getShipToAddress2());
			client.setUpdatedBy(loggedInUser.getId().toString());


			Optional<PaymentMode> paymentMode = paymentModeRepository.findById(request.getPaymentModeId());
			if (paymentMode.isPresent()) {
				client.setPaymentMode(paymentMode.get());
			} else {
				throw new InvoiceManagementException(
						messageSource.getMessage("paymentModeNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<ClientType> clientType = clientTypeRepository.findById(request.getClientTypeId ());
			if (clientType.isPresent()) {
				client.setClientType (clientType.get());
			} else {
				throw new InvoiceManagementException(
						messageSource.getMessage("clientTypeIdNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<Country> country = countryRepository.findById(request.getCountryId());
			if (country.isPresent()) {
				client.setCountry(country.get());
			} else {
				throw new InvoiceManagementException(
						messageSource.getMessage("countryNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<State> state = stateRepository.findById(request.getShipToStateId());
			if (state.isPresent()) {
				client.setShipToState(state.get());
			} else {
				client.setShipToState(null);
//				throw new InvoiceManagementException(
//						messageSource.getMessage("stateNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<City> city = cityRepository.findById(request.getShipToCityId());
			if (city.isPresent()) {
				client.setShipToCity(city.get());
			} else {
				client.setShipToCity(null);
//				throw new InvoiceManagementException(
//						messageSource.getMessage("cityNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<State> state1 = stateRepository.findById(request.getBillToStateId());
			if (state1.isPresent()) {
				client.setBillToState(state1.get());
			} else {
				client.setBillToState(null);
//				throw new InvoiceManagementException(
//						messageSource.getMessage("stateNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<City> city1 = cityRepository.findById(request.getBillToCityId());
			if (city1.isPresent()) {
				client.setBillToCity(city1.get());
			} else {
				client.setBillToCity(null);
//				throw new InvoiceManagementException(
//						messageSource.getMessage("cityNotFound", null, new Locale(acceptLanguage)), 400);
			}
		}else if(client == null){
			throw new InvoiceManagementException(
					messageSource.getMessage("clientNotFound", null, new Locale(acceptLanguage)), 400);

		}

		clientRepository.save(client);
	}

	@Override
	public ClientDTO getASingleClient(Long id) throws InvoiceManagementException{
		Client client = clientRepository.findOneByIdIgnoreIsActive(id);
		if(client != null) {
			ClientDTO clientDTO = new ClientDTO();
			clientDTO.setBillToAddress1(client.getBillToAddress1());
			clientDTO.setBillToAddress2(client.getBillToAddress2());
			clientDTO.setAlternateName(client.getAlternateName());
			clientDTO.setClientId(client.getClientId());
			clientDTO.setContactNo(client.getContactNo());
			clientDTO.setGstin(client.getGstin());
			clientDTO.setContactPersonFirstName(client.getContactPersonFirstName());
			clientDTO.setContactPersonLastName(client.getContactPersonLastName());
			clientDTO.setFirstName(client.getFirstName());
			clientDTO.setLastName(client.getLastName());
			clientDTO.setFaxNumber(client.getFaxNumber());
			clientDTO.setGstin(client.getGstin());
			clientDTO.setBillToZip(client.getBillToZip());
			clientDTO.setEmail(client.getEmail());
			clientDTO.setWebsite ( client.getWebsite () );
			clientDTO.setUpdatedOn(client.getUpdatedOn());
			clientDTO.setIsActive(client.getIsActive());
			clientDTO.setPaymentModeId(client.getPaymentMode().getId());
			clientDTO.setCountryId(client.getCountry().getId());
			clientDTO.setPaymentMode(client.getPaymentMode().getValue());
			clientDTO.setCountryName(client.getCountry().getCountryName());
			if(client.getBillToState() != null) {
				clientDTO.setBillToStateId(client.getBillToState().getId());
			}else{
				clientDTO.setBillToStateId(null);
			}
			if(client.getBillToCity() != null) {
				clientDTO.setBillToCityId(client.getBillToCity().getId());
			}else{
				clientDTO.setBillToCityId(null);
			}
			if(client.getBillToState() != null) {
				clientDTO.setBillToStateName(client.getBillToState().getStateName());
			}else{
				clientDTO.setBillToStateName(null);
			}
			if(client.getBillToCity() != null) {
				clientDTO.setBillToCityName(client.getBillToCity().getCityName());
			}else{
				clientDTO.setBillToCityName(null);
			}
			if(client.getShipToState() != null) {
				clientDTO.setShipToStateId(client.getShipToState().getId());
			}else{
				clientDTO.setShipToStateId(null);
			}
			if(client.getShipToCity() !=null) {
				clientDTO.setShipToCityId(client.getShipToCity().getId());
			}else{
				clientDTO.setShipToCityId(null);
			}
			if(client.getShipToState() != null) {
				clientDTO.setShipToStateName(client.getShipToState().getStateName());
			}else{
				clientDTO.setShipToStateName(null);
			}
			if(client.getShipToCity() != null) {
				clientDTO.setShipToCityName(client.getShipToCity().getCityName());
			}else{
				clientDTO.setShipToCityName(null);
			}
			clientDTO.setClientTypeName ( client.getClientType ().getValue ());
			clientDTO.setClientTypeId ( client.getClientType ().getId ());
			clientDTO.setClientTypeValue ( client.getClientType ().getDataType ());
			clientDTO.setPanNo(client.getPanNo());
			clientDTO.setShipToZip(client.getShipToZip());
			clientDTO.setShipToAddress1(client.getShipToAddress1());
			clientDTO.setShipToAddress2(client.getShipToAddress2());
			return clientDTO;
		}else{
			throw new InvoiceManagementException(
					messageSource.getMessage("clientIdNotFound", null, new Locale("en")), 400);

		}
	}

	@Override
	public void createClient(ClientRequestWrapper request,String acceptLanguage) throws InvoiceManagementException {


		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}

		Client newClient = new Client();

//		Client client = clientRepository.findOneByEmail(request.getEmail());
//		 if(request.getClientId ().isEmpty() && request.getEmail ().isEmpty() && request.getFirstName ().isEmpty() && request.getWebsite ().isEmpty()) {
//			throw new InvoiceManagementException(messageSource.getMessage("requiredFieldIsMissing", null, new Locale(acceptLanguage)),
//					400);}
//		else if(client == null) {
			newClient.setBillToAddress1(request.getBillToAddress1());
			newClient.setBillToAddress2(request.getBillToAddress2());
			newClient.setBillToZip(request.getBillToZip());
			newClient.setAlternateName(request.getAlternateName());
			newClient.setCreatedBy(loggedInUser.getId().toString());
//			Client client1 = clientRepository.findByClientId(request.getClientId());
//			if(client1 == null) {
		String firstName = String.valueOf(request.getFirstName().charAt(0));
		if(!request.getLastName().isEmpty()) {
			String lastName = String.valueOf(request.getLastName().charAt(0));

			newClient.setClientId("1234" + "-" + firstName.toUpperCase() + lastName.toUpperCase());
		}else{
			newClient.setClientId("1234" + "-" + firstName.toUpperCase());
		}
//			}else{
//				throw new InvoiceManagementException(messageSource.getMessage("clientIdAlreadyExists", null, new Locale(acceptLanguage)),
//						400);
//			}

//			if(request.getContactNo() !=null && request.getContactNo()!=""){
			newClient.setContactNo(request.getContactNo());
//	}

			newClient.setContactPersonFirstName(request.getContactPersonFirstName());
			newClient.setContactPersonLastName(request.getContactPersonLastName());


//			if (request.getEmail ()==null ||request.getEmail ()==""){
//				throw new InvoiceManagementException(messageSource.getMessage("emailShouldNotEmpty", null, new Locale(acceptLanguage)),
//						400);
//
//			}
//			else {
				newClient.setEmail ( request.getEmail ( ) );
//			}
			newClient.setFaxNumber(request.getFaxNumber());

			if(request.getFirstName ()!=null&&request.getFirstName ()!=""){
			newClient.setFirstName(request.getFirstName());
			}else{
				throw new InvoiceManagementException(
						messageSource.getMessage("firstNameEmpty", null, new Locale(acceptLanguage)), 400);
			}
			if(request.getLastName()!= null) {
				newClient.setLastName(request.getLastName());
			}
//			Client clientGstin = clientRepository.findByGstin(request.getGstin());
//			if(clientGstin == null) {
				newClient.setGstin(request.getGstin());
//			}else{
//				throw new InvoiceManagementException(messageSource.getMessage("clientGstinAlreadyExists", null, new Locale(acceptLanguage)),
//						400);
//			}
			newClient.setWebsite(request.getWebsite());
			newClient.setIsActive(request.getIsActive());
			newClient.setUpdatedOn(request.getUpdatedOn());
			newClient.setPanNo(request.getPanNo());
			newClient.setShipToZip(request.getShipToZip());
			newClient.setShipToAddress1(request.getShipToAddress1());
			newClient.setShipToAddress2(request.getShipToAddress2());

			Optional<Country> country = countryRepository.findById(request.getCountryId());
			if(country.isPresent()) {
				newClient.setCountry(country.get());
			}else{
				throw new InvoiceManagementException(
                    messageSource.getMessage("countryNotFound", null, new Locale(acceptLanguage)), 400);
			}
			Optional<ClientType> clientType = clientTypeRepository.findById(request.getClientTypeId ());
			if(clientType.isPresent()) {
				newClient.setClientType (clientType.get());
			}else{
				throw new InvoiceManagementException(
                    messageSource.getMessage("clientTypeIdNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<State> state = stateRepository.findById(request.getBillToStateId());
			if(state.isPresent()) {
				newClient.setBillToState(state.get());
			}else{

				newClient.setBillToState(null);
//								throw new InvoiceManagementException(
//						messageSource.getMessage("stateNotFound", null, new Locale(acceptLanguage)), 400);
			}

			Optional<City> city = cityRepository.findById(request.getBillToCityId());
			if(city.isPresent()) {
				newClient.setBillToCity(city.get());
			}else{
				newClient.setBillToCity(null);
//				throw new InvoiceManagementException(
//						messageSource.getMessage("cityNotFound", null, new Locale(acceptLanguage)), 400);
			}

			 Optional<City> city1 = cityRepository.findById(request.getShipToCityId());
			 if(city1.isPresent()) {
				 newClient.setShipToCity(city1.get());
			 }else{
				 newClient.setShipToCity(null);
//				 throw new InvoiceManagementException(
//						 messageSource.getMessage("cityNotFound", null, new Locale(acceptLanguage)), 400);
			 }

			 Optional<State> state1 = stateRepository.findById(request.getShipToStateId());
			 if(state1.isPresent()) {
				 newClient.setShipToState(state1.get());
			 }else{
				 newClient.setShipToState(null);
//				 throw new InvoiceManagementException(
//						 messageSource.getMessage("stateNotFound", null, new Locale(acceptLanguage)), 400);
			 }


			 Optional<PaymentMode> paymentMode = paymentModeRepository.findById(request.getPaymentModeId());
			if(paymentMode.isPresent()) {
				newClient.setPaymentMode(paymentMode.get());
			}else{
								throw new InvoiceManagementException(
						messageSource.getMessage("paymentModeNotFound", null, new Locale(acceptLanguage)), 400);
			}


			clientRepository.save(newClient);
//		}
//		else{
//			throw new InvoiceManagementException(messageSource.getMessage("clientAlreadyExists", null, new Locale(acceptLanguage)),
//					500);
//		}
	}

	@Override
	public void deleteClient(Long id, String acceptLanguage) throws InvoiceManagementException {

		UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
		if (loggedInUser == null) {
			throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
		}
	Client client = clientRepository.findOneByIdIgnoreIsActive(id);
	List<Projects> projects =projectRepository.findByClient(client);
	if(client != null && projects.isEmpty()) {
		client.setIsActive(false);
		client.setIsDelete(true);
		client.setDeletedAt(new Date());
		client.setDeletedBy(loggedInUser.getId().toString());
		clientRepository.save(client);
	}else if(projects != null){
		throw new InvoiceManagementException(
				messageSource.getMessage("projectNotDeleted", null, new Locale(acceptLanguage)), 400);

	}else{
		throw new InvoiceManagementException(
				messageSource.getMessage("clientNotFound", null, new Locale(acceptLanguage)), 400);

	}
	}

	@Override
	public void enableClient(Long id, String acceptLanguage) throws InvoiceManagementException{

		Client client = clientRepository.findOneByIdIgnoreIsActive(id);
		if (client == null) {
			throw new InvoiceManagementException("clientNotFound");
		}

		if (!client.getIsActive()) {
			client.setIsActive(true);

			clientRepository.save(client);
		}

	}

	@Override
	public List<ClientTypeDTO> listClientType() {
		List<ClientType> clientTypes = clientTypeRepository.findAll();
		List<ClientTypeDTO> clientTypeDTOS = new ArrayList<>();
		for(ClientType clientType : clientTypes){
			ClientTypeDTO clientTypeDTO = new ClientTypeDTO ();

			clientTypeDTO.setId (clientType.getId ());
			clientTypeDTO.setDataType(clientType.getDataType());
			clientTypeDTO.setValue(clientType.getValue());

			clientTypeDTOS.add(clientTypeDTO);
		}
		return clientTypeDTOS;

		//return Arrays.asList(InvoiceStatus.BILLEDTOCLIENT,InvoiceStatus.FULLYPAID,InvoiceStatus.PENDING,InvoiceStatus.PARTIALLYPAID);
	}


	@Override
	@Transactional
	public PaginationDTO<ClientDTO> getAllClientList(Pageable pageable, ClientFilterRequestWrapper request, String filter, String sort, String order) throws InvoiceManagementException {
		Page<Client> clientPage = Page.empty();
		clientPage = clientDAO.findAllSearchTextContainingIgnoreCase(request, pageable, filter,sort);
		List<Client> clientList = clientPage.getContent();
		List<ClientDTO> clientDTOList = new ArrayList<>();


		if(!CollectionUtils.isEmpty(clientList)){
			for (Client client : clientList){
				ClientDTO clientDTO = new ClientDTO();
				clientDTO.setId(client.getId());
				clientDTO.setBillToAddress1(client.getBillToAddress1());
				clientDTO.setBillToAddress2(client.getBillToAddress2());
				clientDTO.setBillToZip(client.getBillToZip());
				clientDTO.setAlternateName(client.getAlternateName());
				clientDTO.setClientId(client.getClientId());
				clientDTO.setContactNo(client.getContactNo());
				clientDTO.setGstin(client.getGstin());
				clientDTO.setContactPersonFirstName(client.getContactPersonFirstName());
				clientDTO.setContactPersonLastName(client.getContactPersonLastName());
				clientDTO.setFirstName(client.getFirstName());
				clientDTO.setLastName(client.getLastName());
				clientDTO.setFaxNumber(client.getFaxNumber());
				clientDTO.setGstin(client.getGstin());
				clientDTO.setEmail(client.getEmail());
				clientDTO.setWebsite(client.getWebsite());
				clientDTO.setUpdatedOn(client.getUpdatedOn());
				clientDTO.setIsActive(client.getIsActive());
				clientDTO.setPaymentModeId(client.getPaymentMode().getId());
				clientDTO.setCountryId(client.getCountry().getId());
				clientDTO.setPaymentMode(client.getPaymentMode().getValue());
				clientDTO.setCountryName(client.getCountry().getCountryName());
				if(client.getBillToState() != null) {
					clientDTO.setBillToStateId(client.getBillToState().getId());
				}else{
					clientDTO.setBillToStateId(null);
				}
				if(client.getBillToCity() != null) {
					clientDTO.setBillToCityId(client.getBillToCity().getId());
				}else{
					clientDTO.setBillToCityId(null);
				}
				if(client.getBillToState() != null) {
					clientDTO.setBillToStateName(client.getBillToState().getStateName());
				}else{
					clientDTO.setBillToStateName(null);
				}
				if(client.getBillToCity() != null) {
					clientDTO.setBillToCityName(client.getBillToCity().getCityName());
				}else{
					clientDTO.setBillToCityName(null);
				}
				if(client.getShipToState() != null) {
					clientDTO.setShipToStateId(client.getShipToState().getId());
				}else{
					clientDTO.setShipToStateId(null);
				}
				if(client.getShipToCity() !=null) {
					clientDTO.setShipToCityId(client.getShipToCity().getId());
				}else{
					clientDTO.setShipToCityId(null);
				}
				if(client.getShipToState() != null) {
					clientDTO.setShipToStateName(client.getShipToState().getStateName());
				}else{
					clientDTO.setBillToStateName(null);
				}
				if(client.getShipToCity() != null) {
					clientDTO.setShipToCityName(client.getShipToCity().getCityName());
				}else{
					clientDTO.setBillToCityName(null);
				}
				clientDTO.setClientTypeName ( client.getClientType () .getValue ());
				clientDTO.setClientTypeId ( client.getClientType () .getId ());
				clientDTO.setClientTypeValue ( client.getClientType () .getDataType ());
				clientDTO.setPanNo(client.getPanNo());
				clientDTO.setShipToZip(client.getShipToZip());
				clientDTO.setShipToAddress1(client.getShipToAddress1());
				clientDTO.setShipToAddress2(client.getShipToAddress2());
				clientDTOList.add(clientDTO);

			}
		}

		PaginationDTO<ClientDTO> paginationDTO = new PaginationDTO();
		paginationDTO.setList(clientDTOList);
		paginationDTO.setTotalCount(clientPage.getTotalElements());
		paginationDTO.setTotalPages(clientPage.getTotalPages());
		return paginationDTO;
	}


//	@Override
//	public List<String> clientNameList() {
//
//		List<String> clientNameList = new ArrayList<>();
//
//		List<Client> clientList = clientRepository.findAllByFirstName();
//
//		for(Client client : clientList){
//			clientNameList.add(client.getFirstName());
//		}
//
//		return clientNameList;
//	}



	//	List<String>  clientNameList = new ArrayList<>();
//
//	List<Client> clientList = clientRepository.findAllClientIgnoreCase();
//
//
//	for(Client client :clientList){
//		clientNameList.add(client.getFirstName());
//	}
//	returnclientNameList




}