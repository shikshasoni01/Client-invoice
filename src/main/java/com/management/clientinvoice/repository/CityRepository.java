package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CityRepository extends JpaRepository<City, String> {

    @Query(value = "select * from city ids where ids.id = :id", nativeQuery = true)
    City findOneByIdIgnoreIsActive(@Param("id") Long id);

    @Query(value = "SELECT * FROM  City c  where c.state_id =:id and c.is_active = true order by c.city_name ASC",nativeQuery = true)
    List<City> findAllByStateId(Long id);

    City findByCityNameIgnoreCase(String cityName);

    Optional<City> findById(Long id);


}


