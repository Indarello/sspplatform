package com.ssp.platform.exceptions;

import com.ssp.platform.response.ValidateResponse;

public class SupplyException extends Exception {

    public static ValidateResponse validatorResponse;

    public SupplyException(ValidateResponse validatorResponse) {
        super(validatorResponse.getMessage());
        this.validatorResponse = validatorResponse;
    }

    public static ValidateResponse getValidatorResponse() {
        return validatorResponse;
    }
}
