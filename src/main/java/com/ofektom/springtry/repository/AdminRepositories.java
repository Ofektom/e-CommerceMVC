package com.ofektom.springtry.repository;

import com.ofektom.springtry.models.Admin;
import com.ofektom.springtry.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepositories extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}

