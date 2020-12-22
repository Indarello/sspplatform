package com.ssp.platform.request;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AnswerRequest {

    private UUID id;

    private String description;

    private String publicity;

}
