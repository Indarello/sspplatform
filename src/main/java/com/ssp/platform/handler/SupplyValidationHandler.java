package com.ssp.platform.handler;

import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.response.ValidatorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class SupplyValidationHandler {

    @ExceptionHandler(SupplyException.class)
    protected ResponseEntity<ValidatorResponse> handleSupplyException(){
        return new ResponseEntity<>(SupplyException.getValidatorResponse(), HttpStatus.BAD_REQUEST);
    }

}
