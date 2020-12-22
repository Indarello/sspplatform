package com.ssp.platform.controller;

import com.ssp.platform.entity.*;
import com.ssp.platform.request.*;
import com.ssp.platform.response.*;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.*;
import com.ssp.platform.validate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class QAController {

	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserDetailsServiceImpl userDetailsService;
	private final QuestionValidate questionValidate;

	@Autowired
    public QAController(
            QuestionService questionService, AnswerService answerService, UserDetailsServiceImpl userDetailsService,
            QuestionValidate questionValidate
    ) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userDetailsService = userDetailsService;
        this.questionValidate = questionValidate;
    }

    /**
	 * Метод создания вопроса. Доступен только поставщикам
	 * @param token Токен пользователя для выставления автора
     * @param questionCreateRequest body: name - тема вопроса, description - текст вопроса, purchaseId - id закупки
	 * @return
	 */
	@PostMapping(value = "/question", produces = "application/json")
	@PreAuthorize("hasAuthority('firm')")
	public ResponseEntity<Object> addQuestion(@RequestHeader("Authorization") String token, @RequestBody QuestionCreateRequest questionCreateRequest){

	    ValidateResponse validateResponse = questionValidate.validateQuestionCreate(questionCreateRequest);
        if(!validateResponse.isSuccess()){
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

		User author = userDetailsService.loadUserByToken(token);
		try
		{
			return new ResponseEntity<>(questionService.create(questionCreateRequest, author), HttpStatus.CREATED);
		}
		catch (RuntimeException exception)
		{
			return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()),	HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * Метод изменения вопроса. Доступен только сотруднику
	 * @param questionUpdateRequest body: id, name - тема вопроса, description - текст вопроса, publicity - статус вопроса (public/private)
	 * @return
	 */
	@PutMapping(value = "/question", produces = "application/json", consumes = "application/json")
	@PreAuthorize("hasAuthority('employee')")
	public ResponseEntity<Object> changeQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest){

        ValidateResponse validateResponse = questionValidate.validateQuestionUpdate(questionUpdateRequest);
        if(!validateResponse.isSuccess()){
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

		try{
			return new ResponseEntity<>(questionService.update(questionUpdateRequest), HttpStatus.CREATED);
		}
		catch (RuntimeException exception)
		{
			return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * Метод получения списка публичных вопросов (и заданных самим пользователем) по закупке (нужно указать id нужной закупки).
	 * Метод предназначен как для сотрудников, так и для поставщиков.
	 * @param purchaseId id закупки вопроса
	 * @return
	 */
	@GetMapping(value = "/questions", produces = "application/json")
	@PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
	public ResponseEntity<Object> getQuestion(  @RequestHeader("Authorization") String token,
												@RequestParam(value = "purchaseId") UUID purchaseId) {

		User user = userDetailsService.loadUserByToken(token);
		try{
		    return new ResponseEntity<>(questionService.getQuestionsOfPurchase(purchaseId, user), HttpStatus.OK);
        }catch (RuntimeException exception){
		    return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
	}

	/**
	 * Метод получения вопроса по id. Метод предназначен для сотрудников и поставщиков
	 * @param id - id вопроса
	 * @return
	 */
	@GetMapping(value = "/question/{id}", produces = "application/json")
	@PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
	public ResponseEntity<Object> getQuestionByRole(@RequestHeader("Authorization") String token, @PathVariable(value = "id") UUID id)
	{

		User user = userDetailsService.loadUserByToken(token);

		try {
		    return new ResponseEntity<>(questionService.findById(id, user), HttpStatus.OK);
        }catch (RuntimeException exception){
		    return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }

	}

	/**
	 * Метод удаления вопроса.
	 * @param id - id вопроса
	 * @return
	 */
	@DeleteMapping(value = "/question/{id}", produces = "application/json")
	@PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
	public ResponseEntity<Object> deleteQuestion(@RequestHeader("Authorization") String token, @PathVariable(name = "id") UUID id)
	{

        User user = userDetailsService.loadUserByToken(token);

		try{
		    questionService.delete(id, user);
		    return new ResponseEntity<>(new ApiResponse(true,"Вопрос удален"), HttpStatus.OK);
        }catch (RuntimeException exception){
		    return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
	}

	////////////////////////////////////
	//Методы по работе с ответами ниже//
	////////////////////////////////////

	/**
	 * Меотод добавления ответа на вопрос. Доступен только сотрудникам
	 * @param request body: id вопроса, тема вопроса, статус вопроса
	 * @return
	 */
	@PostMapping(value = "/answer", produces = "application/json")
	@PreAuthorize("hasAuthority('employee')")
	public ResponseEntity<Object> addAnswer(@RequestBody AnswerRequest request){

	    AnswerValidate answerValidate = new AnswerValidate();

	    ValidateResponse validateResponse = answerValidate.validateAnswer(request);
	    if(!validateResponse.isSuccess()){
	        return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

	    try {
	        Answer answer = answerService.save(request);
	        return new ResponseEntity<>(answer, HttpStatus.OK);
        }catch (RuntimeException exception){
	        return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
	}

	/**
	 * Метод изменения ответа. Доступен только сотрудникам
     * @param request id ответа, тема вопроса, статус вопроса
	 * @return
	 */
	@PutMapping(value = "/answer", produces = "application/json")
	@PreAuthorize("hasAuthority('employee')")
	public ResponseEntity<Object> changeAnswer(@RequestBody AnswerRequest request){

        AnswerValidate answerValidate = new AnswerValidate();

        ValidateResponse validateResponse = answerValidate.validateAnswer(request);
        if(!validateResponse.isSuccess()){
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Answer answer = answerService.update(request);
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }catch (RuntimeException exception){
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
	}

	/**
	 * Метод получения ответа по id. Доступен только сотрудникам
	 * @param id - id ответа
	 * @return
	 */
	@GetMapping(value = "/answer/{id}", produces = "application/json")
	@PreAuthorize("hasAuthority('employee')")
	public ResponseEntity<Object> getAnswer(@PathVariable(value = "id") UUID id)
	{
		if (id == null)
		{
			return new ResponseEntity<>(new ApiResponse(false, "Пустое поле id"), HttpStatus.NOT_ACCEPTABLE);
		}
		try {
            return new ResponseEntity<>(answerService.findByID(id), HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * Метод удаления ответа на вопрос. Доступен только сотрудникам
	 * @param id - id ответа
	 * @return
	 */
	@DeleteMapping(value = "/answer/{id}", produces = "application/json")
	@PreAuthorize("hasAuthority('employee')")
	public ResponseEntity<Object> deleteAnswer(@PathVariable(name = "id") UUID id)
	{
		if (id == null)
		{
			return new ResponseEntity<>(new ApiResponse(false, "Пустое поле id"), HttpStatus.NOT_ACCEPTABLE);
		}

		try
		{
		    answerService.delete(id);
            return new ResponseEntity<>(new ApiResponse(true, "Ответ удалён"), HttpStatus.OK);

		}
		catch (Exception e)
		{
			return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		}
	}

}