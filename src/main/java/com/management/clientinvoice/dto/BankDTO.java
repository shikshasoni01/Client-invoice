package com.biz4solutions.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDTO {
    private Long id;
    private String bankName;
    private String bankAccountNumber;
    private String swiftCode;
    private String accountName;
    private String branch;
    private String IFSCCode;
    private String companyAccountNumber;
    private String wireRoutingNumber;
    private String achRoutingNumber;
    private String bankType;
    private String payToSwiftCode;
    private String creditToSwiftCode;
    private String accountNumberForBeneficiary;
    private String accountNumberForCredit;
    private String payToBankName;
}
