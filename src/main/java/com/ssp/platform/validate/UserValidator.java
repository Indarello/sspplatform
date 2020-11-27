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

    private final int MAX_PHONE_NUMBER_SIZE = 12;

    private final int MIN_EMAIL_SIZE = 3;

    private final int MAX_EMAIL_SIZE = 50;

    private final UserServiceImpl userService;

    private ValidatorResponse validatorResponse;

    public UserValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    public ValidatorResponse validateLoginForm(User userEntity) {
        if (!validateLoginNoUnique(userEntity.getUsername())) {
            return validatorResponse;
        }

        validatePassword(userEntity.getPassword());

        return validatorResponse;
    }

    public ValidatorResponse validateFirmUser(User userEntity) {

        if (!validateLogin(userEntity.getUsername())){
            return validatorResponse;
        }

        if (!validatePassword(userEntity.getPassword())) {
            return validatorResponse;
        }

        if (!validateFirstName(userEntity.getFirstName())) {
            return validatorResponse;
        }

        if (!validateLastName(userEntity.getLastName())) {
            return validatorResponse;
        }

        if (!validatePatronymic(userEntity.getPatronymic())) {
            return validatorResponse;
        }

        if (!validateCompanyName(userEntity.getFirmName())) {
            return validatorResponse;
        }

        if (!validateCompanyDescription(userEntity.getDescription())) {
            return validatorResponse;
        }

        if (!validateCompanyAddress(userEntity.getAddress())) {
            return validatorResponse;
        }

        if (!validateCompanyKindOfActivity(userEntity.getActivity())) {
            return validatorResponse;
        }

        if (!validateCompanyTechnologyStack(userEntity.getTechnology())) {
            return validatorResponse;
        }

        if (!validateTIN(userEntity.getInn())) {
            return validatorResponse;
        }

        if (!validateAccountNumber(userEntity.getAccount())) {
            return validatorResponse;
        }

        if (!validatePhoneNumber(userEntity.getTelephone())) {
            return validatorResponse;
        }

        if (!validateEmail(userEntity.getEmail())) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEditFirmUser(User userEntity, User oldUser) {

        if (userEntity.getPassword() == null) userEntity.setPassword(oldUser.getPassword());
        else {
            if (!validatePassword(userEntity.getPassword())) {
                return validatorResponse;
            }
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirstName());
        else if (!validateFirstName(userEntity.getFirstName())) {
            return validatorResponse;
        }

        if (userEntity.getLastName() == null) userEntity.setLastName(oldUser.getLastName());
        else if (!validateLastName(userEntity.getLastName())) {
            return validatorResponse;
        }

        if (userEntity.getPatronymic() == null) userEntity.setPatronymic(oldUser.getPatronymic());
        else if (!validatePatronymic(userEntity.getPatronymic())) {
            return validatorResponse;
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirmName());
        else if (!validateCompanyName(userEntity.getFirmName())) {
            return validatorResponse;
        }

        if (userEntity.getDescription() == null) userEntity.setDescription(oldUser.getDescription());
        else if (!validateCompanyDescription(userEntity.getDescription())) {
            return validatorResponse;
        }

        if (userEntity.getAddress() == null) userEntity.setAddress(oldUser.getAddress());
        else if (!validateCompanyAddress(userEntity.getAddress())) {
            return validatorResponse;
        }

        if (userEntity.getActivity() == null) userEntity.setActivity(oldUser.getActivity());
        else if (!validateCompanyKindOfActivity(userEntity.getActivity())) {
            return validatorResponse;
        }

        if (userEntity.getTechnology() == null) userEntity.setTechnology(oldUser.getTechnology());
        else if (!validateCompanyTechnologyStack(userEntity.getTechnology())) {
            return validatorResponse;
        }

        if (userEntity.getInn() == null) userEntity.setInn(oldUser.getInn());
        else if (!validateTIN(userEntity.getInn())) {
            return validatorResponse;
        }

        if (userEntity.getAccount() == null) userEntity.setAccount(oldUser.getAccount());
        else if (!validateAccountNumber(userEntity.getAccount())) {
            return validatorResponse;
        }

        if (userEntity.getTelephone() == null) userEntity.setTelephone(oldUser.getTelephone());
        else if (!validatePhoneNumber(userEntity.getTelephone())) {
            return validatorResponse;
        }

        if (userEntity.getEmail() == null) userEntity.setEmail(oldUser.getEmail());
        if (!validateEmail(userEntity.getEmail())) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEmployeeUser(User userEntity) {

        if (!validateLogin(userEntity.getUsername())) {
            return validatorResponse;
        }

        if (!validatePassword(userEntity.getPassword())) {
            return validatorResponse;
        }

        if (!validateFirstName(userEntity.getFirstName())) {
            return validatorResponse;
        }

        if (!validateLastName(userEntity.getLastName())) {
            return validatorResponse;
        }

        if (!validatePatronymic(userEntity.getPatronymic())) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    public ValidatorResponse validateEditEmployeeUser(User userEntity, User oldUser) {
        ValidatorResponse validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (userEntity.getPassword() == null) userEntity.setPassword(oldUser.getPassword());
        else {
            if (!validatePassword(userEntity.getPassword())) {
                return validatorResponse;
            }
        }

        if (userEntity.getFirmName() == null) userEntity.setFirstName(oldUser.getFirstName());
        else if (!validateFirstName(userEntity.getFirstName())) {
            return validatorResponse;
        }

        if (userEntity.getLastName() == null) userEntity.setLastName(oldUser.getLastName());
        else if (!validateLastName(userEntity.getLastName())) {
            return validatorResponse;
        }

        if (userEntity.getPatronymic() == null) userEntity.setPatronymic(oldUser.getPatronymic());
        else if (!validatePatronymic(userEntity.getPatronymic())) {
            return validatorResponse;
        }

        return validatorResponse;
    }

    private boolean validateLogin(String login){
        final String FIELD_NAME = "login";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(login)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return false;
        }

        if (isEmpty(login)) {
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(login)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return false;
        }

        if (!inBounds(login, MIN_LOGIN_SIZE, MAX_LOGIN_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
            return false;
        }

        if (!isMatch(login, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return false;
        }

        if (userService.existsByUsername(login)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.LOGIN_ALREADY_EXIST_ERROR);
        }

        return true;
    }

    private boolean validatePassword(String password){
        final String FIELD_NAME = "password";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(password)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_PASSWORD_FIELD_ERROR);
            return false;
        }

        if (isEmpty(password)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_PASSWORD_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(password)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(password, MIN_PASSWORD_SIZE, MAX_PASSWORD_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PASSWORD_SIZE_ERROR);
            return false;
        }

        if (!inBounds(password, MIN_PASSWORD_SIZE, MAX_PASSWORD_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PASSWORD_SIZE_ERROR);
            return false;
        }

        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        String specialCharactersString = "!@#$%&*()'+,-.\\/:;<=>?[]^_`{|}";

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

        if (!((numberPresent || specialCharacterPresent) && upperCasePresent && lowerCasePresent)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PASSWORD_SYMBOLS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateFirstName(String firstName){
        final String FIELD_NAME = "firstName";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(firstName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_FIRST_NAME_FIELD_ERROR);
            return false;
        }

        if (isEmpty(firstName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_FIRST_NAME_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(firstName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(firstName, MAX_FIRST_NAME_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_FIRST_NAME_SIZE_ERROR);
            return false;
        }

        if (!isMatch(firstName, "(^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*[^!@#$ %&*()'+,\\- ./:;<=\\>?\\[\\]^_`{\\|}]$)|([А-Яа-яA-Za-z]*)")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_FIRST_NAME_SYMBOLS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateLastName(String lastName){
        final String FIELD_NAME = "lastName";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(lastName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_LAST_NAME_FIELD_ERROR);
            return false;
        }

        if (isEmpty(lastName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_LAST_NAME_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(lastName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(lastName, MAX_LAST_NAME_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_LAST_NAME_SIZE_ERROR);
            return false;
        }

        if (!isMatch(lastName, "(^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*[^!@#$ %&*()'+,\\- ./:;<=\\>?\\[\\]^_`{\\|}]$)|([А-Яа-яA-Za-z]*)")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_LAST_NAME_SYMBOLS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validatePatronymic(String patronymic){
        final String FIELD_NAME = "patronymic";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(patronymic)){
            return true;
        }

        if (isEmpty(patronymic)){
            return true;
        }

        if (onlySpaces(patronymic)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(patronymic, MAX_PATRONYMIC_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PATRONYMIC_SIZE_ERROR);
            return false;
        }

        if (!isMatch(patronymic, "(^[А-Яа-яA-Za-z]+[ -]?[А-Яа-яA-Za-z]*[^!@#$ %&*()'+,\\- ./:;<=\\>?\\[\\]^_`{\\|}]$)|([А-Яа-яA-Za-z]*)")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PATRONYMIC_SYMBOLS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateCompanyName(String companyName){
        final String FIELD_NAME = "companyName";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(companyName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_NAME_FIELD_ERROR);
            return false;
        }

        if (isEmpty(companyName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_NAME_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(companyName)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(companyName, MAX_COMPANY_NAME_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_NAME_SIZE_ERROR);
            return false;
        }

        if (companyName.length() != 0 && companyName.charAt(0) == companyName.charAt(companyName.length() - 1) && companyName.charAt(0) == ' '){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_NAME_SYMBOLS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateCompanyDescription(String companyDescription){
        final String FIELD_NAME = "companyDescription";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(companyDescription)){
            return true;
        }

        if (isEmpty(companyDescription)){
            return true;
        }

        if (onlySpaces(companyDescription)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(companyDescription, MAX_COMPANY_DESCRIPTION_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_DESCRIPTION_SIZE_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateCompanyAddress(String companyAddress){
        final String FIELD_NAME = "companyAddress";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(companyAddress)){
            return true;
        }

        if (isEmpty(companyAddress)){
            return true;
        }

        if (onlySpaces(companyAddress)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(companyAddress, MAX_COMPANY_ADDRESS_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_ADDRESS_SIZE_ERROR);
            return false;
        }

        if (!isMatch(companyAddress, "[A-Za-zа-яA-Я0-9 .,!@#№$;%:^?&*()_/\\-+={}]+", Pattern.CASE_INSENSITIVE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
            return false;
        }

        if (companyAddress.length() != 0 && companyAddress.charAt(0) == companyAddress.charAt(companyAddress.length() - 1) && companyAddress.charAt(0) == ' '){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ADDRESS_ERROR);
            return false;
        }
        return true;
    }

    private boolean validateCompanyKindOfActivity(String kindOfActivity){
        final String FIELD_NAME = "companyKindOfActivity";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(kindOfActivity)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR);
            return false;
        }

        if (isEmpty(kindOfActivity)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_COMPANY_KIND_OF_ACTIVITY_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(kindOfActivity)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(kindOfActivity, MAX_COMPANY_KIND_OF_ACTIVITY_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_KIND_OF_ACTIVITY_SIZE_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateCompanyTechnologyStack(String technologyStack){
        final String FIELD_NAME = "companyTechnologyStack";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(technologyStack)){
            return true;
        }

        if (isEmpty(technologyStack)){
            return true;
        }

        if (onlySpaces(technologyStack)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(technologyStack, MAX_COMPANY_TECHNOLOGY_STACK_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_COMPANY_TECHNOLOGY_STACK_SIZE_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateTIN(String tIN){
        final String FIELD_NAME = "TIN";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(tIN)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_TIN_FIELD_ERROR);
            return false;
        }

        if (isEmpty(tIN)) {
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_TIN_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(tIN)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(tIN, MIN_TIN_SIZE, MAX_TIN_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_TIN_SIZE_ERROR);
            return false;
        }

        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;

        for (int i = 0; i < tIN.length(); i++) {
            currentCharacter = tIN.charAt(i);
            if (Character.isDigit(currentCharacter)) {
                numberPresent = true;
            } else if (Character.isUpperCase(currentCharacter)) {
                upperCasePresent = true;
            }
        }

        if ((!numberPresent && upperCasePresent)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_TIN_ERROR);
            return false;
        }

        if (!isMatch(tIN, "[A-Z0-9]*")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_TIN_ERROR);
            return false;
        }

        if (userService.existsByInn(tIN)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.TIN_ALREADY_EXIST_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateAccountNumber(String accountNumber){
        final String FIELD_NAME = "accountNumber";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(accountNumber)){
            return true;
        }

        if (isEmpty(accountNumber)){
            return true;
        }

        if (onlySpaces(accountNumber)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(accountNumber, MIN_ACCOUNT_NUMBER_SIZE, MAX_ACCOUNT_NUMBER_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_ACCOUNT_NUMBER_SIZE_ERROR);
            return false;
        }

        if (!isMatch(accountNumber, "[A-Z0-9]+")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_ACCOUNT_NUMBER_ERROR);
            return false;
        }

        return true;
    }

    private boolean validatePhoneNumber(String phoneNumber){
        final String FIELD_NAME = "phoneNumber";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(phoneNumber)){
            return true;
        }

        if (isEmpty(phoneNumber)){
            return true;
        }

        if (onlySpaces(phoneNumber)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(phoneNumber, MIN_PHONE_NUMBER_SIZE, MAX_PHONE_NUMBER_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_PHONE_NUMBER_SIZE_ERROR);
            return false;
        }

        if (!isMatch(phoneNumber, "[0-9]*")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_PHONE_NUMBER_ERROR);
            return false;
        }

        if (userService.existsByPhoneNumber(phoneNumber)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email){
        final String FIELD_NAME = "email";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(email)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_EMAIL_FIELD_ERROR);
            return false;
        }

        if (isEmpty(email)) {
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMPTY_EMAIL_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(email)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (!inBounds(email, MIN_EMAIL_SIZE, MAX_EMAIL_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_EMAIL_SIZE_ERROR);
            return false;
        }

        if (!isMatch(email, ".{1,25}\\@.{2,15}\\..{2,7}")){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_EMAIL_MASK_SIZE_ERROR);
            return false;
        }

        if (!isMatch(email, "[a-z0-9._\\-]{1,25}\\@[a-z0-9._\\-]{2,15}\\.[a-z0-9._\\-]{2,7}", Pattern.CASE_INSENSITIVE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.WRONG_SYMBOLS_IN_EMAIL_ERROR);
            return false;
        }

        if (userService.existsByEmail(email)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME,  ValidatorMessages.EMAIL_ALREADY_EXIST_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateLoginNoUnique(String login){
        final String FIELD_NAME = "login";
        validatorResponse = new ValidatorResponse(OK, HttpStatus.OK, ValidatorMessages.OK);

        if (isNull(login)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return false;
        }

        if (isEmpty(login)) {
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.EMPTY_LOGIN_FIELD_ERROR);
            return false;
        }

        if (onlySpaces(login)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return false;
        }

        if (!inBounds(login, MIN_LOGIN_SIZE, MAX_LOGIN_SIZE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_LOGIN_SIZE_ERROR);
            return false;
        }

        if (!isMatch(login, "(?!\\d|[ ])\\w+", Pattern.CASE_INSENSITIVE)){
            validatorResponse = new ValidatorResponse(ERROR, HttpStatus.BAD_REQUEST, FIELD_NAME, ValidatorMessages.WRONG_SYMBOLS_IN_LOGIN_ERROR);
            return false;
        }

        return true;
    }
}

