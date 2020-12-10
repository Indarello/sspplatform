package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.QuestionStatus;
import com.ssp.platform.repository.QuestionRepository;
import com.ssp.platform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;

	@Autowired
	QuestionServiceImpl(QuestionRepository questionRepository){
		this.questionRepository = questionRepository;
	}


	@Override
	public Question save(Question question) {
		return questionRepository.saveAndFlush(question);
	}

	@Override
	public Optional<Question> update(Question question) {
		Optional<Question> optionalQuestion = questionRepository.findById(question.getId());
		if (optionalQuestion.isPresent()){
			return Optional.of(questionRepository.saveAndFlush(question));
		}
		return Optional.empty();
	}

	@Override
	public boolean delete(UUID id) {
		Optional<Question> optionalQuestion = questionRepository.findById(id);
		if (optionalQuestion.isPresent()){
			questionRepository.deleteById(id);
			return true;
		}
		return false;
	}

    @Override
    public List<Question> getQuestionsOfPurchase(Purchase purchase) {
	    return questionRepository.findByPurchase(purchase);
    }

    @Override
	public Optional<Question> findById(UUID id) {
		return questionRepository.findById(id);
	}

    @Override
    public List<Question> getQuestionsOfPurchaseByAuthor(Purchase purchase, User author) {

        return questionRepository.findByPurchaseAndAuthorOrPublicity(purchase, author, QuestionStatus.PUBLIC);
    }
}
