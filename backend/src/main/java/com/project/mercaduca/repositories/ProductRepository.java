package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByUser(User user);
}
