package com.psikku.backend.service.subtest;

import com.psikku.backend.entity.Subtest;
import com.psikku.backend.exception.SubtestException;
import com.psikku.backend.repository.SubtestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubtestServiceImpl implements SubtestService {

    private final SubtestRepository subtestRepository;

    @Autowired
    public SubtestServiceImpl(SubtestRepository subtestRepository) {
        this.subtestRepository = subtestRepository;
    }

    @Override
    public Subtest findById(String id) {
        return subtestRepository.findById(id).orElseThrow(()-> new SubtestException("subtest by id: "+id+" not found"));
    }

    @Override
    public Subtest save(Subtest subtest) {
        return subtestRepository.save(subtest);
    }
}
