package com.ssp.platform.validate;

import java.util.UUID;
import java.util.regex.*;

public abstract class Validator {

    public boolean isNull(Object object){
        return object == null;
    }

    public boolean isEmpty(String field){
        return field.isEmpty();
    }

    public boolean inBounds(String field, int minBound, int maxBound){
        if (isNull(field)) return true;
        return field.length() >= minBound && field.length() <= maxBound;
    }

    public boolean inBounds(String field, int maxBound){
        if (isNull(field)) return true;
        return field.length() <= maxBound;
    }

    public boolean isMatch(String field, String regex, int flags){
        Pattern fieldPattern = Pattern.compile(regex, flags);
        Matcher matcher = fieldPattern.matcher(field);

        return matcher.matches();
    }

    public boolean onlySpaces(String field){
        Pattern fieldPattern = Pattern.compile("[ ]*");
        Matcher matcher = fieldPattern.matcher(field);

        return matcher.matches();
    }

    public boolean isMatch(String field, String regex){
        if (isNull(field)) return true;

        Pattern fieldPattern = Pattern.compile(regex);
        Matcher matcher = fieldPattern.matcher(field);

        return matcher.matches();
    }
}
