package com.biz4solutions.clientinvoice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website")
    private String website;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="zip")
    private String zip;

    @Column(name="country")
    private String country;

    @Column(name = "pan_no")
    private String panNumber;

    @Column(name = "gstin")
    private String gstin;

    @Column(name = "ein")
    private String ein;

    @Column(name = "company_type")
    private String companyType;



}
