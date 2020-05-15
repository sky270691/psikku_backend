package com.psikku.backend.service;

import com.psikku.backend.dto.testpackage.CreatePackageResponseDto;
import com.psikku.backend.dto.testpackage.PackageDto;
import com.psikku.backend.entity.Package;
import com.psikku.backend.entity.Test;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PackageService {

    boolean generatePackage(List<Test> tests);

    Package getPackageById(int packageId);

    List<Test> getAllTestInPackage (Package thePackage);

    void savePackage(Package thePackage);

    void deletePackage(Package thePackage);
}
