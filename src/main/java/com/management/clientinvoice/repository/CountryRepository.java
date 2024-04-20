package com.biz4solutions.clientinvoice.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.biz4solutions.clientinvoice.domain.Country;

import java.util.Optional;


@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    @Query(value = "select * from country ids where ids.id = :id", nativeQuery = true)
    Country findOneByIdIgnoreIsActive(@Param("id") Long id);

    @Query(value = "select * from country c where c.is_active = true order by c.country_name ASC ",nativeQuery = true)
    Iterable<Country> findAllByIsActive();

//    @Query(value = "select * from country c inner join state s on c.id=:s.country_id",nativeQuery = true)
//    Country findAllByStateName(String Statename);

    Country findByCountryNameIgnoreCase(String countryName);

    Optional<Country> findById(Long id);

}


