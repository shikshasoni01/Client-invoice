package com.biz4solutions.clientinvoice.domain;


import com.biz4solutions.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import javax.validation.constraints.Size;



@Getter
@Setter
@Entity
@Table(name="country")
public class Country extends BaseEntity {

    private static final long serialVersionUID = -2397464882736634183L;

    @Id
    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "country_name")
    @Size(min=DBConstants.COUNTRY_MIN_CHAR_LIMIT,max = DBConstants.COUNTRY_MAX_CHAR_LIMIT)
    private String countryName;

    @Column(name = "country_code")
    @Size(max = DBConstants.COUNTRY_CODE_MAX_CHAR_LIMIT)
    private String countryCode;

    @Column(name = "currency_name",length = DBConstants.CURRENCY_MAX_LIMIT)
    private String currencyName;

    @Column(name = "currency_code",length = DBConstants.CURRENCY_CODE_MAX_LIMIT)
    private String currencyCode;

}