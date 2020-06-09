package com.psikku.backend.service.question;

import com.psikku.backend.entity.Question;
import com.psikku.backend.exception.QuestionException;
import com.psikku.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public Question findQuestionByIdEquals(String id) {
        return questionRepository.findQuestionByIdEquals(id).orElseThrow(()-> new QuestionException("Question by id"+ id+" not found"));
    }

    @Override
    public List<Question> findByIdStartingWith(String prefix) {
        return questionRepository.findByIdStartingWith(prefix);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }
}
