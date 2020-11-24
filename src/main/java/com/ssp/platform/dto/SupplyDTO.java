package com.ssp.platform.dto;

import lombok.*;

@Data
public class SupplyDTO {

    @NonNull
    private long cost;

    @NonNull
    private String tStack;

    @NonNull
    private String structure;

    @NonNull
    private String comment;
}
