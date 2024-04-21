package com.management.clientinvoice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.management.clientinvoice.domain.State;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends CrudRepository<State, String> {

    @Query(value = "select * from state ids where ids.id = :id", nativeQuery = true)
   State findOneByIdIgnoreIsActive(@Param("id") Long id);

    Optional<State> findById(Long id);

    State findByStateNameIgnoreCase(String stateName);

//    @Query(value = "select * from state s where country_id=;",nativeQuery = true)
//    State findAllByStateName(String statename);



   @Query(value = "SELECT * FROM  State st  where st.country_id =:id and st.is_active = true order by st.state_name ASC",nativeQuery = true)
    List<State> findAllByCountryId(Long id);

}



