package com.Jedi.OnePlacementServer.services.Impl;

import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.exceptions.ResourceNotFoundException;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
import com.Jedi.OnePlacementServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("NAHI MILA","User","Id",userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setReg_no(userDto.getReg_no());

        return this.userToDto(this.userRepo.save(user));
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("NAHI MILA","User","Id",userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        Streamable.of(userRepo.findAll()).forEach(user -> users.add(this.userToDto(user)));

        return users;
    }

    @Override
    public void dltUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("NAHI MILA","User","Id",userId));
        this.userRepo.delete(user);
    }

    // model mapper to convert userDto -> user and vice versa, but abhi ->
    private User dtoToUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setReg_no(userDto.getReg_no());
        user.setPassword(userDto.getPassword());

        return user;
    }

    private UserDto userToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setReg_no(user.getReg_no());
        userDto.setPassword(user.getPassword());

        return userDto;
    }
}