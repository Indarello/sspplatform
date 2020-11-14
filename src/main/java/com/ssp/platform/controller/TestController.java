package com.ssp.platform.controller;

import com.ssp.platform.entity.User;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController
{
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public TestController(UserDetailsServiceImpl userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/testall")
    public String allAccess()
    {
        return "доступна всем.";
    }

    @GetMapping("/firm")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public Object moderatorAccess(@RequestHeader("Authorization") String token)
    {
        User user = userDetailsService.loadUserByToken(token);
        if(user.getRole().equals("firm") && !user.getStatus().equals("Approved"))
        {
            return new ResponseEntity<>(new ApiResponse(false, "Ваш аккаунт не подтвердили"), HttpStatus.FORBIDDEN);
        }
        return user;

        //тестирование авторизации
    }
}
