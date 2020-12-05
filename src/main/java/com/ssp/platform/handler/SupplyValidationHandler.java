package com.ssp.platform.handler;

import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.response.ValidateResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class SupplyValidationHandler {

    @ExceptionHandler(SupplyException.class)
    protected ResponseEntity<ValidateResponse> handleSupplyException(){
        return new ResponseEntity<>(SupplyException.getValidatorResponse(), HttpStatus.BAD_REQUEST);
    }

}
