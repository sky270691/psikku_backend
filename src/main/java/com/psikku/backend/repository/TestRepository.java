package com.psikku.backend.repository;

import java.util.Optional;

import com.psikku.backend.dto.test.MinimalTestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.psikku.backend.entity.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {
    Optional<Test> findTestByInternalName(String internalName);
    Optional<Test> findTestByName(String name);
    Optional<Test> findById(int id);

//    Optional<Object> findById(Integer testId);
//    List<test> findAll();
//    List<test> findAll(Pageable pageable);
    

    
}
