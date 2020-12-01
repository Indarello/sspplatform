package com.ssp.platform.validate;

public class ValidatorMessages {

    public static final String ONLY_SPACES_ERROR = "Поле не может состоять только из пробелов!";

    public static final String EMPTY_LOGIN_FIELD_ERROR = "Поле логина должно быть заполнено!";

    public static final String WRONG_LOGIN_SIZE_ERROR = "Логин должен содержать от 2 до 30 символов!";

    public static final String WRONG_SYMBOLS_IN_LOGIN_ERROR = "Логин может содержать только заглавные и строчные латинские буквы и цифры. Цифра не может быть первым символом";

    public static final String LOGIN_ALREADY_EXIST_ERROR = "Данный логин уже занят!";

    public static final String EMPTY_PASSWORD_FIELD_ERROR = "Поле пароля должно быть заполнено!";

    public static final String WRONG_PASSWORD_SIZE_ERROR = "Пароль должен содержать от 8 до 20 символов!";

    public static final String WRONG_PASSWORD_SYMBOLS_ERROR = "Пароль должен содержать хотя бы одну заглавную и строчную латинские буквы, хотя бы одну цифру или символ";

    public static final String EMPTY_FIRST_NAME_FIELD_ERROR = "Поле имени должно быть заполнено!";

    public static final String WRONG_FIRST_NAME_SIZE_ERROR = "В поле имени не может быть больше 20 символов!";

    public static final String WRONG_FIRST_NAME_SYMBOLS_ERROR = "Имя может содержать заглавные и строчные буквы кириллицы или латинского алфавита.";

    public static final String EMPTY_LAST_NAME_FIELD_ERROR = "Поле фамилии должно быть заполнено!";

    public static final String WRONG_LAST_NAME_SIZE_ERROR = "В поле фамилии не может быть больше 20 символов!";

    public static final String WRONG_LAST_NAME_SYMBOLS_ERROR = "Фамилия может содержать заглавные и строчные буквы кириллицы или латинского алфавита.";

    public static final String WRONG_PATRONYMIC_SIZE_ERROR = "В поле отчестве не может быть больше 20 символов!";

    public static final String WRONG_PATRONYMIC_SYMBOLS_ERROR = "Отчество может содержать заглавные и строчные буквы кириллицы или латинского алфавита.";

    public static final String EMPTY_COMPANY_NAME_FIELD_ERROR = "Поле имени компании должно быть заполнено!";

    public static final String WRONG_COMPANY_NAME_SIZE_ERROR = "Имя компании должно содержать не более 30 символов";

    public static final String WRONG_COMPANY_NAME_SYMBOLS_ERROR = "Имя компании может содержать верхний и нижний регистр латиницы или кириллицы, цифры и специальные знаки";

    public static final String WRONG_SYMBOLS_IN_ADDRESS_ERROR = "Адрес может содержать латиницу, кириллицу, цифры, пробел и специальные символы.";

    public static final String WRONG_COMPANY_ADDRESS_SIZE_ERROR = "Адрес компании должен содержать не более 50 символов";

    public static final String EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR = "Поле деятельности компании должно быть заполнено!";

    public static final String WRONG_COMPANY_KIND_OF_ACTIVITY_SIZE_ERROR = "Поле деятельности компании должно содержать не более 100 символов";

    public static final String WRONG_COMPANY_TECHNOLOGY_STACK_SIZE_ERROR = "Поле технологического стека должно содержать не более 100 символов";

    public static final String EMPTY_TIN_FIELD_ERROR = "Поле ИНН должно быть заполнено!";

    public static final String ONLY_LETTERS_TIN_FIELD_ERROR = "Поле ИНН не может содержать только буквы!";

    public static final String WRONG_TIN_SIZE_ERROR = "Поле ИНН/УНН должно содержать от 9 до 12 символов!";

    public static final String WRONG_SYMBOLS_IN_TIN_ERROR = "ИНН/УНН может содержать цифры и латинские заглавные буквы";

    public static final String TIN_ALREADY_EXIST_ERROR = "Пользователь с данным ИНН уже существует";

    public static final String WRONG_PHONE_NUMBER_SIZE_ERROR = "Номер телефона должен содержать не менее 11 символов!";

    public static final String WRONG_SYMBOLS_IN_PHONE_NUMBER_ERROR = "Номер телефона должен содержать только цифры.";

    public static final String PHONE_NUMBER_ALREADY_EXIST_ERROR = "Пользователь с данным номером телефона уже существует";

    public static final String EMPTY_EMAIL_FIELD_ERROR = "Поле e-mail должно быть заполнено!";

    public static final String WRONG_EMAIL_SIZE_ERROR = "E-mail может содержать от 3 до 50 символов!";

    public static final String WRONG_EMAIL_MASK_SIZE_ERROR = "E-mail может содержать до знака \"@\"от 1 до 25 символов, от \"@\" до \".\" от 2 до 15 символов и после \".\" от 2 до 7 символов!";

    public static final String WRONG_SYMBOLS_IN_EMAIL_ERROR = "E-mail должен содержать только латиницу, цифры, \"_\", \"-\", \".\" и \"@\".";

    public static final String EMAIL_ALREADY_EXIST_ERROR = "Пользователь с данным e-mail уже существует!";

    public static final String WRONG_COMPANY_DESCRIPTION_SIZE_ERROR = "Описание компании должно содержать до 1000 символов!";

    public static final String WRONG_USER_STATUS_ERROR = "Некорректный статус пользователя, он может быть только NotApproved|Approved!";
}
