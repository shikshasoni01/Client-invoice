package com.management.clientinvoice.repository;


import com.management.clientinvoice.domain.Otp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface OtpRepository extends BaseRepository<Otp> {

    @Query(value = "select ru from Otp ru where ru.otpTransaction.id = ?1")
    List<Otp> findAllByOtpTransactionId(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("delete from Otp ru where ru.otpTransaction.id in ?1")
    void deleteAllUsersWithIds(@Param("ids") List<UUID> otpIds);
}

