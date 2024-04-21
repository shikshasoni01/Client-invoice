package com.management.clientinvoice.repository;


import com.management.clientinvoice.domain.OtpTransaction;
import com.management.clientinvoice.domain.UserIdentity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OtpTransactionRepository extends BaseRepository<OtpTransaction> {
    OtpTransaction findOneByTransactionId(String transactionId);

    Long countAllByUserIdentityAndCreatedAtAfter(UserIdentity userIdentity, Date addHours);

    @Query(value = "select ru.id from OtpTransaction ru where ru.userIdentity = :userIdentity")
    List<UUID> findAllIdsByUserIdentity(@Param("userIdentity") UserIdentity userIdentity);

    @Modifying
    @Transactional
    @Query("delete from OtpTransaction ru where ru.id in ?1")
    void deleteAllUsersWithIds(@Param("ids") List<UUID> ids);
}

