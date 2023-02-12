package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.payloads.CompanyDto;

public interface CompanyService {
    CompanyDto addCompany(CompanyDto company, String role);
}
