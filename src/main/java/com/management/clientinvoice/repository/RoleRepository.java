package com.management.clientinvoice.repository;

import com.management.clientinvoice.domain.Role;
import com.management.clientinvoice.enumerator.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findOneByRoleType(RoleType roleType);

}

