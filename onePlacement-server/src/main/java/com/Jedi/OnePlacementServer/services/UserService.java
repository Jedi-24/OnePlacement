package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto userDto, String role);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void dltUser(Integer userId);
}
