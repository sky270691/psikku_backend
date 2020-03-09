package com.psikku.backend.repository;

import com.psikku.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, String> {
}
