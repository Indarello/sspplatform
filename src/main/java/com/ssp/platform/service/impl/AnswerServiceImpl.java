package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.QuestionStatus;
import com.ssp.platform.repository.*;
import com.ssp.platform.request.AnswerRequest;
import com.ssp.platform.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerServiceImpl implements AnswerService {

	private final AnswerRepository answerRepository;
	private final QuestionRepository questionRepository;

	@Autowired
	AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository){
		this.answerRepository = answerRepository;
		this.questionRepository = questionRepository;
	}

	@Override
	public Answer save(AnswerRequest request) throws RuntimeException {

        Question question = questionRepository.findById(request.getId()).orElseThrow(()->new RuntimeException("Вопрос по данному id не существует!"));

	    if (question.getAnswer()!=null){
	        throw new RuntimeException("Ответ уже существует, добавить новый невозможно");
        }

	    question.setPublicity(QuestionStatus.fromString(request.getPublicity()));
	    Answer answer = new Answer(request.getDescription(), question);

        answerRepository.saveAndFlush(answer);
        questionRepository.saveAndFlush(question);

	    return answer;
    }

    @Override
    public Answer update(AnswerRequest request) throws RuntimeException {

        Answer answer = answerRepository.findById(request.getId()).orElseThrow(()->new RuntimeException("Ответа с данным ID не существует"));
        Question question = answer.getQuestion();

        question.setPublicity(QuestionStatus.fromString(request.getPublicity()));
        answer.setDescription(request.getDescription());

        answerRepository.saveAndFlush(answer);
        questionRepository.saveAndFlush(question);

        return answer;
    }

    @Override
    public void delete(UUID id) throws RuntimeException{

        Answer answer = answerRepository.findById(id).orElse(null);
        if(answer == null){
            throw new RuntimeException("Ответ не найден");
        }

        Question question = answer.getQuestion();
        question.setAnswer(null);
        questionRepository.saveAndFlush(question);
        answerRepository.delete(answer);
    }

    @Override
    public Answer findByID(UUID id) throws RuntimeException{
        return answerRepository.findById(id).orElseThrow(()-> new RuntimeException("Ответа не существует"));
    }
}
