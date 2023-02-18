package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.payloads.CompanyDto;

import java.util.List;

public interface CompanyService {
    CompanyDto addCompany(CompanyDto company, String role);
    List<CompanyDto> fetchAllCompanies(String role);

}
