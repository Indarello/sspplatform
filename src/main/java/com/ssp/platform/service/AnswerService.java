package com.ssp.platform.service;

import com.ssp.platform.entity.Answer;
import com.ssp.platform.request.AnswerRequest;

import java.util.UUID;

public interface AnswerService {

	Answer save(AnswerRequest request) throws RuntimeException;

	Answer update(AnswerRequest request) throws RuntimeException;

	void delete(UUID id) throws RuntimeException;

	Answer findByID(UUID id) throws RuntimeException;

}
