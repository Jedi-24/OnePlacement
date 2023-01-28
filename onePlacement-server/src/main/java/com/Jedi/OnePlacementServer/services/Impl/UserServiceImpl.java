package com.Jedi.OnePlacementServer.services.Impl;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.exceptions.ResourceNotFoundException;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.repositories.RoleRepo;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
import com.Jedi.OnePlacementServer.services.UserService;
import com.Jedi.OnePlacementServer.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRegNo(userDto.getRegNo());

        return this.userToDto(this.userRepo.save(user));
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));

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
                .orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        this.userRepo.delete(user);
    }

    @Override
    public UserDto registerUser(UserDto userDto, String role) {
        User user = modelMapper.map(userDto, User.class);
        // encode the password:
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // set Roles:
        Role iRole = this.roleRepo.findById(AppConstants.Intern_Role_ID).get();
        Role pRole = this.roleRepo.findById(AppConstants.Placement_Role_ID).get();

        if(role.matches(iRole.getRole_name())) user.getRoles().add(iRole);
        else user.getRoles().add(pRole);

        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser,UserDto.class);
    }

    // model mapper to convert userDto -> user and vice versa, but abhi ->
    private User dtoToUser(UserDto userDto){
        return this.modelMapper.map(userDto, User.class);
    }

    private UserDto userToDto(User user){
        return this.modelMapper.map(user, UserDto.class);
    }
}