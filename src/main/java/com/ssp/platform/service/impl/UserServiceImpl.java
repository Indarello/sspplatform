package com.ssp.platform.service.impl;

import com.ssp.platform.controller.UserController;
import com.ssp.platform.entity.User;
import com.ssp.platform.property.UserCreateProperty;
import com.ssp.platform.repository.UserRepository;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.service.UserService;
import com.ssp.platform.validate.UserValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Сервис для выполнения операций с данными пользователя.
 * Реализация интерфейса
 */
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final UserValidate userValidate;
    private final PasswordEncoder encoder;

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    public UserServiceImpl(UserCreateProperty userCreateProperty, UserRepository userRepository,
                           UserValidate userValidate, PasswordEncoder encoder) throws IOException
    {
        this.userRepository = userRepository;
        this.userValidate = userValidate;
        this.encoder = encoder;

        FileHandler fh = new FileHandler("./log/UserController/users.txt");
        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.FINE);
        log.addHandler(fh);

        User newUser = new User(userCreateProperty.getUsername(), userCreateProperty.getPassword());
        newUser.setFirstName(userCreateProperty.getFirstName());
        newUser.setLastName(userCreateProperty.getLastName());
        newUser.setPatronymic(userCreateProperty.getPatronymic());

        createUserFromProperties(newUser);
    }

    @Override
    public User save(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Page<User> findAllByRole(Pageable pageable, String role)
    {
        return userRepository.findAllByRole(pageable, role);
    }

    @Override
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByRoleAndStatus(String role, String status)
    {
        return userRepository.findByRoleAndStatus(role, status);
    }

    private void createUserFromProperties(User newUser)
    {
        if(userRepository.existsByUsername(newUser.getUsername())) return;

        userValidate.UserValidateReset(newUser);
        ValidateResponse validateResponse = userValidate.validateEmployeeUser();
        if (!validateResponse.isSuccess())
        {
            log.warning("Данные сотрудника в конфигурации заполнены некорректно:\n  " + validateResponse.getMessage());
            log.warning("[" +newUser.getFirstName());
            return;
        }

        User validUser = userValidate.getUser();
        validUser.setPassword(encoder.encode(validUser.getPassword()));

        try
        {
            userRepository.save(validUser);
        }
        catch (Exception e)
        {
            log.warning("Сохранить данные сотрудника из конфигурации не удалось:\n  " + e.getMessage());
        }
    }
}
