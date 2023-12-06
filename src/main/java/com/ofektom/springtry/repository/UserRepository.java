package com.ofektom.springtry.repository;

import com.ofektom.springtry.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.Function;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername (String username);
}
