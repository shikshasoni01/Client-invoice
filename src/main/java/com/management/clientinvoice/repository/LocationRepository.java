package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
}
