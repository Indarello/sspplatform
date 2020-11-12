package com.ssp.platform.controller;

import com.ssp.platform.entity.User;
import com.ssp.platform.repository.UserRepository;
import com.ssp.platform.request.LoginRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.JwtResponse;
import com.ssp.platform.security.jwt.JwtUtils;
import com.ssp.platform.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController
{
    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final PasswordEncoder encoder;
    final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    // @PostMapping(value = "/task", produces = "application/json", consumes = "application/json")
    // public ResponseEntity<Object> taskAdd(@RequestBody Task task)

    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User ObjUser)
    {
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

        // Create new user's account
        //User user = new User(ObjUser.getUsername(),
        //        ObjUser.getEmail(),
        //        encoder.encode(ObjUser.getPassword()));

        User user = ObjUser;
        user.setPassword(encoder.encode(ObjUser.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully!"));
    }
}
