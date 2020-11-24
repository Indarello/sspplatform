package com.ssp.platform.exceptions.PageExceptions;

import com.ssp.platform.dto.ErrorDTO;
import org.springframework.http.HttpStatus;

/**
 * Класс исключения неверного индекса страницы
 */
public class PageIndexException extends Exception{
    private static final String ERROR_MESSAGE = "Page index must not be less than zero!";

    /**
     * Метод для получения ErrorModel исключения
     *
     * @return ErrorModel
     */
    public static ErrorDTO getErrorModel(){
        return new ErrorDTO(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
    }
}
