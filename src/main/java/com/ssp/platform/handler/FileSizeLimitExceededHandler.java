package com.ssp.platform.handler;

import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.ValidateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Обработчик исключений
 */
@ControllerAdvice
public class FileSizeLimitExceededHandler
{
    /**
     * Я не знаю как это по другому решить
     * но без этого если размер файла превышает допустимый (10МБ) - всегда вылетает SizeLimitExceededException
     * при этом на стороне сервера
     * Использую его родительский класс (MultipartException), может быть там еще какие то ошибки бывают
     */
    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(MultipartException ex)
    {
        return new ResponseEntity(new ValidateResponse(false, "files", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
