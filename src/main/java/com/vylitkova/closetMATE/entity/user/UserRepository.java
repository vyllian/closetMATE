package com.vylitkova.closetMATE.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);


    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Transactional
    @Query(value = "select enabled from users where email = ?1", nativeQuery = true)
    boolean isConfirmed(String email);

    @Transactional
    @Modifying
    @Query(value = "delete from confirmation_token where user_id = ?1", nativeQuery = true)
    void deleteToken(UUID user_id);
}
