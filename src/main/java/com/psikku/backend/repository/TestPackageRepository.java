package com.psikku.backend.repository;

import com.psikku.backend.entity.TestPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestPackageRepository extends JpaRepository<TestPackage, Integer> {
}
