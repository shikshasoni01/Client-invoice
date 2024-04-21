package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;


@Getter
@Setter
@Entity
@Table(name="projects")
@Where(clause = "is_active = true")
public class Projects extends BaseEntity {

    private static final long serialVersionUID = 6619854894829170926L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name="project_name",length = DBConstants.PROJECT_NAME_MAX_LIMIT)
    private String projectName;

    @Column(name="description",length = DBConstants.PROJECT_DESCRIPTION_MAX_LIMIT)
    private  String description;

    @Column(name = "project_manager",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String ProjectManager;

    @NotNull(message = "startDateNotBlank")
    @Column(name="start_date")
    private Date startDate;

    @ManyToOne
    private UserIdentity userIdentity;

    @ManyToOne
    private Client client;

    @Column(name = "po_no")
    private String poNo;

    @OneToOne
    private Location location;

}

