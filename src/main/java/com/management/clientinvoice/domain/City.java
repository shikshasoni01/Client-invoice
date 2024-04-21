package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Entity
@Table(name="city")
public class City extends BaseEntity {

    private static final long serialVersionUID = -4321086409696183217L;

    @Id
    @Column(name="city_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="city_name",length = DBConstants.CITY_MAX_CHAR_LIMIT)
    private String cityName;

    @ManyToOne
    @JoinColumn(name="state_id")
    private State state;

}

