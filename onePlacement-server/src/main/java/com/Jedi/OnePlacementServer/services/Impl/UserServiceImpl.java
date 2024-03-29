package com.Jedi.OnePlacementServer.services.Impl;

import com.Jedi.OnePlacementServer.entities.Company;
import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.exceptions.ResourceNotFoundException;
import com.Jedi.OnePlacementServer.payloads.CompanyDto;
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
    @Autowired // the instance or let's say bean for this is made via a method, present in config file: security config.
    private PasswordEncoder passwordEncoder; // name of the object and name of the method declaring that bean needs to be same, highly important.
    // as bean is made as same name as the method is:
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setEmail(userDto.getEmail());
        user.setBranch(userDto.getBranch());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRoles(userDto.getRoles());

        return this.userToDto(this.userRepo.save(user));
    }

    @Override
    public String verifyProfile(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setProfileStatus(AppConstants.VERIFIED);

        this.userRepo.save(user);

        return AppConstants.VERIFIED;
    }

    @Override
    public String setCredits(Integer userId, int credits) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setTpoCredits(Integer.toString(credits));

        this.userRepo.save(user);

        return AppConstants.CREDITS;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

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
        this.userRepo.deleteById(userId);
    }

    @Override
    public void regInCompany(Integer userId, CompanyDto companyDto) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.getCompanies().add(this.modelMapper.map(companyDto, Company.class));
        this.userRepo.save(user);
    }

    @Override
    public void setupFcmToken(Integer userId, String devToken) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setFcmToken(devToken);
        this.userRepo.save(user);
    }


    @Override
    public UserDto registerUser(UserDto userDto, String role) {
        User user = modelMapper.map(userDto, User.class);
        // encode the password:
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // set Roles:
        Role iRole = this.roleRepo.findById(AppConstants.Intern_Role_ID).get();
        Role pRole = this.roleRepo.findById(AppConstants.Placement_Role_ID).get();
        role = "ROLE_".concat(role);

        if (role.matches(iRole.getName())) user.getRoles().add(iRole);
        else user.getRoles().add(pRole);

        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    // model mapper to convert userDto -> user and vice versa, but abhi ->
    private User dtoToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    private UserDto userToDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }
}