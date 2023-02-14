package com.Jedi.OnePlacementServer.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Role {

    @Id
    private int id;
    private String role_name;
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
//    private Set<Company> companies = new HashSet<>();
}