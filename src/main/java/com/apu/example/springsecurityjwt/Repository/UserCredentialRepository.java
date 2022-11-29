package com.apu.example.springsecurityjwt.Repository;

import com.apu.example.springsecurityjwt.entity.UserCredential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends CrudRepository<UserCredential, Long> {
    Optional<UserCredential> findByUsername(String username);
}
