package com.ssp.platform.exceptions;

import com.ssp.platform.response.ValidateResponse;

public class SupplyServiceException extends Exception {
    private final ValidateResponse response;

    public SupplyServiceException(ValidateResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public ValidateResponse getResponse(){
        return response;
    }
}
