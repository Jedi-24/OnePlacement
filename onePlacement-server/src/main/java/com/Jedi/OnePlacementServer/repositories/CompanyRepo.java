package com.Jedi.OnePlacementServer.repositories;

import com.Jedi.OnePlacementServer.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company, Integer> {
}
