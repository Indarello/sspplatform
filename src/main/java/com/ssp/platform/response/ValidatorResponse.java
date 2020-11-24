package com.ssp.platform.response;

import com.ssp.platform.validate.ValidatorStatus;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ValidatorResponse {
    //TODO: в будущем удалить этот файл

    @NonNull private ValidatorStatus validatorStatus;

    @NonNull private HttpStatus httpStatus;

    private String field;

    @NonNull private String message;

}
