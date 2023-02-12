package com.Jedi.OnePlacementServer.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "Companies") @Getter @Setter
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cId;

    private String cname;
    private String profile;
    private String stipend;
    private String ctc;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "companies")
    private Set<User> users = new HashSet<>();
}