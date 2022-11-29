package com.apu.example.springsecurityjwt.Repository;

import com.apu.example.springsecurityjwt.entity.UserCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends CrudRepository<UserCredential, Long> {
    @Query("SELECT DISTINCT user FROM UserCredential user " +
            "INNER JOIN FETCH user.authorities AS userRole " +
            "WHERE user.username = :username")
    Optional<UserCredential> findByUsername(@Param("username") String username);
}
