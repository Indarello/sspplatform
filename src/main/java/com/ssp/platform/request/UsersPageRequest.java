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
    private int requestPage = 0;

    private int numberOfElements = 10;

    private String type;
}
