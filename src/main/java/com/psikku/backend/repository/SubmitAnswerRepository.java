package com.psikku.backend.repository;

import com.psikku.backend.entity.SubmittedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmitAnswerRepository extends JpaRepository<SubmittedAnswer,String> {
//    SubmittedAnswer findAllById(String id);
}
