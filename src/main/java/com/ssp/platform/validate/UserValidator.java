package com.ssp.platform.validate;

import com.ssp.platform.entity.User;
import com.ssp.platform.response.ValidatorResponse;
import com.ssp.platform.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.ssp.platform.validate.ValidatorStatus.*;

@Component
public class UserValidator extends com.ssp.platform.validate.Validator {

    private final int MIN_LOGIN_SIZE = 2;

    private final int MAX_LOGIN_SIZE = 30;

    private final int MIN_PASSWORD_SIZE = 8;

    private final int MAX_PASSWORD_SIZE = 20;

    private final int MAX_FIRST_NAME_SIZE = 20;

    private final int MAX_LAST_NAME_SIZE = 20;

    private final int MAX_PATRONYMIC_SIZE = 20;

    private final int MAX_COMPANY_NAME_SIZE = 30;

    private final int MAX_COMPANY_DESCRIPTION_SIZE = 1000;

    private final int MAX_COMPANY_ADDRESS_SIZE = 50;

    private final int MAX_COMPANY_KIND_OF_ACTIVITY_SIZE = 100;

    private final int MAX_COMPANY_TECHNOLOGY_STACK_SIZE = 100;

    private final int MIN_TIN_SIZE = 9; //TIN - taxpayer identification number = индивидуальный номер налогоплательщика (ИНН/УНН)

    private final int MAX_TIN_SIZE = 12;

    private final int MIN_ACCOUNT_NUMBER_SIZE = 20;

    private final int MAX_ACCOUNT_NUMBER_SIZE = 34;

    private final int MIN_PHONE_NUMBER_SIZE = 11;

    private final int MAX_PHONE_NUMBER_SIZE = 20;

    private final int MIN_EMAIL_SIZE = 5;

    private final int MAX_EMAIL_SIZE = 40;

    private final UserServiceImpl userService;

    public UserValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    public ValidatorResponse validateLoginForm(User userEntity)
    {
        ValidatorResponse validatorResponse;

        if ((validatorResponse = validateLoginNoUnique(userEntity.getUsername())).getValidatorStatus() == ERROR)
        {
            return validatorResponse;
        }

        validatorResponse = validatePassword(userEntity.getPassword());

        return validatorResponse;
    }

    public ValidatorResponse validateFirmUser(User userEntity) {
        ValidatorResponse validatorResponse;

        if ((validatorResponse = validateLogin(userEntity.getUsername())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validatePassword(userEntity.getPassword())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateFirstName(userEntity.getFirstName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateLastName(userEntity.getLastName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validatePatronymic(userEntity.getPatronymic())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateCompanyName(userEntity.getFirmName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateCompanyDescription(userEntity.getDescription())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateCompanyAddress(userEntity.getAddress())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateCompanyKindOfActivity(userEntity.getActivity())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateCompanyTechnologyStack(userEntity.getTechnology())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateTIN(userEntity.getInn())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateAccountNumber(userEntity.getAccount())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validatePhoneNumber(userEntity.getTelephone())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateEmail(userEntity.getEmail())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEditFirmUser(User userEntity, User oldUser) {
        ValidatorResponse validatorResponse;

        if (userEntity.getPassword() == null) userEntity.setPassword(oldUser.getPassword());
        else {
            if ((validatorResponse = validatePassword(userEntity.getPassword())).getValidatorStatus() == ERROR) {
                return validatorResponse;
            }
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirstName());
        else if ((validatorResponse = validateFirstName(userEntity.getFirstName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getLastName() == null) userEntity.setLastName(oldUser.getLastName());
        else if ((validatorResponse = validateLastName(userEntity.getLastName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getPatronymic() == null) userEntity.setPatronymic(oldUser.getPatronymic());
        else if ((validatorResponse = validatePatronymic(userEntity.getPatronymic())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirmName());
        else if ((validatorResponse = validateCompanyName(userEntity.getFirmName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getDescription() == null) userEntity.setDescription(oldUser.getDescription());
        else if ((validatorResponse = validateCompanyDescription(userEntity.getDescription())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getAddress() == null) userEntity.setAddress(oldUser.getAddress());
        else if ((validatorResponse = validateCompanyAddress(userEntity.getAddress())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getActivity() == null) userEntity.setActivity(oldUser.getActivity());
        else if ((validatorResponse = validateCompanyKindOfActivity(userEntity.getActivity())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getTechnology() == null) userEntity.setTechnology(oldUser.getTechnology());
        else if ((validatorResponse = validateCompanyTechnologyStack(userEntity.getTechnology())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getInn() == null) userEntity.setInn(oldUser.getInn());
        else if ((validatorResponse = validateTIN(userEntity.getInn())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getAccount() == null) userEntity.setAccount(oldUser.getAccount());
        else if ((validatorResponse = validateAccountNumber(userEntity.getAccount())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getTelephone() == null) userEntity.setTelephone(oldUser.getTelephone());
        else if ((validatorResponse = validatePhoneNumber(userEntity.getTelephone())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getEmail() == null) userEntity.setEmail(oldUser.getEmail());
        if ((validatorResponse = validateEmail(userEntity.getEmail())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEmployeeUser(User userEntity) {
        ValidatorResponse validatorResponse;

        if ((validatorResponse = validateLogin(userEntity.getUsername())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validatePassword(userEntity.getPassword())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateFirstName(userEntity.getFirstName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validateLastName(userEntity.getLastName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if ((validatorResponse = validatePatronymic(userEntity.getPatronymic())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEditEmployeeUser(User userEntity, User oldUser) {
        ValidatorResponse validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (userEntity.getPassword() == null) userEntity.setPassword(oldUser.getPassword());
        else {
            if ((validatorResponse = validatePassword(userEntity.getPassword())).getValidatorStatus() == ERROR) {
                return validatorResponse;
            }
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirstName());
        else if ((validatorResponse = validateFirstName(userEntity.getFirstName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getLastName() == null) userEntity.setLastName(oldUser.getLastName());
        else if ((validatorResponse = validateLastName(userEntity.getLastName())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        if (userEntity.getPatronymic() == null) userEntity.setPatronymic(oldUser.getPatronymic());
        else if ((validatorResponse = validatePatronymic(userEntity.getPatronymic())).getValidatorStatus() == ERROR) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    private ValidatorResponse validateLogin(String login){
        final String FIELD_NAME = "login";

        if (isNull(login)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
        }

        if (isEmpty(login)) {
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
        }

        if (!inBounds(login, MIN_LOGIN_SIZE, MAX_LOGIN_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
        }

        if (!isMatch(login, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
        }

        if (onlySpaces(login)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
        }

        if (userService.existsByUsername(login)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.LOGIN_ALREADY_EXIST_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validatePassword(String password){
        final String FIELD_NAME = "password";

        if (isNull(password)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_PASSWORD_FIELD_ERROR);
        }

        if (isEmpty(password)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_PASSWORD_FIELD_ERROR);
        }

        if (onlySpaces(password)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.ONLY_SPACES_ERROR);
        }

        //FIXME
        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";

        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (Character.isDigit(currentCharacter)) {
                numberPresent = true;
            } else if (Character.isUpperCase(currentCharacter)) {
                upperCasePresent = true;
            } else if (Character.isLowerCase(currentCharacter)) {
                lowerCasePresent = true;
            } else if (specialCharactersString.contains(Character.toString(currentCharacter))) {
                specialCharacterPresent = true;
            }
        }

        if (!(numberPresent || upperCasePresent || lowerCasePresent || specialCharacterPresent)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PASSWORD_SYMBOLS_ERROR);
        }

        if (!inBounds(password, MIN_PASSWORD_SIZE, MAX_PASSWORD_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PASSWORD_SIZE_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateFirstName(String firstName){
        final String FIELD_NAME = "firstName";

        if (isNull(firstName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_FIRST_NAME_FIELD_ERROR);
        }

        if (isEmpty(firstName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_FIRST_NAME_FIELD_ERROR);
        }

        if (!inBounds(firstName, MAX_FIRST_NAME_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_FIRST_NAME_SIZE_ERROR);
        }

        if (!isMatch(firstName, "^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_FIRST_NAME_SYMBOLS_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateLastName(String lastName){
        final String FIELD_NAME = "lastName";

        if (isNull(lastName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_LAST_NAME_FIELD_ERROR);
        }

        if (isEmpty(lastName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_LAST_NAME_FIELD_ERROR);
        }

        if (!inBounds(lastName, MAX_LAST_NAME_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_LAST_NAME_SIZE_ERROR);
        }

        if (!isMatch(lastName, "^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_LAST_NAME_SYMBOLS_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validatePatronymic(String patronymic){
        final String FIELD_NAME = "patronymic";

        if (isNull(patronymic)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(patronymic)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(patronymic, MAX_PATRONYMIC_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PATRONYMIC_SIZE_ERROR);
        }

        if (!isMatch(patronymic, "^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PATRONYMIC_SYMBOLS_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateCompanyName(String companyName){
        final String FIELD_NAME = "companyName";

        if (isNull(companyName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_NAME_FIELD_ERROR);
        }

        if (isEmpty(companyName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_NAME_FIELD_ERROR);
        }

        if (!inBounds(companyName, MAX_COMPANY_NAME_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_NAME_SIZE_ERROR);
        }

        if (companyName.length() != 0 && companyName.charAt(0) == companyName.charAt(companyName.length() - 1) && companyName.charAt(0) == ' '){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_NAME_SYMBOLS_ERROR);
        }

        if (onlySpaces(companyName)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateCompanyDescription(String companyDescription){
        final String FIELD_NAME = "companyDescription";

        if (isNull(companyDescription)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(companyDescription)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(companyDescription, MAX_COMPANY_DESCRIPTION_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_DESCRIPTION_SIZE_ERROR);
        }

        if (onlySpaces(companyDescription)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateCompanyAddress(String companyAddress){
        final String FIELD_NAME = "companyAddress";

        if (isNull(companyAddress)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(companyAddress)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(companyAddress, MAX_COMPANY_ADDRESS_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_ADDRESS_SIZE_ERROR);
        }

        if (!isMatch(companyAddress, "[а-яA-Я0-9 .,!@#№$;%:^?&*()_\\-+={}]+", Pattern.CASE_INSENSITIVE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
        }

        if (companyAddress.length() != 0 && companyAddress.charAt(0) == companyAddress.charAt(companyAddress.length() - 1) && companyAddress.charAt(0) == ' '){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
        }

        if (onlySpaces(companyAddress)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateCompanyKindOfActivity(String kindOfActivity){
        final String FIELD_NAME = "companyKindOfActivity";

        if (isNull(kindOfActivity)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR);
        }

        if (isEmpty(kindOfActivity)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR);
        }

        if (!inBounds(kindOfActivity, MAX_COMPANY_KIND_OF_ACTIVITY_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_KIND_OF_ACTIVITY_SIZE_ERROR);
        }

        if (onlySpaces(kindOfActivity)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateCompanyTechnologyStack(String technologyStack){
        final String FIELD_NAME = "companyTechnologyStack";

        if (isNull(technologyStack)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(technologyStack)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(technologyStack, MAX_COMPANY_TECHNOLOGY_STACK_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_TECHNOLOGY_STACK_SIZE_ERROR);
        }

        if (onlySpaces(technologyStack)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateTIN(String tIN){
        final String FIELD_NAME = "TIN";

        if (isNull(tIN)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_TIN_FIELD_ERROR);
        }

        if (isEmpty(tIN)) {
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_TIN_FIELD_ERROR);
        }

        if (!inBounds(tIN, MIN_TIN_SIZE, MAX_TIN_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_TIN_SIZE_ERROR);
        }

        if (!isMatch(tIN, "[0-9]+[A-Z]*")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_TIN_ERROR);
        }

        if (userService.existsByInn(tIN)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.TIN_ALREADY_EXIST_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateAccountNumber(String accountNumber){
        final String FIELD_NAME = "accountNumber";

        if (isNull(accountNumber)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(accountNumber)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(accountNumber, MIN_ACCOUNT_NUMBER_SIZE, MAX_ACCOUNT_NUMBER_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_ACCOUNT_NUMBER_SIZE_ERROR);
        }

        if (!isMatch(accountNumber, "[A-Z0-9]+")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ACCOUNT_NUMBER_ERROR);
        }

        if (isExist(accountNumber)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ACCOUNT_NUMBER_ALREADY_EXIST_ERROR);
        }

        if (onlySpaces(accountNumber)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validatePhoneNumber(String phoneNumber){
        final String FIELD_NAME = "phoneNumber";

        if (isNull(phoneNumber)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (isEmpty(phoneNumber)){
            return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
        }

        if (!inBounds(phoneNumber, MIN_PHONE_NUMBER_SIZE, MAX_PHONE_NUMBER_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PHONE_NUMBER_SIZE_ERROR);
        }

        if (!isMatch(phoneNumber, "([+]{0,1}[0-9]{1,3}\\([0-9]{2,3}\\)[0-9]+\\-[0-9]+\\-[0-9]+)|([+]{0,1}[0-9]{1,12})")){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_PHONE_NUMBER_ERROR);
        }

        if (onlySpaces(phoneNumber)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        if (userService.existsByPhoneNumber(phoneNumber)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateEmail(String email){
        final String FIELD_NAME = "email";

        if (isNull(email)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_EMAIL_FIELD_ERROR);
        }

        if (isEmpty(email)) {
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_EMAIL_FIELD_ERROR);
        }

        if (!inBounds(email, MIN_EMAIL_SIZE, MAX_EMAIL_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_EMAIL_SIZE_ERROR);
        }

        if (onlySpaces(email)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
        }

        if (!isMatch(email, "[a-z0-9._\\-]{1,25}\\@[a-z0-9._\\-]{2,25}\\.[a-z0-9._\\-]{1,7}", Pattern.CASE_INSENSITIVE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_EMAIL_ERROR);
        }

        if (userService.existsByEmail(email)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMAIL_ALREADY_EXIST_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }

    private ValidatorResponse validateLoginNoUnique(String login){
        final String FIELD_NAME = "login";

        if (isNull(login)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
        }

        if (isEmpty(login)) {
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
        }

        if (!inBounds(login, MIN_LOGIN_SIZE, MAX_LOGIN_SIZE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
        }

        if (!isMatch(login, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
        }

        if (onlySpaces(login)){
            return new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
        }

        return new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);
    }
}

