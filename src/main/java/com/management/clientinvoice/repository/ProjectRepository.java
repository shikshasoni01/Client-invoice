package com.biz4solutions.clientinvoice.repository;



import com.biz4solutions.clientinvoice.domain.Client;
import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ProjectRepository extends JpaRepository<Projects,String> {

    @Query(value = "select * from projects ids where ids.id = :id", nativeQuery = true)
    Projects findOneByIdIgnoreIsActive(@Param("id") Long id);

    Projects findById(Long id);

    Projects findOneByProjectNameIgnoreCase(String projectName);

    @Query(value = "select * from projects p where p.is_active = true order by p.project_name ASC ",nativeQuery = true)
    List<Projects> findAllByIsActive();

    List<Projects> findByClient(Client client);

    @Query(value = "select * from projects where user_identity_id= :id",nativeQuery = true)
    List<Projects> findAllProjectByUserIdentityId(@Param("id") Long id);



}
