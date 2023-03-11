package com.Jedi.OnePlacementServer.repositories;

import com.Jedi.OnePlacementServer.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company, Integer> {
//    @Query("select c from Company c where c.roles in: roles") : todo: study USE-CASE: JpaRepository find User with Role in list of roles [using query];
//    List<Company> findBySpecificRoles(@Param("roles") ArrayList<Role> rolesList);
    Page<Company> findAllByRoles_name(String role, Pageable pageable);
}