package com.psikku.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psikku.backend.entity.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {
    Optional<Test> findTestByName(String name);
//    List<Test> findAll();
//    List<Test> findAll(Pageable pageable);
    

    
}
