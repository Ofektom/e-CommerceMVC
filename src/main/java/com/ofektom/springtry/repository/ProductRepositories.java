package com.ofektom.springtry.repository;

import com.ofektom.springtry.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositories extends JpaRepository<Product, Long>{
    List<Product> findByCategories(String categories);

    void deleteById(Long id);


//    List<Product> findByIdIn(List<Long> ids);
    List<Product> findByProductName(String productName);
//
//    List<Product> findByProductNameIgnoreCasesStartingWith(String name);
}
