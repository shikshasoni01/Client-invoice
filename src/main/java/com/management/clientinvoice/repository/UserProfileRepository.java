package com.management.clientinvoice.repository;


import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findOneByUserIdentityAndIsActive(UserIdentity userIdentity, boolean isActive);

}

