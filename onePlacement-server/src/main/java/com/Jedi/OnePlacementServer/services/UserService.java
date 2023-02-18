package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.payloads.CompanyDto;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface UserService {
    UserDto registerUser(UserDto userDto, String role);
    UserDto updateUser(UserDto user, Integer userId);
    String verifyProfile(Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void dltUser(Integer userId);
    void regInCompany(Integer userId, CompanyDto companyDto);
}
