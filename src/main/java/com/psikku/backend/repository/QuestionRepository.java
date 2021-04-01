package com.psikku.backend.repository;

import com.psikku.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, String> {

    Optional<Question> findQuestionByIdEquals(String id);
    List<Question> findByIdStartingWith(String prefix);
}
