package com.biz4solutions.clientinvoice.dao;

import com.biz4solutions.clientinvoice.domain.Client;
import com.biz4solutions.clientinvoice.repository.ClientRepository;
import com.biz4solutions.clientinvoice.requestWrapper.ClientFilterRequestWrapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

@Component
public class ClientDAO {

    private static final Logger LOGGER = Logger.getLogger(ClientDAO.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    public Page<Client> findAllSearchTextContainingIgnoreCase(ClientFilterRequestWrapper request, Pageable pageable, String filter,String sort){

        StringBuilder selectQuery1 = new StringBuilder();

        //select distinct
        selectQuery1.append("select  c.* from client c inner join country co on c.country_id = co.id ");

        //where search query
        if(!StringUtils.isEmpty(request != null)) {

            if (!request.getContactPersonFirstName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.contact_person_first_name) LIKE LOWER('%" + request.getContactPersonFirstName() + "%')");
            } else if(!request.getContactPersonLastName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.contact_person_last_name) LIKE LOWER('%" + request.getContactPersonLastName() + "%')");
            } else if(!request.getFirstName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getFirstName() + "%')");
            } else if(!request.getLastName().isEmpty() ) {
                selectQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getLastName() + "%')");
            } else if(!request.getEmail().isEmpty()) {
                selectQuery1.append("AND c.email LIKE '%" + request.getEmail() + "%'");
            } else if(!request.getCountryName().isEmpty()) {
                selectQuery1.append("AND LOWER(co.country_name) LIKE LOWER('%" + request.getCountryName() + "%')");
            } else if(null != request.getIsActive()){
                selectQuery1.append("AND c.is_active = " +request.getIsActive()+" ");
            }


            if(StringUtils.isEmpty(request) && !"ALL".equalsIgnoreCase(filter)){
                selectQuery1.append(" where ");
            } else if(!"ALL".equalsIgnoreCase(filter)){
                selectQuery1.append(" and ");
            }

            if(sort.equalsIgnoreCase("FIRSTNAME")) {
                selectQuery1.append("order by c.first_name ASC");
            } else if(sort.equalsIgnoreCase("LASTNAME")) {
                selectQuery1.append("order by c.last_name ASC");
            } else if(sort.equalsIgnoreCase("CONTACTPERSONFIRSTNAME")) {
                selectQuery1.append("order by c.contact_person_first_name ASC");
            } else if(sort.equalsIgnoreCase("CONTACTPERSONLASTNAME")) {
                selectQuery1.append("order by c.contact_person_last_name ASC");
            } else if(sort.equalsIgnoreCase("ISACTIVE")) {
                selectQuery1.append("order by c.is_active ASC");
            } else if(sort.equalsIgnoreCase("EMAIL")) {
                selectQuery1.append("order by c.email ASC");
            } else if(sort.equalsIgnoreCase("COUNTRYNAME")) {
                selectQuery1.append("order by co.country_name ASC");
            }
//            else if (sort.equalsIgnoreCase ( "CLIENTTYPE" )){
//                selectQuery1.append ( "order by c.client_type ASC " );
//            }
        }

            String selectQuery = selectQuery1.toString();

            StringBuilder countQuery1 = new StringBuilder();

            countQuery1.append("select DISTINCT count(c.*) from client c where c.is_active = true ");

            String countQuery = countQuery1.toString();

            List<Client> clientList =entityManager.createNativeQuery(selectQuery, Client.class)
                    .setFirstResult((pageable.getPageNumber() * pageable.getPageSize()))
                    .setMaxResults(pageable.getPageSize()).getResultList();

            BigInteger count = (BigInteger) entityManager.createNativeQuery(countQuery)
                    .getSingleResult();


            long totalSwipeMatchCount = 0l;
            if(count != null)
                totalSwipeMatchCount = count.longValue();

            // give `client` list ,count with pagination
            Page<Client> clients = new PageImpl<>(clientList, pageable, totalSwipeMatchCount);

            return clients;

    }

}
