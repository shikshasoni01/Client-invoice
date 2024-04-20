package com.biz4solutions.clientinvoice.repository;


import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findOneByUserIdentityAndIsActive(UserIdentity userIdentity, boolean isActive);

}

