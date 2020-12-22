package com.ssp.platform.validate;

import com.ssp.platform.entity.enums.QuestionStatus;
import com.ssp.platform.request.AnswerRequest;
import com.ssp.platform.response.ValidateResponse;

import java.util.UUID;

public class AnswerValidate extends Validator{

    private final int MIN_SIZE_DESCRIPTION = 1;
    private final int MAX_SIZE_DESCRIPTION = 1000;

    public  ValidateResponse validateAnswer(AnswerRequest request){

        UUID id = request.getId();
        String description = request.getDescription();
        String publicity = request.getPublicity();

        if(id == null){
            return new ValidateResponse(false, "ID", "Поле ID не может быть пустым");
        }

        if (description == null || onlySpaces(description)){
            return new ValidateResponse(false, "description", "Поле Текст ответа не может быть пустым");
        }

        if (description.length()< MIN_SIZE_DESCRIPTION || description.length()>MAX_SIZE_DESCRIPTION) {
            String message = String.format("Поле Текст ответа должно содержать от %d до %d символов", MIN_SIZE_DESCRIPTION, MAX_SIZE_DESCRIPTION);
            return new ValidateResponse(false, "description", message);
        }

        if(publicity == null || onlySpaces(publicity)){
            return new ValidateResponse(false, "publicity", "Поле Статус вопроса не может быть пустым");
        }

        if (!(publicity.equals(QuestionStatus.PRIVATE.getMessage())) &&
                !(publicity.equals(QuestionStatus.PUBLIC.getMessage()))){
            return new ValidateResponse(false, "publicity", "Значение поля Статус вопроса некорректно");
        }


        return new ValidateResponse(true, "", "ok");
    }


}
