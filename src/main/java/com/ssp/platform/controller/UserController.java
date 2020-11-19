package com.ssp.platform.controller;

import com.ssp.platform.entity.User;
import com.ssp.platform.request.LoginRequest;
import com.ssp.platform.request.UsersPageRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.JwtResponse;
import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.UserService;
import com.ssp.platform.validate.UserValidate;
import com.ssp.platform.validate.UsersPageRequestValidate;
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
import javax.validation.Valid;
import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
//TODO VALIDATE
//TODO user.setPassword("[PROTECTED]");
//TODO try catch при сохранении
@RestController
public class UserController
{
    final AuthenticationManager authenticationManager;
    final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    final PasswordEncoder encoder;
    final JwtUtils jwtUtils;
    boolean existsFirstUser = false;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService, PasswordEncoder encoder, JwtUtils jwtUtils)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
    }

/*
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody User ObjUser)
    {
        UserValidate userValidate = new UserValidate(ObjUser);

        String checkResult = userValidate.validateFirmUser();
        if(!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        User validUser = userValidate.getUser();

        ObjUser.setPassword(encoder.encode(validUser.getPassword()));         //пароли шифруются
        userRepository.save(validUser);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully!"));
    }
*/


    /**
     * Метод регистрации для поставщиков
     */

    @PostMapping("/registration")
    public ResponseEntity<?> registerFirm(@RequestBody User ObjUser)
    {
        User user = new User(ObjUser.getUsername(), encoder.encode(ObjUser.getPassword()),
                ObjUser.getFirstName(),ObjUser.getLastName(),ObjUser.getFirmName(),
                ObjUser.getActivity(),ObjUser.getInn(),ObjUser.getEmail());

        if(ObjUser.getPatronymic() != null) user.setPatronymic(ObjUser.getPatronymic());
        if(ObjUser.getDescription() != null) user.setDescription(ObjUser.getDescription());
        if(ObjUser.getAddress() != null) user.setAddress(ObjUser.getAddress());
        if(ObjUser.getTechnology() != null) user.setTechnology(ObjUser.getTechnology());
        if(ObjUser.getAccount() != null) user.setAccount(ObjUser.getAccount());

        //user.setPassword(encoder.encode(ObjUser.getPassword()));    //потом сделаю валидацию, уберу конструктор и разкомментирую это
        userService.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "Вы успешно зарегестрировались, ожидайте аккредитации от сотрудника"), HttpStatus.CREATED);
    }

    /**
     * Метод регистрации нового сотрудника
     */
    @PostMapping("/regemployee")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<?> registerEmployee(@RequestBody User ObjUser)
    {
        User user = new User(ObjUser.getUsername(), encoder.encode(ObjUser.getPassword()),
                ObjUser.getFirstName(),ObjUser.getLastName());

        if(ObjUser.getPatronymic() != null) user.setPatronymic(ObjUser.getPatronymic());

        //user.setPassword(encoder.encode(ObjUser.getPassword()));    //потом сделаю валидацию, уберу конструктор и разкомментирую это

        userService.save(user);

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

        User user = new User(ObjUser.getUsername(), encoder.encode(ObjUser.getPassword()),
                ObjUser.getFirstName(), ObjUser.getLastName());

        if(ObjUser.getPatronymic() != null) user.setPatronymic(ObjUser.getPatronymic());

        //user.setPassword(encoder.encode(ObjUser.getPassword()));    //потом сделаю валидацию, уберу конструктор и разкомментирую это

        userService.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "Первый сотрудник успешно зарегестрирован!"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{type}")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> getUsers(@PathVariable(name = "type") String type, @RequestBody UsersPageRequest userPageRequest)
    {
        userPageRequest.setType(type);
        UsersPageRequestValidate usersPageValidate = new UsersPageRequestValidate(userPageRequest);

        String checkResult = usersPageValidate.ValidateUsersPage();
        if(!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        UsersPageRequest validPageRequest = usersPageValidate.getUsersPageRequest();

        Pageable pageable = PageRequest.of(validPageRequest.getRequestPage(), validPageRequest.getNumberOfElements());

        Page<User> searchResult = userService.findAllByRole(pageable, validPageRequest.getType());



        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{username}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getUser(@PathVariable(name = "username") String username, @RequestHeader("Authorization") String token)
    {
        String checkResult = UserValidate.validateUsername(username);
        if(!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        User user = userDetailsService.loadUserByToken(token);
        if(user.getRole().equals("firm") && !username.equals(user.getUsername()))
        {
            return new ResponseEntity<>(new ApiResponse(false, "Вы можете смотреть информацию только по своему аккаунту"), HttpStatus.FORBIDDEN);
        }

        Optional<User> searchResult = userService.findByUsername(username);

        if (searchResult.isPresent())
        {
            User foundedUser = searchResult.get();
            foundedUser.setPassword("[PROTECTED]");
            return new ResponseEntity<>(foundedUser, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Пользователь не найден"), HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/user")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> editUser(@RequestBody User ObjUser)
    {
        String checkResult = UserValidate.validateUsername(ObjUser.getUsername());
        if(!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<User> searchResult = userService.findByUsername(ObjUser.getUsername());

        if (!searchResult.isPresent())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Пользователь не найден"), HttpStatus.NOT_FOUND);
        }

        User oldUser = searchResult.get();
        if(ObjUser.getPassword() != null) ObjUser.setPassword(encoder.encode(ObjUser.getPassword()));
        UserValidate userValidate = new UserValidate(ObjUser);

        if(oldUser.getRole().equals("firm")) checkResult = userValidate.validateEditFirmUser(oldUser);
        else checkResult = userValidate.validateEditEmployeeUser(oldUser);

        if(!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        User editedUser = userValidate.getUser();
        userService.save(editedUser);

        editedUser.setPassword("[PROTECTED]");
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }
}

