package com.Jedi.OnePlacementServer.payloads;


import com.Jedi.OnePlacementServer.entities.Company;
import com.Jedi.OnePlacementServer.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Getter @Setter
public class UserDto {

    private int id;

//    @NotEmpty @Size(min = 4, message = "Username must be of length >= 4")
    private String name;

//    @NotEmpty
    private String regNo;

//    @Email(message = "Invalid Email Address")
    private String email;

    // pattern annotation;
//    @NotEmpty(message = "Invalid Password") @Size(min = 4)
    private String password;

    private String profilePath;
    private String resumePath;

    private String jwtToken;
    private String branch;
    private String phoneNumber;
    private String tpoCredits;
    private String roleStatus;
    private String profileStatus;

    private String fcmToken;
    private Set<Role> roles = new HashSet<>();
    private Set<Company> companies = new HashSet<>();
}