package com.biz4solutions.clientinvoice.dto;


import com.biz4solutions.clientinvoice.domain.Country;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StateDTO {

    private Long id;

    private String stateName;

    private Long countryId;

}
