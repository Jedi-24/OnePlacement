package com.jedi.oneplacement.payloads;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Company {
    private Integer cId;
    private String cname;
    private String profile;
    private String stipend;
    private String ctc;
    private String ppo;

    private Set<RoleDto> roles = new HashSet<>();
    private Set<User> users = new HashSet<>();
}