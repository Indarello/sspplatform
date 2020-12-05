package com.ssp.platform.request;

import com.ssp.platform.entity.enums.SupplyStatus;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SupplyUpdateRequest {

    String description;

    Long budget;

    String comment;

    SupplyStatus status;

    String result;

    MultipartFile[] files;

}
