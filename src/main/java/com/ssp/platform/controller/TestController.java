package com.ssp.platform.controller;

import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Firm')")
    public String moderatorAccess(@RequestHeader("Authorization") String token)
    {
        UserDetails userDetails = userDetailsService.loadUserByToken(token);

        return "FIRM Board.";

        //тестирование авторизации
    }
}
