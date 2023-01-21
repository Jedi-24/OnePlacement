package com.Jedi.OnePlacementServer.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@Getter
@Setter
public class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String reg_no;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // profile url ?
}
