package com.management.clientinvoice.repository;


import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.domain.UserPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPictureRepository extends JpaRepository<UserPicture, UUID> {

    List<UserPicture> findByUserIdentityAndIsActive(UserIdentity userIdentity, boolean isActive);

    List<UserPicture> findByUserIdentityAndPicURLAndIsActive(UserIdentity userIdentity, String picURL, boolean isActive);

    UserPicture findOneByUserIdentityAndIdAndIsActive(UserIdentity userIdentity, UUID id, boolean isActive);

    UserPicture findOneByUserIdentityAndIsDefaultImageAndIsActive(UserIdentity userIdentity, boolean isDefaultImage, boolean isActive);

    @Query(value = "SELECT count(*) FROM user_picture p WHERE p.user_identity_id = :userIdentityId and p.is_active = :stateActive", nativeQuery = true)
    int findPicturesCountByUserIdentityAndIsActive(@Param("userIdentityId") Long userIdentityId, @Param("stateActive") Boolean stateActive);

}