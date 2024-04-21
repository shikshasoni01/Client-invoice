package com.management.clientinvoice.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.management.clientinvoice.domain.Client;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, String> {


    @Query(value = "select * from client ids where ids.id = :id", nativeQuery = true)
    Client findOneByIdIgnoreIsActive(@Param("id") Long id);

    List<Client> findAll();

    Client findOneByEmail(String email);

    @Query(value = "select * from client c where c.is_active = true order by c.first_name ASC ",nativeQuery = true)
     List<Client> findAllByIsActive();


    Client findById(Long id);

//    Client findByGstin(String gstin);

    Client findByClientId(String clientId);

}