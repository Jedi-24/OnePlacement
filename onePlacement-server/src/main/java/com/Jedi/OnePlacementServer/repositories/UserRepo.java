package com.Jedi.OnePlacementServer.repositories;

import com.Jedi.OnePlacementServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
