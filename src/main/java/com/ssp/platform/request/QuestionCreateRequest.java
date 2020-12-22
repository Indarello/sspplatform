package com.ssp.platform.request;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionCreateRequest {

    String name;

    String description;

    UUID purchaseId;

}
