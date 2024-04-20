package com.biz4solutions.clientinvoice.domain;


import com.biz4solutions.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name="payment_mode")
public class PaymentMode extends BaseEntity {

    private static final long serialVersionUID = -6090904966074263579L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="data_type",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String dataType;

    @Column(name="value",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String value;

}
