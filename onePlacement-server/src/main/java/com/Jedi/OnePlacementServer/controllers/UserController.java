package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.ApiResponse;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired // implementation class's object Injection into the userService.
    private UserService userService;

    // create a new User;
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    // get All users;
    @GetMapping("/")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

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

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){
        this.userService.dltUser(uid);
        return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }
}
