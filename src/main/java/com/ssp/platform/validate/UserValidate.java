package com.ssp.platform.validate;

import com.ssp.platform.entity.User;
import lombok.Data;

@Data
public class UserValidate
{
    private User user;
    private String checkResult = "ok";
    private boolean foundInvalid = false;

    public UserValidate(User user)
    {
        this.checkResult = "ok";
        this.foundInvalid = false;
        this.user = user;
    }

    /**
     * Валидация при регистрации пользователя сотрудника
     */
    public String validateEmployeeUser()
    {
        checkUsername();                        //проверка параметра username
        if(foundInvalid) return checkResult;

        checkPatronymic();                      //проверка параметра Отчества
        if(foundInvalid) return checkResult;

        user.setInn("");                        //у сотрудника нет Inn, по паттерну хранить null в бд не стоит
        //TODO:все остальные проверки

        return checkResult;
    }

    /**
     * Валидация при регистрации пользователя поставщика
     */
    public String validateFirmUser()
    {
        checkUsername();                //проверка параметра username
        if(foundInvalid) return checkResult;

        checkPatronymic();              //проверка параметра Отчества
        if(foundInvalid) return checkResult;

        checkInn();                     //проверка параметра INN, в отличии от сотрудника - у поставщика есть ИНН
        if(foundInvalid) return checkResult;
        //TODO:все остальные проверки

        return checkResult;
    }

    /**
     * Валидация при изменении пользователя поставщика
     */
    public String validateEditFirmUser(User oldUser)
    {
        //логин проверили в контроллере, его нельзя изменить
        if(user.getPassword() == null) user.setPassword(oldUser.getPassword());         //Параметр пароль не предоставлен, выставляем старое
        else                                                                            //проверка параметра пароль
        {
            checkPassword();
            if(foundInvalid) return checkResult;
        }

        if(user.getFirstName() == null) user.setFirstName(oldUser.getFirstName());
        else
        {
            //else checkFirstName();
            if(foundInvalid) return checkResult;
        }

        if(user.getLastName() == null) user.setLastName(oldUser.getLastName());
        else
        {
            //else checkLastName();
            if(foundInvalid) return checkResult;
        }

        if(user.getPatronymic() == null) user.setPatronymic(oldUser.getPatronymic());
        else
        {
            checkPatronymic();
            if(foundInvalid) return checkResult;
        }

        if(user.getFirmName() == null) user.setFirmName(oldUser.getFirmName());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getDescription() == null) user.setDescription(oldUser.getDescription());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getAddress() == null) user.setAddress(oldUser.getAddress());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getActivity() == null) user.setActivity(oldUser.getActivity());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getTechnology() == null) user.setTechnology(oldUser.getTechnology());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getInn() == null) user.setInn(oldUser.getInn());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getAccount() == null) user.setAccount(oldUser.getAccount());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getTelephone() == null) user.setTelephone(oldUser.getTelephone());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getEmail() == null) user.setEmail(oldUser.getEmail());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }

        if(user.getStatus() == null) user.setStatus(oldUser.getStatus());
        else
        {
            //check
            if(foundInvalid) return checkResult;
        }



        user.setRole("firm");                                                           //роль поменять нельзя

        return checkResult;
    }

    /**
     * Валидация при изменении пользователя сотрудника
     */
    public String validateEditEmployeeUser(User oldUser)
    {
        //логин проверили в контроллере, его нельзя изменить
        if(user.getPassword() == null) user.setPassword(oldUser.getPassword());         //Параметр пароль не предоставлен, выставляем старое
        else                                                                            //проверка параметра пароль
        {
            checkPassword();
            if(foundInvalid) return checkResult;
        }

        if(user.getFirstName() == null) user.setFirstName(oldUser.getFirstName());
        else
        {
            //else checkFirstName();
            if(foundInvalid) return checkResult;
        }

        if(user.getLastName() == null) user.setLastName(oldUser.getLastName());
        else
        {
            //else checkLastName();
            if(foundInvalid) return checkResult;
        }

        if(user.getPatronymic() == null) user.setPatronymic(oldUser.getPatronymic());
        else
        {
            checkPatronymic();
            if(foundInvalid) return checkResult;
        }

        user.setFirmName("");                                                           //у сотрудника нет всех этих параметров
        user.setDescription("");
        user.setAddress("");
        user.setActivity("");
        user.setTechnology("");
        user.setInn("");
        user.setAccount("");
        user.setTelephone("");
        user.setEmail("");
        user.setRole("employee");
        user.setStatus("Approved");



        return checkResult;
    }

    /**
     * Приватный метод проверки username
     * Необходимо вынести так как будет использоваться при validateFirmUser() и при validateEmployeeUser()
     * Те при проверки регистрации нового поставщика и при создании сотрудника
     */
    private void checkUsername()
    {
        String checkString = user.getUsername();
        if (checkString == null)                                       //Параметр username обязателен
        {
            setCheckResult("Параметр username не предоставлен");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 2 || checkLength > 30)
        {
            setCheckResult("Параметр username должен быть от 2 до 30 символов");
            return;
        }

        //TODO сюда проверку на уникальность в бд
    }

    /**
     * Приватный метод проверки пароля
     */
    private void checkPassword()
    {
        String checkString = user.getPassword();

        //TODO сделать
    }

    /**
     * Приватный метод проверки Отчетства
     * Необходимо вынести так как будет использоваться при validateFirmUser() и при validateEmployeeUser()
     * Те при проверки регистрации нового поставщика и при создании сотрудника
     */
    private void checkPatronymic()
    {
        String checkString = user.getPatronymic();
        if (checkString == null)                                       //если отчество все таки предоставили - его надо проверить
        {
            user.setPatronymic("");                     //параметр отчество не обязателен, по паттерну хранить null в бд не стоит
        }

        else if (!checkString.equals(""))                //если отчество все таки предоставили - его надо проверить
        {
            int checkLength = checkString.length();
            if (checkLength < 1 || checkLength > 20)
            {
                setCheckResult("Параметр Отчество должен быть от 1 до 20 символов");
                return;
            }
        }
    }

    /**
     * Приватный метод проверки Инн
     */
    private void checkInn()
    {
        /*
        BigInteger checkBigInteger = user.getInn();
        if (checkBigInteger == null)                                       //Параметр ИНН для поставщика обязателен
        {
            setCheckResult("Параметр ИНН не предоставлен";
            return;
        }

        if (checkBigInteger.compareTo(BigInteger.valueOf(9)) < 0 || checkBigInteger.compareTo(BigInteger.valueOf(13)) > 0)
        {
            setCheckResult("Параметр ИНН должен быть от 9 до 13 символов";   //Уточню у аналитика
            return;
        }
        */

        //TODO сюда проверку на уникальность в бд
    }

    /**
     * Статичный метод проверки username
     * Необходим для метода получения информации по 1 пользователю
     */
    public static String validateUsername(String username)
    {
        if (username == null)
        {
            return "Параметр username не предоставлен";
        }

        int checkLength = username.length();
        if (checkLength < 2 || checkLength > 30)
        {
            return "Параметр username должен быть от 2 до 30 символов";
        }

        return "ok";
    }

    private void setCheckResult(String result)
    {
        foundInvalid = true;
        checkResult = result;
    }
}



        /*
        //TODO VALIDATE
        if (userRepository.existsByUsername(ObjUser.getUsername()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(ObjUser.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Error: Email is already taken!"));
        }
         */