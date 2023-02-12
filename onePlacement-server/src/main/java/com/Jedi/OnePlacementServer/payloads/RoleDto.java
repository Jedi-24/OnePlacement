package com.Jedi.OnePlacementServer.payloads;

import com.Jedi.OnePlacementServer.entities.Company;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDto {
    private int id;
    private String role_name;
    private Set<Company> companies = new HashSet<>();
}
