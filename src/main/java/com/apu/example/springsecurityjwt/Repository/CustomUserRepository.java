package com.apu.example.springsecurityjwt.Repository;

import com.apu.example.springsecurityjwt.entity.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomUserRepository extends CrudRepository<CustomUser, Long>, JpaSpecificationExecutor<CustomUser> {
    Optional<CustomUser> findCustomUserByUserId(String userId);
}
