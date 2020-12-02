package com.ssp.platform.request;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SupplyPageRequest {
    UUID purchaseId;
    Integer pageIndex;
    Integer pageSize;
}
