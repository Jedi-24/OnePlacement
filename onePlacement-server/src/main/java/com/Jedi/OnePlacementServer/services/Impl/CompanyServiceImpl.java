package com.Jedi.OnePlacementServer.services.Impl;

import com.Jedi.OnePlacementServer.entities.Company;
import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.payloads.CompanyDto;
import com.Jedi.OnePlacementServer.repositories.CompanyRepo;
import com.Jedi.OnePlacementServer.repositories.RoleRepo;
import com.Jedi.OnePlacementServer.services.CompanyService;
import com.Jedi.OnePlacementServer.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RoleRepo roleRepo;

    @Override
    public CompanyDto addCompany(CompanyDto companyDto, String role) {
        Company company = modelMapper.map(companyDto, Company.class);
        // set Roles for the company:
        Role iRole = this.roleRepo.findById(AppConstants.Intern_Role_ID).get();
        Role pRole = this.roleRepo.findById(AppConstants.Placement_Role_ID).get();
        role = "ROLE_".concat(role);

        if (role.matches(iRole.getName())) {
            company.getRoles().add(iRole);
        } else if (role.matches(pRole.getName())) {
            company.getRoles().add(pRole);
        } else {
            company.getRoles().add(iRole);
            company.getRoles().add(pRole);
        }

        Company savedCompany = this.companyRepo.save(company);
        return this.modelMapper.map(savedCompany, CompanyDto.class);
    }

    @Override
    public List<CompanyDto> fetchAllCompanies(String role, Integer pageN, Integer pageS, String sortBy) {
        role = "ROLE_" + role;
        Pageable p = PageRequest.of(pageN, pageS, Sort.by(sortBy).ascending());
        Page<Company> companyPage = this.companyRepo.findAllByRoles_name(role, p);

        List<Company> companyList = companyPage.getContent();
        List<CompanyDto> retC = new ArrayList<>();
        for(Company company: companyList){
            retC.add(this.modelMapper.map(company, CompanyDto.class));
        }

        return retC;
    }
}