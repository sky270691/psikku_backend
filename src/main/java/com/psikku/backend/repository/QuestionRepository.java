package com.psikku.backend.repository;

import com.psikku.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Optional<Question> findQuestionByIdEquals(String id);
}
