package com.ssp.platform.response;

import com.ssp.platform.entity.enums.SupplyStatus;
import lombok.*;

@Data
public class SupplyResponse {

    private String description;

    private Long createDate;

    private String author;

    private Long budget;

    private String comment;

    private SupplyStatus status;

    private String resultOfConsideration;

    private Long resultDate;

    private FileDTO file;

}
