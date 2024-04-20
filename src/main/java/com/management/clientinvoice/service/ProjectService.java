package com.biz4solutions.clientinvoice.service;


import com.biz4solutions.clientinvoice.dto.PaginationDTO;
import com.biz4solutions.clientinvoice.dto.ProjectDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;


@Service
public interface ProjectService {

    List<ProjectDTO> getAllProject();

    void createProject(ProjectRequestWrapper request,String acceptLanguage) throws InvoiceManagementException, MessagingException;

    ProjectDTO getASingleProject(Long id);

    void updateProject(UpdateProjectRequestWrapper request, String acceptLanguage);

    void enableProject(Long projectId, String acceptLanguage) throws InvoiceManagementException;

    void deleteProject(Long projectId, String acceptLanguage) throws InvoiceManagementException;

    PaginationDTO<ProjectDTO> getAllProjectList(Pageable pageable, ProjectFilterRequestWrapper request, String filter, String sort, String order) throws InvoiceManagementException;


}
