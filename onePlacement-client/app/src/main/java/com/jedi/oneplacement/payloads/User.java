package com.jedi.oneplacement.payloads;

import lombok.Data;

@Data
public class User extends Object{
    private String name;
    private String regNo;
    private String email;
    private String password;
    private String jwtToken;
}
