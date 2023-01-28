package com.jedi.oneplacement.payloads;

import lombok.Data;

@Data
public class User {
    private String name;
    private String regNo;
    private String email;
    private String password;
    private String jwtToken;
}
