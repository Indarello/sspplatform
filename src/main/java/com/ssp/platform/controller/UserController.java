package com.ssp.platform.controller;

import com.ssp.platform.entity.User;
import com.ssp.platform.request.UsersPageRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.JwtResponse;
import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.UserService;
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
    public UserController(
            AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService, PasswordEncoder encoder,
            JwtUtils jwtUtils
    )
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader("username") String username, @RequestHeader("password") String password)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
    }

    /**
     * Метод регистрации первого админа
     */
    @PostMapping("/regfirstadmin")
    public ResponseEntity<?> registerFirstAdmin(@RequestBody User ObjUser)
    {

        User user = new User(ObjUser.getUsername(), encoder.encode(ObjUser.getPassword()), ObjUser.getRole());

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
        if (!checkResult.equals("ok"))
        {
            return new ResponseEntity<>(new ApiResponse(false, checkResult), HttpStatus.NOT_ACCEPTABLE);
        }

        UsersPageRequest validPageRequest = usersPageValidate.getUsersPageRequest();

        Pageable pageable = PageRequest.of(validPageRequest.getRequestPage(), validPageRequest.getNumberOfElements());

        Page<User> searchResult = userService.findAllByRole(pageable, validPageRequest.getType());

        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{username}", produces = "application/json")
    public ResponseEntity<Object> taskDelete(@PathVariable(name = "username") String username)
    {
        if (username == null)
            return new ResponseEntity<>(new ApiResponse(false, "Parameter username not provided"), HttpStatus.NOT_ACCEPTABLE);

        try
        {
            if (userService.deleteUser(username))
            {
                return new ResponseEntity<>(new ApiResponse(true, "user deleted"), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ApiResponse(false, "User not found"), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

