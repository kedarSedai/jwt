package com.example.springauthjwt.controller;

import com.example.springauthjwt.Model.JwtRequest;
import com.example.springauthjwt.Model.JwtResponse;
import com.example.springauthjwt.Service.UserService;
import com.example.springauthjwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String home() {
        return "Hello from home";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new Exception("Invalid Credentials: ");
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new JwtResponse(token);

    }
}
