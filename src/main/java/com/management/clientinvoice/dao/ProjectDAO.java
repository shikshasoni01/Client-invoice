package com.biz4solutions.clientinvoice.dao;


import com.biz4solutions.clientinvoice.domain.Projects;
import com.biz4solutions.clientinvoice.dto.ProjectDTO;
import com.biz4solutions.clientinvoice.repository.ProjectRepository;
import com.biz4solutions.clientinvoice.requestWrapper.ProjectFilterRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Component
public class ProjectDAO {

    private static final Logger LOGGER = Logger.getLogger(ProjectDAO.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    public Page<Projects>  findAllSearchTextContainingIgnoreCase(ProjectFilterRequestWrapper request, Pageable pageable,String filter, String sort){

        StringBuilder selectQuery1 = new StringBuilder();

        selectQuery1.append("select  p.* from projects p inner join client c on p.client_id = c.id inner join user_identity u on p.user_identity_id = u.id ");



        if(!StringUtils.isEmpty(request != null)){

            if(!request.getProjectName().isEmpty()){
                selectQuery1.append("AND LOWER(p.project_name) LIKE LOWER('%" + request.getProjectName() + "%')");
            } else if(!request.getProjectManagerFirstName ().isEmpty()){
                selectQuery1.append("AND LOWER(u.first_name) LIKE LOWER('%" + request.getProjectManagerFirstName () + "%')");
            } else if(!request.getProjectManagerLastName ().isEmpty()){
                selectQuery1.append("AND LOWER(u.last_name) LIKE LOWER('%" + request.getProjectManagerLastName () + "%')");
            } else if(!request.getClientFirstName ().isEmpty()){
                selectQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getClientFirstName () + "%')");
            } else if(!request.getClientLastName ().isEmpty()){
                selectQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getClientLastName () + "%')");
            } else if(null != request.getProjectsStartDateFrom () && null != request.getProjectsStartDateTo ()){
                selectQuery1.append("AND (p.start_date >= '" +request.getProjectsStartDateFrom ()+"' AND p.start_date <= '"+ request.getProjectsStartDateTo () +"')");
            } else if(null != request.getIsActive()) {
                selectQuery1.append("AND p.is_active = " + request.getIsActive() + " ");
            }

            if(StringUtils.isEmpty(request) && !"ALL".equalsIgnoreCase(filter)){
                selectQuery1.append(" where ");
            } else if (!"ALL".equalsIgnoreCase(filter)) {
                selectQuery1.append(" and ");
            }

            if(sort.equalsIgnoreCase("PROJECTNAME")){
                selectQuery1.append("order by p.project_name ASC");
            } else if(sort.equalsIgnoreCase("PROJECTFIRSTMANAGER")){
                selectQuery1.append("order by u.first_name ASC");
            } else if(sort.equalsIgnoreCase("PROJECTLASTNAME")){
                selectQuery1.append("order by u.last_name ASC");
            } else if(sort.equalsIgnoreCase("CLIENTFIRSTNAME")){
                selectQuery1.append("order by c.first_name ASC");
            } else if(sort.equalsIgnoreCase("CLIENTLASTNAME")){
                selectQuery1.append("order by c.last_name ASC");
            } else if(sort.equalsIgnoreCase("ISACTIVE")){
                selectQuery1.append("order by p.is_active ASC");
            }
        }

        String selectQuery = selectQuery1.toString();

        StringBuilder countQuery1 = new StringBuilder();

        countQuery1.append("select DISTINCT count(p.*) from projects p where p.is_active = true ");

        String countQuery = countQuery1.toString();

        List<Projects> projectsList =entityManager.createNativeQuery(selectQuery, Projects.class)
                .setFirstResult((pageable.getPageNumber() * pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize()).getResultList();

        BigInteger count = (BigInteger) entityManager.createNativeQuery(countQuery).getSingleResult();

        long totalSwipeMatchCount = 0l;
        if(count != null)
            totalSwipeMatchCount = count.longValue();

        Page<Projects> projects = new PageImpl<>(projectsList, pageable, totalSwipeMatchCount);

        return projects;
    }

}
