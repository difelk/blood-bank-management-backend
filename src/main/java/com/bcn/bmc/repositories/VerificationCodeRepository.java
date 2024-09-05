package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.VerificationStatus;
import com.bcn.bmc.models.User;
import com.bcn.bmc.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VerificationCodeRepository  extends JpaRepository<VerificationCode, Long> {

    @Query("select v from VerificationCode v where v.email = ?1 and v.code = ?2")
    VerificationCode getDetailsForEmailAndCode(String email, String code);


    @Modifying
    @Query("update VerificationCode v set v.status = :status where v.email = :email and v.code = :code")
    int updateVerificationCode(@Param("status") VerificationStatus status, @Param("email") String email, @Param("code") String code);

}
