package com.Jedi.OnePlacementServer.repositories;

import com.Jedi.OnePlacementServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByRegNo(String regNo);

//     search users: todo: LEARN SPRING DATA JPA FURTHER FOR BETTER UNDERSTANDING.
//    public List<User> findByNameIsContaining(String name);
}