package com.psikku.backend.service.answer;

import com.psikku.backend.entity.Answer;

import java.util.List;

public interface AnswerService {

    Answer findById(String id);
    List<Answer> findByIdStartingWith(String prefix);

}
