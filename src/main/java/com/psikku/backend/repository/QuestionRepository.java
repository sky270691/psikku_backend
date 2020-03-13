package com.psikku.backend.repository;

import com.psikku.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findQuestionByIdEquals(String id);
}
