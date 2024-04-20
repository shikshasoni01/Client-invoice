package com.biz4solutions.clientinvoice.constant;

import com.biz4solutions.clientinvoice.domain.Client;
import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.Projects;
import java.util.Comparator;

public interface ComparatorConstantsForProjects {

    Comparator<Projects> sortProjectsByProjectName =(p1 , p2) -> {
        if(null == p1.getProjectName() || null == p2.getProjectName()){
            return 0;
        }

        return p1.getProjectName().compareTo(p2.getProjectName());
    };




    Comparator<Projects> sortProjectsByIsActive =(p1 , p2) -> {
        if(null == p1.getIsActive() || null == p2.getIsActive()){
            return 0;
        }

        return p2.getIsActive().compareTo(p1.getIsActive());
    };


    Comparator<Client> sortInvoiceByClientFirstName=(p1 , p2) -> {
        if(null == p1.getFirstName()|| null == p2.getFirstName()){
            return 0;
        }

        return p2.getFirstName().compareTo(p1.getFirstName());
    };


    Comparator<Client> sortInvoiceByClientLastName=(p1 , p2) -> {
        if(null == p1.getLastName()|| null == p2.getLastName()){
            return 0;
        }

        return p2.getLastName().compareTo(p1.getLastName());
    };


    Comparator<Invoice> sortInvoiceById=(p1 , p2) -> {
        if(null == p1.getId()|| null == p2.getId()){
            return 0;
        }

        return p2.getId().compareTo(p1.getId());
    };


    Comparator<Client> sortClientByFaxNumber=(p1 , p2) -> {

        if(null == p1.getFaxNumber()|| null == p2.getFaxNumber()){
            return 0;
        }

        return p2.getFaxNumber().compareTo(p1.getFaxNumber());
    };


//    Comparator<Client> sortInvoiceByInvoiceDate=(p1 , p2) -> {
//
//        if(null == p1.getCity()|| null == p2.getCity()){
//            return 0;
//        }
//
//        return p2.getCity().compareTo(p1.getCity());
//    };
//





    //
//    Comparator<Projects> sortProjectsByStartDate =(p1 , p2) -> {
//        if(null == p1.getstartDate() || null == p2.getstartDate()){
//            return 0;
//        }
//
//        return p2.getstartDate().compareTo(p1.getstartDate());
//    };



//
//    Comparator<Projects> sortProjectsByStartDate =(p1 , p2) -> {
//        if(null == p1.getstartDate() || null == p2.getstartDate()){
//            return 0;
//        }
//
//        return p2.getstartDate().compareTo(p1.getstartDate());
//    };


//    Comparator<Projects> sortProjectsByClientId =(c1 , c2) -> {
//        if(null == c1.getClients() || null == c2.getClients()){
//            return 0;
//        }
//
//        return c1.getClients().compareTo(c2.getClients());
//    };


}

