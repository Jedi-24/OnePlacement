package com.jedi.oneplacement.user.payloads;

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

    private RoleDto role;
}
