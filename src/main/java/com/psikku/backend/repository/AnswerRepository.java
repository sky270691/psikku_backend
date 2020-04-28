package com.psikku.backend.repository;

import com.psikku.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    Optional<Answer> findById(String id);
    List<Answer> findByIdStartingWith(String prefix);
}
