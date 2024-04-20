package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class ProjectFilterRequestWrapper {


    private String projectName;

    private String clientFirstName;

    private String clientLastName;

    private String projectManagerFirstName;

    private String projectManagerLastName;

    private Boolean isActive;

    private Date projectsStartDateFrom;

    private Date projectsStartDateTo;
}
