package com.biz4solutions.clientinvoice.dto;


import com.biz4solutions.clientinvoice.domain.Projects;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class ProjectDTO {

    private Long id;

    private Long clientId;

    private String clientFirstName;

    private String clientLastName;

    private String projectName;

    private String description;

    private Date startDate;

    private Long projectManagerId;

    private String projectManagerFirstName;

    private String projectManagerLastName;

    private Boolean isActive;

    private String poNo;

    private Long projectLocationId;

    private String projectArea;

    private String projectCity;

    public ProjectDTO() {
    }

    public ProjectDTO(Projects projects) {

        if(null != projects){
            setId(projects.getId());
            setDescription(projects.getDescription());
            setProjectName ( projects.getProjectName () );
            setProjectManagerId(projects.getUserIdentity().getId());
            setClientId(projects.getClient ().getId());
            setStartDate(projects.getStartDate());
            setIsActive(projects.getIsActive());
            setProjectName(projects.getProjectName());
            setProjectManagerFirstName (projects.getUserIdentity().getFirstName());
            setProjectManagerLastName (projects.getUserIdentity().getLastName());
            setClientFirstName (projects.getClient().getFirstName());
            setClientLastName (projects.getClient().getLastName());
            setPoNo(projects.getPoNo());
            if(projects.getLocation() != null) {
                setProjectLocationId(projects.getLocation().getId());
            }else{
                setProjectLocationId(null);
            }
            if(projects.getLocation() != null) {
                setProjectArea(projects.getLocation().getArea());
            }else{
                setProjectArea(null);
            }
            if(projects.getLocation() != null) {
                setProjectCity(projects.getLocation().getCity());
            }else{
                setProjectCity(null);
            }
        }
    }

}

