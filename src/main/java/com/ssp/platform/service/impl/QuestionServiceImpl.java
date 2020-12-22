package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.QuestionStatus;
import com.ssp.platform.repository.*;
import com.ssp.platform.request.*;
import com.ssp.platform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;
	private final PurchaseRepository purchaseRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, PurchaseRepository purchaseRepository) {
        this.questionRepository = questionRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Question create(QuestionCreateRequest request, User author) throws RuntimeException{

        Purchase purchase = purchaseRepository.findById(request.getPurchaseId()).orElseThrow(()-> new RuntimeException("Закупки по данному id не существует"));

        Question question = new Question();
        question.setPurchase(purchase);
        question.setAuthor(author);
        question.setName(request.getName());
        question.setDescription(request.getDescription());

        questionRepository.saveAndFlush(question);

        return question;
    }

    @Override
    public Question update(QuestionUpdateRequest request) throws RuntimeException {
        Question question = questionRepository.findById(request.getId()).orElseThrow(()-> new RuntimeException("Вопрос не найден"));

        question.setDescription(request.getDescription());
        question.setName(request.getName());
        question.setPublicity(QuestionStatus.fromString(request.getPublicity()));

        return questionRepository.saveAndFlush(question);
    }

    @Override
    public void delete(UUID id, User user) throws RuntimeException {
        checkID(id);
        Question question = questionRepository.findById(id).orElseThrow(()-> new RuntimeException("Вопрос не найден"));

        if( (!question.getAuthor().equals(user)) && (!user.getRole().equals("employee")) ){
            throw new RuntimeException("Вопрос может удалить только его автор или сотрудник ресурса");
        }

        questionRepository.delete(question);

    }

    @Override
	public Question findById(UUID id, User user) throws RuntimeException{

        checkID(id);

        Question question = questionRepository.findById(id).orElseThrow(()-> new RuntimeException("Вопроса не существует"));

        if (user.getRole().equals("firm")){
            if (question.getAuthor().equals(user) ||
                    question.getPublicity().equals(QuestionStatus.PUBLIC)){
                return question;
            }
            throw new RuntimeException("Доступ к чужим приватным вопросам запрещён");
        }
        return question;
	}


    @Override
    public List<Question> getQuestionsOfPurchase(UUID purchaseID, User user) throws RuntimeException {

        checkID(purchaseID);

        Purchase purchase = purchaseRepository.findById(purchaseID).orElseThrow(()->new RuntimeException("Закупки не существует"));

        List<Question> result;

        if(user.getRole().equals("employee")){
            //получить все вопросы
            result = questionRepository.findByPurchase(purchase, Sort.by("createDate").descending());
        } else {
            //получить только свои и публичные вопросы
            result = questionRepository.findByPurchaseAndAuthorOrPurchaseAndPublicity(purchase,user,purchase,QuestionStatus.PUBLIC, Sort.by("createDate").descending());
        }

        return result;
    }

    private void checkID(UUID id) throws RuntimeException {
        if (id==null){
            throw new RuntimeException("Пустое поле ID");
        }
    }
}
