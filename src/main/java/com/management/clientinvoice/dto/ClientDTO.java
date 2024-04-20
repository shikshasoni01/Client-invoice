package com.biz4solutions.clientinvoice.dto;

import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.biz4solutions.clientinvoice.domain.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.sql.Date;

@Getter
@Setter
public class ClientDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String firstName;

    private String lastName;

    private String contactPersonFirstName;

    private String contactPersonLastName;

    private String alternateName;

    private String gstin;

    private String clientId;

    private String email;

    private String billToAddress1;

    private String billToAddress2;

    private String billToZip;

    private String website;

    private String contactNo;

    private String faxNumber;

    private Date updatedOn;

    private Long countryId;

    private Long shipToStateId;

    private Long shipToCityId;

    private Long paymentModeId;

    private String PaymentMode;

    private String countryName;

    private String shipToStateName;

    private String shipToCityName;

    private Long clientTypeId;

    private String clientTypeName;

    private String clientTypeValue;

    private String shipToAddress1;

    private String shipToAddress2;

    private String shipToZip;

    private String panNo;

    private Boolean isActive;

    private Long billToCityId;

    private Long billToStateId;

    private String billToStateName;

    private String billToCityName;


    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        if(null!= client){
            setId ( client.getId () );
            setClientId ( client.getClientId () );
            setBillToAddress1 ( client.getBillToAddress1() );
            setBillToAddress2 ( client.getBillToAddress2());
            setAlternateName ( client.getAlternateName () );
            setBillToCityId ( client.getBillToCity().getId () );
            setBillToCityName ( client.getBillToCity().getCityName () );
            setBillToStateId ( client.getBillToState().getId () );
            setBillToStateName ( client.getBillToState ().getStateName () );
            setShipToCityId ( client.getShipToCity().getId () );
            setShipToCityName ( client.getShipToCity ().getCityName () );
            setShipToStateId ( client.getShipToState().getId () );
            setShipToStateName ( client.getShipToState().getStateName () );
            setContactNo ( client.getContactNo () );
            setCountryId ( client.getCountry ().getId () );
            setCountryName ( client.getCountry ().getCountryName () );
            setEmail ( client.getEmail () );
            setContactPersonFirstName ( client.getContactPersonFirstName () );
            setContactPersonLastName ( client.getContactPersonLastName () );
            setFaxNumber ( client.getFaxNumber () );
            setGstin ( client.getGstin () );
            setFirstName ( client.getFirstName () );
            setLastName ( client.getLastName () );
            setIsActive ( client.getIsActive () );
            setPaymentMode ( client.getPaymentMode ().getValue ());
            setPaymentModeId ( client.getPaymentMode ().getId () );
            setBillToZip ( client.getBillToZip () );
            setWebsite ( client.getWebsite () );
            setUpdatedOn ( client.getUpdatedOn () );
            setClientTypeId ( client.getClientType ().getId () );
            setClientTypeName ( client.getClientType ().getValue ());
            setClientTypeValue ( client.getClientType ().getDataType ());
            setShipToAddress1(client.getShipToAddress1());
            setShipToAddress2(client.getShipToAddress2());
            setShipToZip(client.getShipToZip());
            setPanNo(client.getPanNo());

        }
    }


}
