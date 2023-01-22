package com.Jedi.OnePlacementServer.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Data
@NoArgsConstructor
@Getter @Setter
public class UserDto {

    private int id;

    @NotEmpty @Size(min = 4, message = "Username must be of length >= 4")
    private String name;

    @NotEmpty
    private String reg_no;

    @Email(message = "Invalid Email Address")
    private String email;

    // pattern annotation;
    @NotEmpty(message = "Invalid Password") @Size(min = 4, max = 12)
    private String password;
}
