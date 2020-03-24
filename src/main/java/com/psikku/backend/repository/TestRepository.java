package com.psikku.backend.repository;

import com.psikku.backend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Integer> {
    Optional<Test> findTestByName(String name);
}
