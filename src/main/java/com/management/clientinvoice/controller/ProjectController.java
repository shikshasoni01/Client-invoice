package com.management.clientinvoice.controller;


import com.management.clientinvoice.constant.UrlConstant;
import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.dto.PaginationDTO;
import com.management.clientinvoice.dto.ProjectDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.ProjectRepository;
import com.management.clientinvoice.service.ICommonService;
import com.management.clientinvoice.service.ProjectService;
import com.management.clientinvoice.util.ResponseFormatter;
import com.management.clientinvoice.requestWrapper.ProjectFilterRequestWrapper;
import com.management.clientinvoice.requestWrapper.ProjectRequestWrapper;
import com.management.clientinvoice.requestWrapper.UpdateProjectRequestWrapper;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/v1/user/project")
public class ProjectController {

    private static final Logger LOGGER = Logger.getLogger(ProjectController.class);

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MessageSource messageSource;


    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getProjectById(@PathVariable("id") Long id,
                                                         @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
                                                         @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
    ) throws InvoiceManagementException {

        LOGGER.info("get all project by id Start");

        ProjectDTO project = projectService.getASingleProject(id);
        String message = messageSource.getMessage("projectGetSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, project);

        LOGGER.info("get all project by id end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value =  "/getAllProject", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getAllProject(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                        @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws InvoiceManagementException {

        LOGGER.info("get all project Start");

        List<ProjectDTO> projectDTO = projectService.getAllProject();
        String message = messageSource.getMessage("allProjectGetSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message,projectDTO);

        LOGGER.info("get all project end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> addProject( @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                      @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
                                                      @RequestBody ProjectRequestWrapper request
    ) throws InvoiceManagementException , MessagingException {

        LOGGER.info("add project Start");

        projectService.createProject(request,acceptLanguage);
        String message = messageSource.getMessage("ProjectCreatedSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("add project end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }




    @RequestMapping(value = "/delete/project", method = RequestMethod.PUT)
    public ResponseEntity<JSONObject> deleteProject(@RequestParam Long id,
                                                        @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage,
                                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
    ) throws  InvoiceManagementException {

        LOGGER.info("delete project Start");

        projectService.deleteProject(id, acceptLanguage);
        String message = messageSource.getMessage("projectDeletedSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("delete project end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = "/enable/project", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> enableProject(@RequestParam Long id,
                                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                        @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws  InvoiceManagementException {

        LOGGER.info("enable project Start");

        projectService.enableProject(id, acceptLanguage);
        String message = messageSource.getMessage("projectEnableSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("enable project end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = UrlConstant.UPDATE_OPERATION, method = RequestMethod.PUT)
    public ResponseEntity<JSONObject> updateProject(@RequestBody UpdateProjectRequestWrapper request,
                                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                        @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws InvoiceManagementException {

        LOGGER.info("update project Start");

        projectService.updateProject(request, acceptLanguage);
        String message = commonService.getMessage("projectUpdatedSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("update project end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> getAllProjectList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                            Pageable pageable,
                                                            @RequestBody ProjectFilterRequestWrapper request,
                                                            @RequestParam(required = false, defaultValue = "ALL") String filter,
                                                            @RequestParam(required = false, defaultValue = "PROJECTNAME") String sort, //NAME
                                                            @RequestParam(required = false, defaultValue = "ASC") String order
    ) throws InvoiceManagementException {

        LOGGER.info("filter project Start");

        PaginationDTO<ProjectDTO> list = projectService.getAllProjectList(pageable, request, filter, sort, order);
        String message = commonService.getMessage("listRetrievedSuccessfully");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, list);

        LOGGER.info("filter project end");

        return new ResponseEntity<>(data, HttpStatus.OK);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JSONObject> processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex
    ) throws InvoiceManagementException {

        BindingResult result = ex.getBindingResult();
        String localizedErrorMessage = "";
        String lang = req.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);

        if (lang == null || lang.isEmpty()) {
            lang = "en";
        }

        if (!result.getAllErrors().isEmpty()) {
            localizedErrorMessage = result.getAllErrors().get(0).getDefaultMessage();

        }
        localizedErrorMessage = messageSource.getMessage(localizedErrorMessage, null, new Locale(lang));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, localizedErrorMessage);

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }


}





//
//    @GetMapping("/project")
//    public ResponseEntity<List<Projects>> getProjects() {
//        try {
//            return new ResponseEntity<>(projectRepository.findAll(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/project")
//    public ResponseEntity<Projects> newProjects(@RequestBody Projects projects) {
////        Projects newProjects= projectRepository
////                .save(Projects.builder()
////                        .projectName(projects.getprojectName())
////                        .description(projects.getDescription())
////                        .build());
////        return new ResponseEntity<>(newProjects, HttpStatus.OK);
//        return null;
//    }
//
//    @GetMapping("/project/{id}")
//    public ResponseEntity<Projects> getProjectsById(@PathVariable("id") long id) {
//        try {
//            //check if invoice exist in database
//            Projects proObj= getProRec(id);
//
//            if (proObj != null) {
//                return new ResponseEntity<>(proObj, HttpStatus.OK);
//            }
//
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//    private Projects getProRec(long id) {
//        Optional<Projects> proObj = projectRepository.findById(id);
//
//        if (proObj.isPresent()) {
//            return proObj.get();
//        }
//        return null;
//    }
//
//    @PutMapping("/project/{id}")
//    public ResponseEntity<Projects> updateProject(@PathVariable("id") long id, @RequestBody Projects projects) {
//
//        //check if projects exist in database
//        Projects proObj= getProRec(id);
//
//        if (proObj != null) {
//            proObj.setClients(projects.getClients());
//            proObj.setstartDate(projects.getstartDate());
//            proObj.setprojectName(projects.getprojectName());
//            proObj.setDescription(projects.getDescription());
//            proObj.setUsers(projects.getUsers());
//            proObj.setIsActive(projects.getIsActive());
//            return new ResponseEntity<>(projectRepository.save(proObj), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @PutMapping(path="deleteProject")
//    void deleteInactive(List<String> projectList){
//        try{
//            projectService.deleteInactive(projectList);
//        }catch (MyResourceNotFoundException exc) {
//            System.out.println("Exception found " + exc.getMessage());
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "project not deleted", exc);
//        }
//    }
//
//
//    @RequestMapping("filterByClient")
//    public List<Projects> getProjectsByClient(String client_id) {
//        try {
//
//            return projectService.getProjectsByClient(client_id);
//
//        } catch (MyResourceNotFoundException exc) {
//            System.out.println("Exception found " + exc.getMessage());
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found", exc);
//        }
//    }

