package com.psikku.backend.repository;

import com.psikku.backend.entity.TestPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPackageRepository extends JpaRepository<TestPackage, Integer> {
//    Optional<Package> save(Package thePackage);

}
