package com.psikku.backend.service;

import com.psikku.backend.dto.testpackage.CreatePackageResponseDto;
import com.psikku.backend.dto.testpackage.PackageDto;
import com.psikku.backend.entity.Package;
import org.springframework.stereotype.Service;

@Service
public interface PackageService {

    CreatePackageResponseDto createPackage(Package thePackage);
    Package convertToPackageEntity(PackageDto packageDto);

}
