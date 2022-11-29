package com.apu.example.springsecurityjwt.Repository;


import com.apu.example.springsecurityjwt.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}