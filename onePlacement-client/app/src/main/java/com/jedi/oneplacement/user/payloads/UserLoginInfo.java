package com.jedi.oneplacement.user.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginInfo {
    private String username;
    private String password;
}
