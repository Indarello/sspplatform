package com.ssp.platform.validate;

public class ValidatorMessages {

    public static final String OK = "OK";

    //User messages

    public static final String ONLY_SPACES_ERROR = "Поле не может состоять только из пробелов!";

    public static final String WRONG_LOGIN_DATA_ERROR = "Неверный логин или пароль";

    public static final String EMPTY_LOGIN_FIELD_ERROR = "Поле логина не может быть пустым";

    public static final String WRONG_LOGIN_SIZE_ERROR = "Неправильный размер логина";

    public static final String WRONG_SYMBOLS_IN_LOGIN_ERROR = "Логин может содержать только латиницу или цифры. Цифра не может быть первым символом";

    public static final String LOGIN_ALREADY_EXIST_ERROR = "Пользователь с таким логином уже существует";

    public static final String EMPTY_PASSWORD_FIELD_ERROR = "Поле пароля не может быть пустым";

    public static final String WRONG_PASSWORD_SIZE_ERROR = "Неправильный размер пароля";

    public static final String WRONG_PASSWORD_SYMBOLS_ERROR = "Пароль должен содержать латиницу, цифры, специальные символы и пробелы";

    public static final String EMPTY_FIRST_NAME_FIELD_ERROR = "Поле имени не может быть пустым";

    public static final String WRONG_FIRST_NAME_SIZE_ERROR = "Неправильный размер имени";

    public static final String WRONG_FIRST_NAME_SYMBOLS_ERROR = "Недопустимые символы имени";

    public static final String EMPTY_LAST_NAME_FIELD_ERROR = "Поле фамилии не может быть пустым";

    public static final String WRONG_LAST_NAME_SIZE_ERROR = "Неправильный размер фамилии";

    public static final String WRONG_LAST_NAME_SYMBOLS_ERROR = "Недопустимые символы фамилии";

    public static final String WRONG_PATRONYMIC_SIZE_ERROR = "Неправильный размер отчества";

    public static final String WRONG_PATRONYMIC_SYMBOLS_ERROR = "Недопустимые символы отчества";

    public static final String EMPTY_COMPANY_NAME_FIELD_ERROR = "Поле имени компании не может быть пустым";

    public static final String WRONG_COMPANY_NAME_SIZE_ERROR = "Неправильный размер имени компании";

    public static final String WRONG_COMPANY_NAME_SYMBOLS_ERROR = "Недопустимые символы имени компании";

    public static final String WRONG_SYMBOLS_IN_ADDRESS_ERROR = "Адрес может содержать кириллицу, цифры, пробел и специальные символы.";

    public static final String WRONG_COMPANY_ADDRESS_SIZE_ERROR = "Неправильный размер поля адреса";

    public static final String EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR = "Поле деятельности компании не может быть пустым";

    public static final String WRONG_COMPANY_KIND_OF_ACTIVITY_SIZE_ERROR = "Неправильный размер поля деятельности компании";

    public static final String WRONG_COMPANY_TECHNOLOGY_STACK_SIZE_ERROR = "Неправильный размер поля стека технологий";

    public static final String EMPTY_TIN_FIELD_ERROR = "Поле ИНН не может быть пустым";

    public static final String WRONG_TIN_SIZE_ERROR = "Неправильный рамер поля ИНН";

    public static final String WRONG_SYMBOLS_IN_TIN_ERROR = "ИНН должен содержать только цифры и может содержать латиницу";

    public static final String TIN_ALREADY_EXIST_ERROR = "Пользователь с данным ИНН уже существует";

    public static final String WRONG_ACCOUNT_NUMBER_SIZE_ERROR = "Неправильный размер поля номера счёта";

    public static final String WRONG_SYMBOLS_IN_ACCOUNT_NUMBER_ERROR = "Поле номера счёта может содержать латиницу и цифры";

    public static final String ACCOUNT_NUMBER_ALREADY_EXIST_ERROR = "Пользователь с данным номером счёта уже существует";

    public static final String WRONG_PHONE_NUMBER_SIZE_ERROR = "Неправильный размер поля номера телефона";

    public static final String WRONG_SYMBOLS_IN_PHONE_NUMBER_ERROR = "Номер телефона может содержать только цифры, \"+\", \"(\", \")\" and \"-\".";

    public static final String PHONE_NUMBER_ALREADY_EXIST_ERROR = "Пользователь с данным номером телефона уже существует";

    public static final String EMPTY_EMAIL_FIELD_ERROR = "Поле e-mail не может быть пустым";

    public static final String WRONG_EMAIL_SIZE_ERROR = "Неправильный размер поля e-mail";

    public static final String WRONG_SYMBOLS_IN_EMAIL_ERROR = "E-mail должен содержать только латиницу, цифры, \"_\", \"-\", \".\" и \"@\".";

    public static final String EMAIL_ALREADY_EXIST_ERROR = "Пользователь с данным e-mail уже существует!";

    public static final String WRONG_COMPANY_DESCRIPTION_SIZE_ERROR = "Неправильный размер поля описания компании";

    //Question messages

    public static final String WRONG_QUESTION_ID_ERROR = "This question doesn't exist!";

    public static final String EMPTY_QUESTION_PURCHASE_ID_ERROR = "Question purchase ID must be not null!";

    public static final String WRONG_QUESTION_PURCHASE_ID_ERROR = "This question purchase ID doesn't exist!";

    public static final String EMPTY_QUESTION_NAME_ERROR = "Question name must be not null!";

    public static final String EMPTY_QUESTION_CONTENT_ERROR = "Question content must be not null!";

    //Answer messages

    public static final String WRONG_ANSWER_ID_ERROR = "This answer doesn't exist!";

    public static final String EMPTY_ANSWER_QUESTION_ID_ERROR = "Answer question ID must be not null!";

    public static final String WRONG_ANSWER_QUESTION_ID_ERROR = "This answer question ID doesn't exist!";

    public static final String EMPTY_ANSWER_CONTENT_ERROR = "Answer content must be not null!";

    public static final String EMPTY_ANSWER_PUBLICITY_ERROR = "Answer publicity must be not null!";

    //Supply messages

    public static final String WRONG_SUPPLY_ID_ERROR = "This supply doesn't exist!";

    public static final String EMPTY_SUPPLY_ID_ERROR = "Supply ID must be not null!";

    public static final String EMPTY_PURCHASE_ID_ERROR = "Purchase ID must be not null!";

    public static final String WRONG_PURCHASE_ID_ERROR = "This purchase doesn't exist!";

    public static final String EMPTY_COST_ERROR = "Cost must be not null!";

    public static final String EMPTY_T_STACK_ERROR = "Technology stack must be not null!";

    public static final String EMPTY_STRUCTURE_ERROR = "Structure must be not null!";

}
