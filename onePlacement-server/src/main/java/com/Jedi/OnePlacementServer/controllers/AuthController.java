package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.exceptions.LoginException;
import com.Jedi.OnePlacementServer.payloads.JwtAuthRequest;
import com.Jedi.OnePlacementServer.payloads.JwtAuthResponse;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.security.JwtTokenHelper;
import com.Jedi.OnePlacementServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // LOGIN API POINT: and to create Token.
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest authRequest) throws Exception{
        this.authenticate(authRequest.getUsername(), authRequest.getPassword());
        String jwtToken = this.jwtTokenHelper.generateToken(userDetailsService.loadUserByUsername(authRequest.getUsername()));
        JwtAuthResponse authResponse = new JwtAuthResponse();
        authResponse.setToken(jwtToken);

        return new ResponseEntity<JwtAuthResponse>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/register/user/{role}")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto , @PathVariable("role") String role) throws Exception{
        UserDto user = this.userService.registerUser(userDto,role);
        JwtAuthRequest authRequest = new JwtAuthRequest();
        authRequest.setUsername(user.getRegNo());
        authRequest.setPassword(userDto.getPassword());
        ResponseEntity<JwtAuthResponse> response = createToken(authRequest);
        user.setJwtToken(response.getBody().getToken());
        user.setPassword("$$$$");

        return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception{
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e){
            System.out.println("Invalid Password !");
            // Necessary to throw an Exception, vrna Token generate ho jayega be faltu ka:
            throw new LoginException("Invalid Password !");
        }
    }
}