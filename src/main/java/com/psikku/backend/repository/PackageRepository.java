package com.psikku.backend.repository;

import com.psikku.backend.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Integer> {
//    Optional<Package> save(Package thePackage);
}
