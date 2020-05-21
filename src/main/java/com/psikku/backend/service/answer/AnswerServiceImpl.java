package com.psikku.backend.service.answer;

import com.psikku.backend.entity.Answer;
import com.psikku.backend.exception.AnswerException;
import com.psikku.backend.repository.AnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final Logger logger;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Answer findById(String id) {
        return answerRepository.findById(id).orElseThrow(()-> {
            logger.error("No answer found by Id: "+id);
            return new AnswerException("No Answer Found");
        });
    }

    @Override
    public List<Answer> findByIdStartingWith(String prefix) {
        return answerRepository.findByIdStartingWith(prefix);
    }
}
