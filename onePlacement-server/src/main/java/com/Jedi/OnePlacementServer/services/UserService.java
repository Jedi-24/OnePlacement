package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void dltUser(Integer userId);
}