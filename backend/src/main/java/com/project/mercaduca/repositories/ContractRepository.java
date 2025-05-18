package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllByUserId(Long userId);
}
