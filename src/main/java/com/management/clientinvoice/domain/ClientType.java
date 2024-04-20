package com.biz4solutions.clientinvoice.domain;

import com.biz4solutions.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name="client_type")
public class ClientType extends BaseEntity {
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
