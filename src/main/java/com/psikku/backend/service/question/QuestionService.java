package com.psikku.backend.service.question;

import com.psikku.backend.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    Question findQuestionByIdEquals(String id);

    List<Question> findByIdStartingWith(String prefix);
}
