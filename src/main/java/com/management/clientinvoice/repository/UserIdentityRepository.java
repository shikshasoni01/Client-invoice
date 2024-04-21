package com.management.clientinvoice.repository;



import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.enumerator.RoleType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface     UserIdentityRepository extends BaseRepository<UserIdentity>, JpaSpecificationExecutor<UserIdentity> {

    @Query(value = "SELECT * FROM user_identity u WHERE u.verification_code = ?1",nativeQuery = true)
     UserIdentity findByVerificationCode(String code);

    UserIdentity findTop1ByEmailIgnoreCase(String email);

    UserIdentity findOneByUniqueId(String uniqueId);

    Optional<UserIdentity> findById(Long id);
    UserIdentity findOneById(Long id);

    UserIdentity findTop1ByEmailIgnoreCaseAndIdNot(String email, UUID uuid);


    UserIdentity findTop1ByContactNoAndIdNot(String phoneNumber, UUID uuidFromStringIfValidElseNull);

    UserIdentity findOneByEmailIgnoreCaseAndRole_RoleType(String email, RoleType roleType);

    List<UserIdentity> findAllByIdIn(List<UUID> uuidList);

    UserIdentity findOneByEmailIgnoreCase(String email);

    UserIdentity findOneByContactNo(String contactNo);

}

