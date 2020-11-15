package com.ssp.platform.validate;

import com.ssp.platform.entity.User;
import com.ssp.platform.request.UsersPageRequest;
import lombok.Data;

import java.math.BigInteger;

@Data
public class UsersPageRequestValidate
{
    private UsersPageRequest usersPageRequest;

    public UsersPageRequestValidate(UsersPageRequest usersPageRequest)
    {
        this.usersPageRequest = usersPageRequest;
    }

    public String ValidateUsersPage()
    {
        if (usersPageRequest.getRequestPage() < 0 || usersPageRequest.getRequestPage() > 100000)
        {
            return "Параметр requestPage может быть только 0-100000";
        }
        if (usersPageRequest.getNumberOfElements() < 1 || usersPageRequest.getNumberOfElements() > 100)
        {
            usersPageRequest.setNumberOfElements(10);
        }

        if (usersPageRequest.getType() == null)
        {
            return "Параметр type не предоставлен";
        }

        if(!usersPageRequest.getType().equals("firm") && !usersPageRequest.getType().equals("employee"))
        {
            return "Параметр type может быть только firm|employee";
        }

        return "ok";
    }
}