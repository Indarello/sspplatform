package com.ssp.platform.service;

import com.ssp.platform.entity.*;
import com.ssp.platform.request.*;

import java.util.*;

public interface QuestionService {

	Question create(QuestionCreateRequest request, User author) throws RuntimeException;

	Question update(QuestionUpdateRequest request) throws RuntimeException;

	void delete(UUID id, User user) throws RuntimeException;

	Question findById(UUID id, User user) throws RuntimeException;

	List<Question> getQuestionsOfPurchase(UUID purchaseID, User user) throws RuntimeException;

}