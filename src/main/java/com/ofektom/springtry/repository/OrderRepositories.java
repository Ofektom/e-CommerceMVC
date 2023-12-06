package com.ofektom.springtry.repository;

import com.ofektom.springtry.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositories extends JpaRepository<Order, Long> {
}
