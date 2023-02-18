package com.Jedi.OnePlacementServer.repositories;

import com.Jedi.OnePlacementServer.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Integer> {
}
