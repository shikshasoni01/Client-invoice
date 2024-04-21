package com.management.clientinvoice.domain;

import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name="payment_status")
public class PaymentStatus extends BaseEntity{

    private static final long serialVersionUID = 179584475031589989L;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_type",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String dataType;

    @NotNull
    @Column(name = "value",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String value;

}
