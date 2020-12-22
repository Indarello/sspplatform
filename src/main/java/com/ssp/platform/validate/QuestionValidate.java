package com.ssp.platform.validate;

import com.ssp.platform.entity.enums.QuestionStatus;
import com.ssp.platform.request.*;
import com.ssp.platform.response.ValidateResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuestionValidate extends Validator{

    private  final int MIN_SIZE = 1;
    private  final int MAX_SIZE_NAME = 100;
    private  final int MAX_SIZE_DESCRIPTION = 1000;

    private  boolean success;
    private  String field;
    private  String message;

    public QuestionValidate() {
        success = true;
        field = "";
        message = "ok";
    }

    public ValidateResponse validateQuestionCreate(QuestionCreateRequest questionCreateRequest){

        validateId(questionCreateRequest.getPurchaseId());
        if(!success){
            return new ValidateResponse(false, field, message);
        }

        validateName(questionCreateRequest.getName());
        if(!success){
            return new ValidateResponse(false, field, message);
        }
        validateDescription(questionCreateRequest.getDescription());

        return new ValidateResponse(success, field, message);
    }

    public ValidateResponse validateQuestionUpdate(QuestionUpdateRequest questionUpdateRequest){

        validateId(questionUpdateRequest.getId());
        if(!success){
            return new ValidateResponse(false, field, message);
        }

        validateName(questionUpdateRequest.getName());
        if(!success){
            return new ValidateResponse(false, field, message);
        }
        validateDescription(questionUpdateRequest.getDescription());
        if(!success){
            return new ValidateResponse(false, field, message);
        }
        validateStatus(questionUpdateRequest.getPublicity());

        return new ValidateResponse(success, field, message);
    }

    private void validateName(String name){
        if (name == null || onlySpaces(name)){
            success = false;
            field = "name";
            message = "Поле Тема сообщения не может быть пустым";
            return;
        }

        if (name.length()<MIN_SIZE || name.length()>MAX_SIZE_NAME){
            success = false;
            field = "name";
            message = String.format("Тема сообщения должна содержать от %d до %d символов", MIN_SIZE, MAX_SIZE_NAME);
        }
    }

    private  void validateDescription(String description){

        if (description == null || onlySpaces(description)){
            success = false;
            field = "description";
            message = "Поле Текст сообщения не может быть пустым";
            return;
        }

        if (description.length()<MIN_SIZE || description.length()>MAX_SIZE_DESCRIPTION){
            success = false;
            field = "description";
            message = String.format("Текст сообщения должен содержать от %d до %d символов", MIN_SIZE, MAX_SIZE_DESCRIPTION);
        }

    }

    private void validateStatus(String status){

        if (status == null || onlySpaces(status)){
            success = false;
            field = "status";
            message = "Поле Статус вопроса не может быть пустым";
            return;
        }

        if (!(status.equals(QuestionStatus.PRIVATE.getMessage())) &&
        !(status.equals(QuestionStatus.PUBLIC.getMessage()))){
            success = false;
            field = "status";
            message = "Значение поля Статус вопроса некорректно";
        }

    }

    private void validateId(UUID id){
        if(id == null){
            success = false;
            field = "ID";
            message = "ID не может быть пустым";
        }
    }

}
