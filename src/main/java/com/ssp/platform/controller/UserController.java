package com.ssp.platform.controller;

import com.ssp.platform.entity.User;
import com.ssp.platform.request.UsersPageRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.JwtResponse;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.UserService;
import com.ssp.platform.validate.UserValidate;
import com.ssp.platform.validate.UsersPageValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


//TODO try catch при сохранении
@RestController
public class UserController
{
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserValidate userValidate;
    private boolean existsFirstUser = false;



    @Autowired
    public UserController(
            AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService,
            PasswordEncoder encoder, JwtUtils jwtUtils, UserValidate userValidate
    )
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userValidate = userValidate;
    }

    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader("username") String username, @RequestHeader("password") String password)
    {
        User ObjUser = new User(username, password);
        userValidate.UserValidateReset(ObjUser);
        ValidateResponse validateResponse = userValidate.validateLogin();
        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
    }

    /**
     * Метод регистрации для поставщиков
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registerFirm(@RequestBody User ObjUser)
    {
        userValidate.UserValidateReset(ObjUser);
        ValidateResponse validateResponse = userValidate.validateFirmUser();
        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        User validUser = userValidate.getUser();
        validUser.setPassword(encoder.encode(ObjUser.getPassword()));
        userService.save(validUser);

        return new ResponseEntity<>(new ApiResponse(true, "Вы успешно зарегестрировались, ожидайте аккредитации от сотрудника"), HttpStatus.CREATED);
    }

    /**
     * Метод регистрации нового сотрудника
     */
    @PostMapping("/regemployee")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<?> registerEmployee(@RequestBody User ObjUser)
    {
        userValidate.UserValidateReset(ObjUser);
        ValidateResponse validateResponse = userValidate.validateEmployeeUser();
        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        User validUser = userValidate.getUser();
        validUser.setPassword(encoder.encode(ObjUser.getPassword()));
        userService.save(validUser);

        return new ResponseEntity<>(new ApiResponse(true, "Сотрудник успешно зарегестрирован!"), HttpStatus.CREATED);
    }

    /**
     * Метод регистрации первого админа
     */
    @PostMapping("/regfirstadmin")
    public ResponseEntity<?> registerFirstAdmin(@RequestBody User ObjUser)
    {
        if (existsFirstUser || userService.existsByRole("employee"))
        {
            existsFirstUser = true;
            return new ResponseEntity<>(new ApiResponse(false, "В системе уже есть первый сотрудник"), HttpStatus.NOT_ACCEPTABLE);
        }

        userValidate.UserValidateReset(ObjUser);
        ValidateResponse validateResponse = userValidate.validateEmployeeUser();
        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        User validUser = userValidate.getUser();
        validUser.setPassword(encoder.encode(ObjUser.getPassword()));
        userService.save(validUser);

        return new ResponseEntity<>(new ApiResponse(true, "Первый сотрудник успешно зарегестрирован!"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{type}")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> getUsers(@PathVariable(name = "type") String type,
           @RequestParam(value = "requestPage", required = false) Integer requestPage,
           @RequestParam(value = "numberOfElements", required = false) Integer numberOfElements)
    {
        UsersPageRequest usersPageRequest = new UsersPageRequest(requestPage, numberOfElements, type);
        UsersPageValidate usersPageValidate = new UsersPageValidate(usersPageRequest);
        ValidateResponse validateResponse = usersPageValidate.validateUsersPage();

        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        UsersPageRequest validUsersPageRequest = usersPageValidate.getUsersPageRequest();

        Pageable pageable = PageRequest.of(validUsersPageRequest.getRequestPage(), validUsersPageRequest.getNumberOfElements());

        Page<User> searchResult = userService.findAllByRole(pageable, validUsersPageRequest.getType());


        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{username}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getUser(@PathVariable(name = "username") String username, @RequestHeader("Authorization") String token)
    {
        ValidateResponse validateResponse = userValidate.validateUsernameLogin(username);

        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        User user = userDetailsService.loadUserByToken(token);
        if (user.getRole().equals("firm") && !username.equals(user.getUsername()))
        {
            return new ResponseEntity<>(new ApiResponse(false, "Вы можете смотреть информацию только по своему аккаунту"), HttpStatus.FORBIDDEN);
        }

        Optional<User> searchResult = userService.findByUsername(username);

        if (searchResult.isPresent())
        {
            return new ResponseEntity<>(searchResult.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Пользователь не найден"), HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/user")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> editUser(@RequestBody User ObjUser)
    {
        ValidateResponse validateResponse = userValidate.validateUsernameLogin(ObjUser.getUsername());

        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<User> searchResult = userService.findByUsername(ObjUser.getUsername());

        if (!searchResult.isPresent())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Пользователь не найден"), HttpStatus.NOT_FOUND);
        }

        User oldUser = searchResult.get();
        userValidate.UserValidateReset(ObjUser);

        if (oldUser.getRole().equals("firm"))
        {
            validateResponse = userValidate.validateEditFirmUser(oldUser);
        }
        else
        {
            validateResponse = userValidate.validateEditEmployeeUser(oldUser);
        }

        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        User validUser = userValidate.getUser();
        validUser.setPassword(encoder.encode(ObjUser.getPassword()));

        return new ResponseEntity<>(userService.save(validUser), HttpStatus.OK);
    }

}

