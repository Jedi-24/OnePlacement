package com.Jedi.OnePlacementServer.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Role {

    @Id
    private int id;
    private String name;
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
//    private Set<Company> companies = new HashSet<>();
}