package com.biz4solutions.clientinvoice.domain;



import com.biz4solutions.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="state")
public class State extends BaseEntity {


    private static final long serialVersionUID = -4029776626105035205L;
    @Id
    @Column(name="state_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="state_name",length = DBConstants.STATE_MAX_CHAR_LIMIT)
    private String stateName;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

}
