package com.jedi.oneplacement.payloads;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String regNo;
    private String email;
    private String password;
    private String jwtToken;
    private String branch;
    private String phoneNumber;
    private String tpoCredits;
    private String roleStatus;
    private String profileStatus;
    private String fcmToken;

    private Set<RoleDto> roles;
    private Set<Company> companies = new HashSet<>();
}
