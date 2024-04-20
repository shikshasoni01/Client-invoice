package com.biz4solutions.clientinvoice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "bank")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "pay_to_swift_code")
    private String payToSwiftCode;

    @Column(name = "credit_to_swift_code")
    private String creditToSwiftCode;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "branch")
    private String branch;

    @Column(name = "ifsc_code")
    private String IFSCCode;

    @Column(name = "account_number_beneficiary")
    private String accountNumberForBeneficiary;

    @Column(name = "account_number_credit")
    private String accountNumberForCredit;

    @Column(name = "wire_routing_number")
    private String wireRoutingNumber;

    @Column(name = "ach_routing_number")
    private String achRoutingNumber;

    @Column(name = "bank_type")
    private String bankType;

    @Column(name ="pay_to_bank_name")
    private String payToBankName;


}
