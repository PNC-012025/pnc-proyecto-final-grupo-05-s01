package com.project.mercaduca.repositories;

import com.project.mercaduca.models.ProductApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductApprovalRepository extends JpaRepository<ProductApproval, Long> {

}
