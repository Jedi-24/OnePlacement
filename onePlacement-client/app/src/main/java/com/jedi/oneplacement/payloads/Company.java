package com.jedi.oneplacement.payloads;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Company {
    private int cid;
    private String cname;
    private String profile;
    private String stipend;
    private String ctc;
    private String ppo;

    private Set<RoleDto> roles = new HashSet<>();
    private Set<User> users = new HashSet<>();

    @Override
    public String toString() {
        return "Company{" +
                "cId=" + cid +
                ", cname='" + cname + '\'' +
                ", profile='" + profile + '\'' +
                ", stipend='" + stipend + '\'' +
                ", ctc='" + ctc + '\'' +
                ", ppo='" + ppo + '\'' +
                ", roles=" + roles +
                ", users=" + users +
                '}';
    }
}