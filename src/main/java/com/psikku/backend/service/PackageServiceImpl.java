package com.psikku.backend.service;

import com.psikku.backend.dto.testpackage.CreatePackageResponseDto;
import com.psikku.backend.dto.testpackage.PackageDto;
import com.psikku.backend.entity.Package;
import com.psikku.backend.exception.PackageException;
import com.psikku.backend.repository.PackageRepository;
import com.psikku.backend.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    TestRepository testRepository;

    @Override
    public CreatePackageResponseDto createPackage(Package thePackage) {
        Package createdPackage = packageRepository.save(thePackage);
        if(createdPackage != null){
            int id = createdPackage.getId();
            String name = createdPackage.getName();
            int price = createdPackage.getPrice();
            List<String> testNameList = createdPackage.getTestList().stream()
                    .map(test -> test.getName())
                    .collect(Collectors.toList());
            CreatePackageResponseDto responseDto = new CreatePackageResponseDto(id,name,"success",price,testNameList);
            return responseDto;
        }
        throw new PackageException("error creating package");
    }

    @Override
    public Package convertToPackageEntity(PackageDto packageDto) {
        Package tempPackage = new Package();
        tempPackage.setName(packageDto.getName());
        tempPackage.setPrice(packageDto.getPrice());
        tempPackage.setTestList(new ArrayList<>());
        if(!packageDto.getTestIdList().isEmpty()){
            for(Integer testId : packageDto.getTestIdList()){
                testRepository.findById(testId).ifPresent(test -> tempPackage.getTestList().add(test));
            }
        }else{
            throw new PackageException("error creating package");
        }
        return tempPackage;
    }
}
