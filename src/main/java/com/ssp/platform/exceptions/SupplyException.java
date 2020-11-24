package com.ssp.platform.exceptions;

import com.ssp.platform.response.ValidatorResponse;

public class SupplyException extends Exception {

    public static ValidatorResponse validatorResponse;

    public SupplyException(ValidatorResponse validatorResponse) {
        super(validatorResponse.getMessage());
        this.validatorResponse = validatorResponse;
    }

    public static ValidatorResponse getValidatorResponse() {
        return validatorResponse;
    }
}
