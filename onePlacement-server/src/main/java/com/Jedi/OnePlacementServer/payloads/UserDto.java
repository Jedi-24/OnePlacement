package com.Jedi.OnePlacementServer.payloads;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter @Setter
public class UserDto {

    private int id;
    private String name;
    private String reg_no;
    private String email;
    private String password;
}
