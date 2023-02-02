package com.jedi.oneplacement.payloads;

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

    private Set<RoleDto> roles;
}
