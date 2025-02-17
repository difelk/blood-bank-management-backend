package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a where a.userId = ?1")
    Optional<Address> findAddressByUserId(@Param("userId") Long userId);
}
