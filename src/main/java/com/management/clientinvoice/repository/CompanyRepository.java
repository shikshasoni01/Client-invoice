package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Company findCompanyById(Long id);


    @Query(value = "select * from company where company_type ='D'",nativeQuery = true)
    List<Company> findAllDomesticCompanyList();

}
