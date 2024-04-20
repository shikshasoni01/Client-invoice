package com.biz4solutions.clientinvoice.dao;


import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.dto.InvoiceDTO;
import com.biz4solutions.clientinvoice.repository.InvoiceRepository;
import com.biz4solutions.clientinvoice.requestWrapper.InvoiceFilterRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
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
public class InvoiceDAO {

    private static final Logger LOGGER = Logger.getLogger(InvoiceDAO.class);


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ICommonService commonService;

    public Page<Invoice> findAllSearchTextContainingIgnoreCase(InvoiceFilterRequestWrapper request, Pageable pageable, String filter, String sort) {

        StringBuilder selectQuery1 = new StringBuilder();

        selectQuery1.append("select i.* from invoice i inner join projects p on  i.project_id = p.id  inner join client c on p.client_id=c.id inner join payment_status ps on i.payment_status_id = ps.id AND i.is_active = true ");

        if (!StringUtils.isEmpty(request != null)) {

            if (!request.getClientFirstName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getClientFirstName () + "%')");
            } else if (!request.getClientLastName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getClientLastName () + "%')");
            } else if (!request.getProjectName().isEmpty()) {
                selectQuery1.append("AND LOWER(p.project_name) LIKE LOWER('%" + request.getProjectName() + "%')");
            } else if(request.getPaymentStatus() != null){
                selectQuery1.append("AND i.payment_status_id = '"+ request.getPaymentStatus() +"'");
            }
            else if (null != request.getInvoiceDateFrom() && null != request.getInvoiceDateTo()) {
                selectQuery1.append("AND (i.invoice_date >= '" +request.getInvoiceDateFrom()+"' AND i.invoice_date <= '"+request.getInvoiceDateTo() +"')");
            } else if (null != request.getDueDateFrom() && null != request.getDueDateTo()) {
                selectQuery1.append("AND (i.due_date >= '" +request.getDueDateFrom()+"' AND i.due_date <= '"+request.getDueDateTo() +"')");
            } else if (null != request.getPaidDateFrom() && null != request.getPaidDateTo()) {
                selectQuery1.append("AND (i.paid_date >= '" +request.getPaidDateFrom()+"' AND i.paid_date <= '"+request.getPaidDateTo() +"')");
            }


            if(StringUtils.isEmpty(request) && !"ALL".equalsIgnoreCase(filter)){
                selectQuery1.append(" where ");
            } else if (!"ALL".equalsIgnoreCase(filter)) {
                selectQuery1.append(" and ");
            }
            if (sort.equalsIgnoreCase("FIRSTNAME")) {
                selectQuery1.append("order by c.first_name ASC, i.id DESC");

            } else if (sort.equalsIgnoreCase("LASTNAME")) {
                selectQuery1.append("order by c.last_name ASC, i.id DESC");
            } else if (sort.equalsIgnoreCase("ISACTIVE")) {
                selectQuery1.append("order by i.is_active ASC, i.id DESC");
            } else if (sort.equalsIgnoreCase("PROJECTNAME")) {
                selectQuery1.append("order by p.project_name ASC, i.id DESC");
            } else if (sort.equalsIgnoreCase("ID")){
                selectQuery1.append("order by i.id ASC");
            }
            else if (sort.equalsIgnoreCase("INVOICEDATE")){
                selectQuery1.append("order by i.invoice_date DESC, i.id DESC");
            }
        }

        String selectQuery = selectQuery1.toString();

        StringBuilder countQuery1 = new StringBuilder();

//        countQuery1.append("select DISTINCT count(i.*) from invoice i where i.is_active= true ");
        countQuery1.append("select count(i.*) from invoice i inner join projects p on  i.project_id = p.id  inner join client c on p.client_id=c.id inner join payment_status ps on i.payment_status_id = ps.id AND i.is_active = true ");

        if (!StringUtils.isEmpty(request != null)) {

            if (!request.getClientFirstName().isEmpty()) {
                countQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getClientFirstName() + "%')");
            } else if (!request.getClientLastName().isEmpty()) {
                countQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getClientLastName() + "%')");
            } else if (!request.getProjectName().isEmpty()) {
                countQuery1.append("AND LOWER(p.project_name) LIKE LOWER('%" + request.getProjectName() + "%')");
            } else if (request.getPaymentStatus() != null) {
                countQuery1.append("AND i.payment_status_id = '" + request.getPaymentStatus() + "'");
            } else if (null != request.getInvoiceDateFrom() && null != request.getInvoiceDateTo()) {
                countQuery1.append("AND (i.invoice_date >= '" + request.getInvoiceDateFrom() + "' AND i.invoice_date <= '" + request.getInvoiceDateTo() + "')");
            } else if (null != request.getDueDateFrom() && null != request.getDueDateTo()) {
                countQuery1.append("AND (i.due_date >= '" + request.getDueDateFrom() + "' AND i.due_date <= '" + request.getDueDateTo() + "')");
            } else if (null != request.getPaidDateFrom() && null != request.getPaidDateTo()) {
                countQuery1.append("AND (i.paid_date >= '" + request.getPaidDateFrom() + "' AND i.paid_date <= '" + request.getPaidDateTo() + "')");
            }
        }

        String countQuery = countQuery1.toString();

        List<Invoice> invoiceList =entityManager.createNativeQuery(selectQuery, Invoice.class)
                .setFirstResult((pageable.getPageNumber() * pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize()).getResultList();

        BigInteger count = (BigInteger) entityManager.createNativeQuery(countQuery)
                .getSingleResult();

        System.out.println("count "+count);

        long totalSwipeMatchCount = 0l;
        if(count != null)
            totalSwipeMatchCount = count.longValue();

        Page<Invoice> invoices = new PageImpl<>(invoiceList, pageable, totalSwipeMatchCount);

        return invoices;
    }


    public Page<Invoice> findAllSearchTextContainingIgnoreCaseAndRoleTypeManager(InvoiceFilterRequestWrapper request, Pageable pageable, String filter, String sort) {

        StringBuilder selectQuery1 = new StringBuilder();
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(commonService.getLanguageCode());
        System.out.println("logged in user id "+loggedInUser.getId().toString());
        selectQuery1.append("select i.* from invoice i inner join projects p on  i.project_id = p.id  inner join client c on p.client_id=c.id inner join payment_status ps on i.payment_status_id = ps.id inner join user_identity ui on ui.id = p.user_identity_id AND i.is_active = true  ");
        selectQuery1.append("Where p.created_by = '"+loggedInUser.getId().toString()+"'");

        if (!StringUtils.isEmpty(request != null)) {

            if (!request.getClientFirstName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getClientFirstName () + "%')");
            } else if (!request.getClientLastName().isEmpty()) {
                selectQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getClientLastName () + "%')");
            } else if (!request.getProjectName().isEmpty()) {
                selectQuery1.append("AND LOWER(p.project_name) LIKE LOWER('%" + request.getProjectName() + "%')");
            } else if(request.getPaymentStatus() != null){
                selectQuery1.append("AND i.payment_status_id = '"+ request.getPaymentStatus() +"'");
            }
            else if (null != request.getInvoiceDateFrom() && null != request.getInvoiceDateTo()) {
                selectQuery1.append("AND (i.invoice_date >= '" +request.getInvoiceDateFrom()+"' AND i.invoice_date <= '"+request.getInvoiceDateTo() +"')");
            } else if (null != request.getDueDateFrom() && null != request.getDueDateTo()) {
                selectQuery1.append("AND (i.due_date >= '" +request.getDueDateFrom()+"' AND i.due_date <= '"+request.getDueDateTo() +"')");
            } else if (null != request.getPaidDateFrom() && null != request.getPaidDateTo()) {
                selectQuery1.append("AND (i.paid_date >= '" +request.getPaidDateFrom()+"' AND i.paid_date <= '"+request.getPaidDateTo() +"')");
            }


            if(StringUtils.isEmpty(request) && !"ALL".equalsIgnoreCase(filter)){
                selectQuery1.append(" where ");
            } else if (!"ALL".equalsIgnoreCase(filter)) {
                selectQuery1.append(" and ");
            }

            if (sort.equalsIgnoreCase("FIRSTNAME")) {
                selectQuery1.append("order by c.first_name ASC, i.id DESC");

            } else if (sort.equalsIgnoreCase("LASTNAME")) {
                selectQuery1.append("order by c.last_name ASC, i.id DESC");

            } else if (sort.equalsIgnoreCase("ISACTIVE")) {
                selectQuery1.append("order by i.is_active ASC, i.id DESC");
            } else if (sort.equalsIgnoreCase("PROJECTNAME")) {
                selectQuery1.append("order by p.project_name ASC, i.id DESC");
            } else if (sort.equalsIgnoreCase("ID")){
                selectQuery1.append("order by i.id ASC");
            }
            else if (sort.equalsIgnoreCase("INVOICEDATE")){
                selectQuery1.append("order by i.invoice_date DESC, i.id DESC");
            }
        }

        String selectQuery = selectQuery1.toString();

        StringBuilder countQuery1 = new StringBuilder();

//        countQuery1.append("select DISTINCT count(i.*) from invoice i where i.is_active= true ");
        countQuery1.append("select count(i.*) from invoice i inner join projects p on  i.project_id = p.id  inner join client c on p.client_id=c.id inner join payment_status ps on i.payment_status_id = ps.id inner join user_identity ui on ui.id = p.user_identity_id AND i.is_active = true  ");
        countQuery1.append("Where p.created_by = '"+loggedInUser.getId()+"'");

        if (!StringUtils.isEmpty(request != null)) {

            if (!request.getClientFirstName().isEmpty()) {
                countQuery1.append("AND LOWER(c.first_name) LIKE LOWER('%" + request.getClientFirstName() + "%')");
            } else if (!request.getClientLastName().isEmpty()) {
                countQuery1.append("AND LOWER(c.last_name) LIKE LOWER('%" + request.getClientLastName() + "%')");
            } else if (!request.getProjectName().isEmpty()) {
                countQuery1.append("AND LOWER(p.project_name) LIKE LOWER('%" + request.getProjectName() + "%')");
            } else if (request.getPaymentStatus() != null) {
                countQuery1.append("AND i.payment_status_id = '" + request.getPaymentStatus() + "'");
            } else if (null != request.getInvoiceDateFrom() && null != request.getInvoiceDateTo()) {
                countQuery1.append("AND (i.invoice_date >= '" + request.getInvoiceDateFrom() + "' AND i.invoice_date <= '" + request.getInvoiceDateTo() + "')");
            } else if (null != request.getDueDateFrom() && null != request.getDueDateTo()) {
                countQuery1.append("AND (i.due_date >= '" + request.getDueDateFrom() + "' AND i.due_date <= '" + request.getDueDateTo() + "')");
            } else if (null != request.getPaidDateFrom() && null != request.getPaidDateTo()) {
                countQuery1.append("AND (i.paid_date >= '" + request.getPaidDateFrom() + "' AND i.paid_date <= '" + request.getPaidDateTo() + "')");
            }
        }

        String countQuery = countQuery1.toString();

        List<Invoice> invoiceList =entityManager.createNativeQuery(selectQuery, Invoice.class)
                .setFirstResult((pageable.getPageNumber() * pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize()).getResultList();

        BigInteger count = (BigInteger) entityManager.createNativeQuery(countQuery)
                 .getSingleResult();

        System.out.println("count "+count);

        long totalSwipeMatchCount = 0l;
        if(count != null)
            totalSwipeMatchCount = count.longValue();

        Page<Invoice> invoices = new PageImpl<>(invoiceList, pageable, totalSwipeMatchCount);

        return invoices;
    }
}
