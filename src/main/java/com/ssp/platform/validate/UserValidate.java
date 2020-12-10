package com.ssp.platform.validate;

import com.ssp.platform.entity.User;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.service.UserService;
import com.ssp.platform.validate.ValidatorMessages.UserValidatorMessages;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

//TODO:В будущем если будет время сделать так что бы название полей ValidateResponse совпадало с названием поля user
@Getter
@Component
public class UserValidate extends com.ssp.platform.validate.Validator
{
    private User user;
    private String checkResult = "ok";
    private boolean foundInvalid = false;

    private final UserService userService;

    private final int MIN_LOGIN_SIZE = 2;
    private final int MAX_LOGIN_SIZE = 30;


    public void UserValidateReset(User user)
    {
        this.checkResult = "ok";
        this.foundInvalid = false;
        this.user = user;
    }

    @Autowired
    public UserValidate(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * Валидация при создании пользователя сотрудника
     */
    public ValidateResponse validateEmployeeUser()
    {
        checkUsername();
        if(foundInvalid) return new ValidateResponse(false, "login", checkResult);

        checkPassword();
        if(foundInvalid) return new ValidateResponse(false, "password", checkResult);

        checkFirstName();
        if(foundInvalid) return new ValidateResponse(false, "firstName", checkResult);

        checkLastName();
        if(foundInvalid) return new ValidateResponse(false, "lastName", checkResult);

        checkPatronymic();
        if(foundInvalid) return new ValidateResponse(false, "patronymic", checkResult);

        user.setFirmName("");
        user.setDescription("");
        user.setAddress("");
        user.setActivity("");
        user.setTechnology("");
        user.setInn("");
        user.setTelephone("");
        user.setEmail("");
        user.setRole("employee");
        user.setStatus("Approved");

        return new ValidateResponse(true, "", checkResult);
    }

    /**
     * Валидация при регистрации пользователя поставщика
     */
    public ValidateResponse validateFirmUser()
    {
        checkUsername();
        if(foundInvalid) return new ValidateResponse(false, "login", checkResult);

        checkPassword();
        if(foundInvalid) return new ValidateResponse(false, "password", checkResult);

        checkFirstName();
        if(foundInvalid) return new ValidateResponse(false, "firstName", checkResult);

        checkLastName();
        if(foundInvalid) return new ValidateResponse(false, "lastName", checkResult);

        checkPatronymic();
        if(foundInvalid) return new ValidateResponse(false, "patronymic", checkResult);

        checkFirmName();
        if(foundInvalid) return new ValidateResponse(false, "companyName", checkResult);

        checkDescription();
        if(foundInvalid) return new ValidateResponse(false, "companyDescription", checkResult);

        checkAddress();
        if(foundInvalid) return new ValidateResponse(false, "companyAddress", checkResult);

        checkActivity();
        if(foundInvalid) return new ValidateResponse(false, "companyKindOfActivity", checkResult);

        checkTechnology();
        if(foundInvalid) return new ValidateResponse(false, "companyTechnologyStack", checkResult);

        checkInn();
        if(foundInvalid) return new ValidateResponse(false, "TIN", checkResult);

        checkTelephone();
        if(foundInvalid) return new ValidateResponse(false, "phoneNumber", checkResult);

        checkEmail();
        if(foundInvalid) return new ValidateResponse(false, "email", checkResult);

        user.setRole("firm");
        user.setStatus("NotApproved");

        return new ValidateResponse(true, "", checkResult);
    }

    /**
     * Валидация при авторизации
     */
    public ValidateResponse validateLogin()
    {
        validateUsernameNoUnique();
        if(foundInvalid) return new ValidateResponse(false, "login", checkResult);

        checkPassword();
        if(foundInvalid) return new ValidateResponse(false, "password", checkResult);

        return new ValidateResponse(true, "", checkResult);
    }

    /**
     * Валидация при изменении пользователя поставщика
     */
    public ValidateResponse validateEditFirmUser(User oldUser)
    {
        /*
          Валидация параметров только если они предоставлены и отличны от прошлых
          username, role изменить нельзя
         */
        String checkParameter = user.getPassword();
        String oldParameter = oldUser.getPassword();
        if(checkParameter == null) user.setPassword(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkPassword();
            if(foundInvalid) return new ValidateResponse(false, "password", checkResult);
        }

        checkParameter = user.getFirstName();
        oldParameter = oldUser.getFirstName();
        if(checkParameter == null) user.setFirstName(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkFirstName();
            if(foundInvalid) return new ValidateResponse(false, "firstName", checkResult);
        }

        checkParameter = user.getLastName();
        oldParameter = oldUser.getLastName();
        if(checkParameter == null) user.setLastName(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkLastName();
            if(foundInvalid) return new ValidateResponse(false, "lastName", checkResult);
        }

        checkParameter = user.getPatronymic();
        oldParameter = oldUser.getPatronymic();
        if(checkParameter == null) user.setPatronymic(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkPatronymic();
            if(foundInvalid) return new ValidateResponse(false, "patronymic", checkResult);
        }

        checkParameter = user.getFirmName();
        oldParameter = oldUser.getFirmName();
        if(checkParameter == null) user.setFirmName(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkFirmName();
            if(foundInvalid) return new ValidateResponse(false, "companyName", checkResult);
        }

        checkParameter = user.getDescription();
        oldParameter = oldUser.getDescription();
        if(checkParameter == null) user.setDescription(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkDescription();
            if(foundInvalid) return new ValidateResponse(false, "companyDescription", checkResult);
        }

        checkParameter = user.getAddress();
        oldParameter = oldUser.getAddress();
        if(checkParameter == null) user.setAddress(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkAddress();
            if(foundInvalid) return new ValidateResponse(false, "companyAddress", checkResult);
        }

        checkParameter = user.getActivity();
        oldParameter = oldUser.getActivity();
        if(checkParameter == null) user.setActivity(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkActivity();
            if(foundInvalid) return new ValidateResponse(false, "companyKindOfActivity", checkResult);
        }

        checkParameter = user.getTechnology();
        oldParameter = oldUser.getTechnology();
        if(checkParameter == null) user.setTechnology(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkTechnology();
            if(foundInvalid) return new ValidateResponse(false, "companyTechnologyStack", checkResult);
        }

        checkParameter = user.getInn();
        oldParameter = oldUser.getInn();
        if(checkParameter == null) user.setInn(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkInn();
            if(foundInvalid) return new ValidateResponse(false, "TIN", checkResult);
        }

        checkParameter = user.getTelephone();
        oldParameter = oldUser.getTelephone();
        if(checkParameter == null) user.setTelephone(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkTelephone();
            if(foundInvalid) return new ValidateResponse(false, "phoneNumber", checkResult);
        }

        checkParameter = user.getEmail();
        oldParameter = oldUser.getEmail();
        if(checkParameter == null) user.setEmail(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkEmail();
            if(foundInvalid) return new ValidateResponse(false, "email", checkResult);
        }

        checkParameter = user.getStatus();
        oldParameter = oldUser.getStatus();
        if(checkParameter == null) user.setStatus(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkStatus();
            if(foundInvalid) return new ValidateResponse(false, "status", checkResult);
        }


        user.setRole("firm");

        return new ValidateResponse(true, "", checkResult);
    }

    /**
     * Валидация при изменении пользователя сотрудника
     */
    public ValidateResponse validateEditEmployeeUser(User oldUser)
    {
        /*
          Валидация параметров только если они предоставлены и отличны от прошлых
          username, role, status изменить нельзя
         */

        String checkParameter = user.getPassword();
        String oldParameter = oldUser.getPassword();
        if(checkParameter == null) user.setPassword(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkPassword();
            if(foundInvalid) return new ValidateResponse(false, "password", checkResult);
        }

        checkParameter = user.getFirstName();
        oldParameter = oldUser.getFirstName();
        if(checkParameter == null) user.setFirstName(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkFirstName();
            if(foundInvalid) return new ValidateResponse(false, "firstName", checkResult);
        }

        checkParameter = user.getLastName();
        oldParameter = oldUser.getLastName();
        if(checkParameter == null) user.setLastName(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkLastName();
            if(foundInvalid) return new ValidateResponse(false, "lastName", checkResult);
        }

        checkParameter = user.getPatronymic();
        oldParameter = oldUser.getPatronymic();
        if(checkParameter == null) user.setPatronymic(oldParameter);
        else if(!checkParameter.equals(oldParameter))
        {
            checkPatronymic();
            if(foundInvalid) return new ValidateResponse(false, "patronymic", checkResult);
        }

        user.setFirmName("");
        user.setDescription("");
        user.setAddress("");
        user.setActivity("");
        user.setTechnology("");
        user.setInn("");
        user.setTelephone("");
        user.setEmail("");
        user.setRole("employee");
        user.setStatus("Approved");

        return new ValidateResponse(true, "", checkResult);
    }

    /**
     * Приватный метод проверки username
     * Необходимо вынести так как будет использоваться при validateFirmUser() и при validateEmployeeUser()
     * Те при проверки регистрации нового поставщика и при создании сотрудника
     */
    private void checkUsername()
    {
        String checkString = user.getUsername();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < MIN_LOGIN_SIZE || checkLength > MAX_LOGIN_SIZE)
        {
            setCheckResult(UserValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return;
        }

        if (userService.existsByUsername(checkString))
        {
            setCheckResult(UserValidatorMessages.LOGIN_ALREADY_EXIST_ERROR);
            return;
        }
    }

    /**
     * Приватный метод проверки пароля
     */
    private void checkPassword()
    {
        String checkString = user.getPassword();

        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_PASSWORD_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 8 || checkLength > 20)
        {
            setCheckResult(UserValidatorMessages.WRONG_PASSWORD_SIZE_ERROR);
            return;
        }

        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        String specialCharactersString = "!@#$%&*()'+,-.\\/:;<=>?[]^_`{|}";

        for (int i = 0; i < checkLength; i++)
        {
            currentCharacter = checkString.charAt(i);
            if (Character.isDigit(currentCharacter))
            {
                numberPresent = true;
            }
            else if (Character.isUpperCase(currentCharacter))
            {
                upperCasePresent = true;
            }
            else if (Character.isLowerCase(currentCharacter))
            {
                lowerCasePresent = true;
            }
            else if (specialCharactersString.contains(Character.toString(currentCharacter)))
            {
                specialCharacterPresent = true;
            }
        }

        if (!((numberPresent || specialCharacterPresent) && upperCasePresent && lowerCasePresent))
        {
            setCheckResult(UserValidatorMessages.WRONG_PASSWORD_SYMBOLS_ERROR);
            return;
        }

        if (!isMatch(checkString, "[A-Za-z0-9^!@#$%&\\*()'\\+,\\-\\./:;<=\\>\\?\\[\\]^_`{\\|}\\\\]*"))
        {
            setCheckResult(UserValidatorMessages.WRONG_PASSWORD_SYMBOLS_REGEX);
            return;
        }
    }

    private void checkFirstName()
    {
        String checkString = user.getFirstName();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_FIRST_NAME_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 30)
        {
            setCheckResult(UserValidatorMessages.WRONG_FIRST_NAME_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "[А-ЯёЁа-яA-Za-z]+[ -]?([А-ЯёЁа-яA-Za-z]+)"))
        {
            setCheckResult(UserValidatorMessages.WRONG_FIRST_NAME_SYMBOLS_ERROR);
            return;
        }
    }

    private void checkLastName()
    {
        String checkString = user.getLastName();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_LAST_NAME_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 30)
        {
            setCheckResult(UserValidatorMessages.WRONG_LAST_NAME_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "[А-ЯёЁа-яA-Za-z]+[ -]?([А-ЯёЁа-яA-Za-z]+)"))
        {
            setCheckResult(UserValidatorMessages.WRONG_LAST_NAME_SYMBOLS_ERROR);
            return;
        }
    }

    private void checkPatronymic()
    {
        String checkString = user.getPatronymic();
        if (checkString == null)
        {
            user.setPatronymic("");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength == 0) return;

        if (checkLength > 30)
        {
            setCheckResult(UserValidatorMessages.WRONG_PATRONYMIC_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "[А-ЯёЁа-яA-Za-z]+[ -]?([А-ЯёЁа-яA-Za-z]+)"))
        {
            setCheckResult(UserValidatorMessages.WRONG_PATRONYMIC_SYMBOLS_ERROR);
            return;
        }
    }

    private void checkFirmName()
    {
        String checkString = user.getFirmName();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_COMPANY_NAME_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 30)
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_NAME_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

        if (checkString.charAt(checkLength - 1) == ' ' && checkString.charAt(0) == ' ')
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_NAME_SYMBOLS_ERROR);
            return;
        }

    }

    private void checkDescription()
    {
        String checkString = user.getDescription();
        if (checkString == null)
        {
            user.setDescription("");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength == 0) return;

        if (checkLength > 1000)
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_DESCRIPTION_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

    }

    private void checkAddress()
    {
        String checkString = user.getAddress();
        if (checkString == null)
        {
            user.setAddress("");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength == 0) return;

        if (checkLength > 50)
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_ADDRESS_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

        if (!isMatch(checkString, "[A-Za-zа-яёЁA-Я0-9 .,!@#№$;%:^?&*()_/\\-+={}]+", Pattern.CASE_INSENSITIVE))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
            return;
        }

        if (checkString.charAt(checkLength - 1) == ' ' && checkString.charAt(0) == ' ')
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
            return;
        }
    }

    private void checkActivity()
    {
        String checkString = user.getActivity();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 100)
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_KIND_OF_ACTIVITY_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

    }

    private void checkTechnology()
    {
        String checkString = user.getTechnology();
        if (checkString == null)
        {
            user.setTechnology("");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength == 0) return;

        if (checkLength > 100)
        {
            setCheckResult(UserValidatorMessages.WRONG_COMPANY_TECHNOLOGY_STACK_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

    }

    private void checkInn()
    {
        String checkString = user.getInn();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_TIN_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 9 || checkLength > 12)
        {
            setCheckResult(UserValidatorMessages.WRONG_TIN_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

        if (!isMatch(checkString, "[A-Z0-9]*"))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_TIN_ERROR);
            return;
        }

        if (!isMatch(checkString, "[A-Z0-9]*[0-9]+[A-Z0-9]*"))
        {
            setCheckResult(UserValidatorMessages.NO_NUMBER_IN_TIN_ERROR);
            return;
        }

        if (userService.existsByInn(checkString))
        {
            setCheckResult(UserValidatorMessages.TIN_ALREADY_EXIST_ERROR);
            return;
        }
    }

    private void checkTelephone()
    {
        String checkString = user.getTelephone();
        if (checkString == null)
        {
            user.setTelephone("");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength == 0) return;

        if (checkLength < 11 || checkLength > 17)
        {
            setCheckResult(UserValidatorMessages.WRONG_PHONE_NUMBER_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "[0-9]*"))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_PHONE_NUMBER_ERROR);
            return;
        }

        if (userService.existsByPhoneNumber(checkString))
        {
            setCheckResult(UserValidatorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);
            return;
        }

    }

    private void checkEmail()
    {
        String checkString = user.getEmail();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_EMAIL_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 7 || checkLength > 49)
        {
            setCheckResult(UserValidatorMessages.WRONG_EMAIL_SIZE_ERROR);
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult(UserValidatorMessages.ONLY_SPACES_ERROR);
            return;
        }

        if (!isMatch(checkString, ".*\\@.+\\..+"))
        {
            setCheckResult(UserValidatorMessages.WRONG_EMAIL_MASK_TYPE_ERROR);
            return;
        }

        if (!isMatch(checkString, ".{1,25}\\@.*"))
        {
            setCheckResult(UserValidatorMessages.WRONG_EMAIL_MASK_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, ".{1,25}\\@.{2,15}\\..*"))
        {
            setCheckResult(UserValidatorMessages.WRONG_EMAIL_MASK_SIZE_ERROR2);
            return;
        }

        if (!isMatch(checkString, ".{1,25}\\@.{2,15}\\..{2,7}"))
        {
            setCheckResult(UserValidatorMessages.WRONG_EMAIL_MASK_SIZE_ERROR3);
            return;
        }

        if (!isMatch(checkString, "[a-z0-9._\\-]{1,25}\\@[a-z0-9._\\-]{2,15}\\.[a-z0-9._\\-]{2,7}", Pattern.CASE_INSENSITIVE))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_EMAIL_ERROR);
            return;
        }

        if (userService.existsByEmail(checkString))
        {
            setCheckResult(UserValidatorMessages.EMAIL_ALREADY_EXIST_ERROR);
            return;
        }

    }

    private void checkStatus()
    {
        String checkString = user.getStatus();
        if (!checkString.equals("NotApproved") && !checkString.equals("Approved"))
        {
            setCheckResult(UserValidatorMessages.WRONG_USER_STATUS_ERROR);
            return;
        }

    }

    /**
     * Необходим для валидцаии при авторизации, убрана проверка на уникальность
     */
    private void validateUsernameNoUnique()
    {
        String checkString = user.getUsername();
        if (checkString == null)
        {
            setCheckResult(UserValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < MIN_LOGIN_SIZE || checkLength > MAX_LOGIN_SIZE)
        {
            setCheckResult(UserValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
            return;
        }

        if (!isMatch(checkString, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE))
        {
            setCheckResult(UserValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return;
        }

        return;
    }

    /**
     * Необходим для валидцаии при авторизации, убрана проверка на уникальность
     */
    public ValidateResponse validateUsernameLogin(String checkString)
    {
        if (checkString == null)
        {
            return new ValidateResponse(false, "login", UserValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
        }

        int checkLength = checkString.length();
        if (checkLength < MIN_LOGIN_SIZE || checkLength > MAX_LOGIN_SIZE)
        {
            return new ValidateResponse(false, "login", UserValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
        }

        if (!isMatch(checkString, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE))
        {
            return new ValidateResponse(false, "login", UserValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
        }

        return new ValidateResponse(true, "", checkResult);
    }

    private void setCheckResult(String result)
    {
        foundInvalid = true;
        checkResult = result;
    }
}