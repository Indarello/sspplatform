package com.ssp.platform.request;

import com.ssp.platform.entity.enums.SupplyStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SupplyUpdateRequest {

    String description;

    Long budget;

    String comment;

    SupplyStatus status;

    String result;

    public SupplyUpdateRequest(String description, Long budget, String comment){
        this.description = description;
        this.budget = budget;
        this.comment = comment;
    }

    public SupplyUpdateRequest(SupplyStatus status, String result) {
        this.status = status;
        this.result = result;
    }
}
