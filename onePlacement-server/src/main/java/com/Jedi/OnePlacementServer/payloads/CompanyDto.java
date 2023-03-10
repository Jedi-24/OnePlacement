package com.Jedi.OnePlacementServer.payloads;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CompanyDto {
    private int cid;

    private String cname;
    private String profile;
    private String stipend;
    private String ctc;
    private String ppo;

    private Set<Role> roles = new HashSet<>();
//    private Set<User> users = new HashSet<>();
}
