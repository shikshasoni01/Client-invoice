package com.management.clientinvoice.service.Impl;



import com.management.clientinvoice.dao.ProjectDAO;
import com.management.clientinvoice.domain.*;
import com.management.clientinvoice.dto.PaginationDTO;
import com.management.clientinvoice.dto.ProjectDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.*;
import com.management.clientinvoice.requestWrapper.ProjectFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.ProjectRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateProjectRequestWrapper;
import com.management.clientinvoice.service.EmailService;
import com.management.clientinvoice.service.ICommonService;
import com.management.clientinvoice.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.util.*;


@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<ProjectDTO> getAllProject() {

        List<Projects> projects = projectRepository.findAllByIsActive();

        List<ProjectDTO> projectDTOS = new ArrayList<>();

        for(Projects projects1 : projects) {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(projects1.getId());
            projectDTO.setProjectName ( projects1.getProjectName ( ) );
            projectDTO.setClientId(projects1.getClient ().getId());
            projectDTO.setClientFirstName ( projects1.getClient ( ).getFirstName () );
            projectDTO.setClientLastName ( projects1.getClient ().getLastName () );
            projectDTO.setProjectManagerId(projects1.getUserIdentity ().getId ());
            projectDTO.setDescription ( projects1.getDescription () );
            projectDTO.setProjectManagerFirstName ( projects1.getUserIdentity ().getFirstName () );
            projectDTO.setProjectManagerLastName ( projects1.getUserIdentity ().getLastName () );
            projectDTO.setStartDate(projects1.getStartDate());
            projectDTO.setIsActive(projects1.getIsActive());
            projectDTO.setPoNo(projects1.getPoNo());
            if(projects1.getLocation() == null) {
                projectDTO.setProjectArea(null);
            }else{
                projectDTO.setProjectArea(projects1.getLocation().getArea());
            }
            if(projects1.getLocation() == null) {
                projectDTO.setProjectCity(null);
            }else{
                projectDTO.setProjectCity(projects1.getLocation().getCity());
            }

            projectDTOS.add(projectDTO);
        }
        return projectDTOS;
    }

    @Override
    public void createProject(ProjectRequestWrapper request, String acceptLanguage) throws InvoiceManagementException, MessagingException {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }

        Projects newProject = new Projects();
        Projects projects = projectRepository.findOneByProjectNameIgnoreCase(request.getProjectName());
        if(projects == null) {
            newProject.setProjectName(request.getProjectName());
            newProject.setDescription(request.getDescription());
            newProject.setStartDate(request.getStartDate());
            newProject.setPoNo(request.getPoNo());
            newProject.setCreatedBy(loggedInUser.getId().toString());



            Client client =  clientRepository.findById ( request.getClientId ( ) ) ;
            if(client!=null) {
                newProject.setClient( client );
            }else{
                throw new InvoiceManagementException(
                        messageSource.getMessage("clientIdNotFound", null, new Locale(acceptLanguage)), 400);
            }

            Optional<UserIdentity> userIdentity = Optional.ofNullable(userIdentityRepository.findOneById(request.getProjectManagerId()));
            if(userIdentity.isPresent()) {
                newProject.setUserIdentity(userIdentity.get());
            }else{
                newProject.setLocation(null);
                throw new InvoiceManagementException(
                        messageSource.getMessage("projectManagerNotFound", null, new Locale(acceptLanguage)), 400);
            }

            if(request.getProjectLocationId() != null) {
                Optional<Location> location = locationRepository.findById(request.getProjectLocationId());

            if(location.isPresent()) {
                newProject.setLocation(location.get());
            }}else{
                newProject.setLocation(null);
//                throw new InvoiceManagementException(
//                        messageSource.getMessage("projectLocationNotFound", null, new Locale(acceptLanguage)), 400);
            }


            projectRepository.save(newProject);
        }else{
            throw new InvoiceManagementException(messageSource.getMessage("projectAlreadyExists", null, new Locale(acceptLanguage)),
                    500);
        }
//        emailService.sendProjectCreateMail(newProject.getProjectName(),newProject.getUserIdentity().getFirstName());

    }

    @Override
    public ProjectDTO getASingleProject(Long id) {

        Projects projects = projectRepository.findOneByIdIgnoreIsActive(id);
        if(projects != null) {
            return new ProjectDTO(projects);
        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("projectNotFound", null, new Locale("en")), 400);
        }
    }
    @Override
    public void updateProject(UpdateProjectRequestWrapper request, String acceptLanguage) {

        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }

        Projects projects =projectRepository.findOneByIdIgnoreIsActive(request.getId());
        if(projects != null) {

            projects.setProjectName(request.getProjectName());
            projects.setStartDate(request.getStartDate());
            projects.setDescription(request.getDescription());
            projects.setIsActive(request.getIsActive());
            projects.setIsDelete(!request.getIsActive());
            projects.setPoNo(request.getPoNo());
            projects.setUpdatedBy(loggedInUser.getId().toString());



            Client client = clientRepository.findById (request.getClientId());
            if (client!=null) {
                projects.setClient ( client );

            } else {
                throw new InvoiceManagementException(
                        messageSource.getMessage("clientNotFound", null, new Locale(acceptLanguage)), 400);
            }

//            if(!request.getProjectLocationId().equals(null)){
            Optional<Location> location = locationRepository.findById(request.getProjectLocationId());

            if(location.isPresent()) {
                projects.setLocation(location.get());
//            }
        }else{
                projects.setLocation(null);
//                throw new InvoiceManagementException(
//                        messageSource.getMessage("projectLocationNotFound", null, new Locale(acceptLanguage)), 400);
            }

            Optional<UserIdentity> userIdentity = Optional.ofNullable(userIdentityRepository.findOneById((request.getProjectManagerId())));
            if (userIdentity.isPresent()) {
                projects.setUserIdentity(userIdentity.get());
            } else if(projects == null){
                throw new InvoiceManagementException(
                        messageSource.getMessage("projectNotFound", null, new Locale(acceptLanguage)), 400);

            }else{
                throw new InvoiceManagementException(
                        messageSource.getMessage("userNotFound", null, new Locale(acceptLanguage)), 400);
            }

        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("projectIdNotFound", null, new Locale(acceptLanguage)), 400);



        }

        projectRepository.save(projects);
    }

    @Override
    public void enableProject(Long projectId, String acceptLanguage) throws InvoiceManagementException {


        Projects projects = projectRepository.findOneByIdIgnoreIsActive(projectId);
        if (projects == null) {
            throw new InvoiceManagementException("projectNotFound");
        }

        if (!projects.getIsActive()) {
            projects.setIsActive(true);

            projectRepository.save(projects);
        }
    }

    @Override
    public void deleteProject(Long projectId, String acceptLanguage) throws InvoiceManagementException {
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        if (loggedInUser == null) {
            throw new InvoiceManagementException (commonService.getMessageFromDatabase("userNotFound"), 400);
        }
        Projects project = projectRepository.findOneByIdIgnoreIsActive(projectId);
        List<Invoice> invoice = invoiceRepository.findByProject(project);


        if(project != null && invoice.isEmpty()) {
            project.setIsActive(false);
            project.setIsDelete(true);
            project.setDeletedAt(new Date());
            project.setDeletedBy(loggedInUser.getId().toString());
            projectRepository.save(project);
        }else if(invoice != null){

            throw new InvoiceManagementException(
                    messageSource.getMessage("InvoiceNotDeleted", null, new Locale(acceptLanguage)), 400);

        }else{
            throw new InvoiceManagementException(
                    messageSource.getMessage("projectNotFound", null, new Locale(acceptLanguage)), 400);

        }
    }

    @Override
    public PaginationDTO<ProjectDTO> getAllProjectList(Pageable pageable, ProjectFilterRequestWrapper request, String filter, String sort, String order) throws InvoiceManagementException {
        Page<Projects> projectsPage = Page.empty();
        projectsPage = projectDAO.findAllSearchTextContainingIgnoreCase(request, pageable, filter,sort);
        List<Projects> projectsList = projectsPage.getContent();
        List<ProjectDTO> projectDTOList = new ArrayList<>();


        if(!CollectionUtils.isEmpty(projectsList)){
            for (Projects projects : projectsList){
                ProjectDTO projectDTO = new ProjectDTO();

                projectDTO.setId(projects.getId());
                projectDTO.setClientId ( projects.getClient ().getId() );
                projectDTO.setProjectName(projects.getProjectName());
                projectDTO.setDescription(projects.getDescription());
                projectDTO.setProjectManagerId(projects.getUserIdentity().getId());
                projectDTO.setClientFirstName (projects.getClient().getFirstName());
                projectDTO.setClientLastName (projects.getClient().getLastName ());
                projectDTO.setProjectManagerFirstName (projects.getUserIdentity().getFirstName());
                projectDTO.setProjectManagerLastName (projects.getUserIdentity().getLastName ());
                projectDTO.setStartDate(projects.getStartDate());
                projectDTO.setIsActive(projects.getIsActive());
                projectDTO.setPoNo(projects.getPoNo());
                if(projects.getLocation() ==null) {
                    projectDTO.setProjectArea(null);
                }else{
                    projectDTO.setProjectArea(projects.getLocation().getArea());
                }
                if(projects.getLocation() == null) {
                    projectDTO.setProjectCity(null);
                }else{
                    projectDTO.setProjectCity(projects.getLocation().getCity());

                }

                projectDTOList.add(projectDTO);

            }
        }

        PaginationDTO<ProjectDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setList(projectDTOList);
        paginationDTO.setTotalCount(projectsPage.getTotalElements());
        paginationDTO.setTotalPages(projectsPage.getTotalPages());
        return paginationDTO;

    }

}
