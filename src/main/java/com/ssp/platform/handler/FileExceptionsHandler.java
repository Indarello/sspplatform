package com.ssp.platform.handler;

import com.ssp.platform.exceptions.*;
import com.ssp.platform.response.ValidateResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class FileExceptionsHandler {

    @ExceptionHandler(FileValidationException.class)
    protected ResponseEntity<ValidateResponse> handleFileValidationException(FileValidationException exception){
        return new ResponseEntity<>(exception.getResponse(), HttpStatus.BAD_REQUEST);
    }
}
