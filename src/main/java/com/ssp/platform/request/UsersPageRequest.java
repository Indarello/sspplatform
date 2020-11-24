package com.ssp.platform.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 */
@Data
public class UsersPageRequest
{
    private Integer requestPage = 0;

    private Integer numberOfElements = 10;

    private String type;

    public UsersPageRequest(Integer requestPage, Integer numberOfElements, String type)
    {
        this.requestPage = requestPage;
        this.numberOfElements = numberOfElements;
        this.type = type;
    }
}
