package com.Jedi.OnePlacementServer.entities;

import jakarta.persistence.*;

@Entity @Table(name = "Admin")
public class Admin {
    // todo: TBD;
    @Id
    private int Id;
    private String username;
    private String password;

    //relation b/w admin and role entities:
}