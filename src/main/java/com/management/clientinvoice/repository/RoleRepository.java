package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.Role;
import com.biz4solutions.clientinvoice.enumerator.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findOneByRoleType(RoleType roleType);

}

