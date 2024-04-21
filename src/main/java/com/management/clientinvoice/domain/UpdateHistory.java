package com.management.clientinvoice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter@Getter
@Entity
@Table(name = "update_history")
public class UpdateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;

    private String previousInvoiceNumber;

    private String updatedInvoiceNumber;

    private Long userId;

    private Date updatedOn;
}
