package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.ApiResponse;
import com.Jedi.OnePlacementServer.payloads.CompanyDto;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    // role based access using preAuthorized: as method label;

    @Autowired // implementation class's object Injection into the userService.
    private UserService userService;

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/")
    public List<UserDto> getAllUsers(){ return userService.getAllUsers(); }

    // get A single User:
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Integer uid){
        return ResponseEntity.ok(this.userService.getUserById(uid));
    }

    // update a user;
    @PutMapping("/{userId}") // path uri variable;
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid){
        return ResponseEntity.ok(this.userService.updateUser(userDto, uid));
    }
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/verify/{userId}")
    public ResponseEntity<ApiResponse> verifyProfile(@PathVariable("userId") Integer uId){
        String s = this.userService.verifyProfile(uId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(s, true), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        this.userService.dltUser(uid);
        return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @PostMapping("/company/register/{userId}")
    public ResponseEntity<ApiResponse> registerIntoCompany(@PathVariable("userId") Integer uid, @Valid @RequestBody CompanyDto companyDto){
        this.userService.regInCompany(uid, companyDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Registered Successfully", true), HttpStatus.OK);
    }
}