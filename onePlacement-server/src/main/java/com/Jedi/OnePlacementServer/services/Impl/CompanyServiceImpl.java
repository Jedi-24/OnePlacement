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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        System.out.println("hereeeeeeee JJJJ");
        Company company = modelMapper.map(companyDto, Company.class);
        // set Roles for the company:
        Role iRole = this.roleRepo.findById(AppConstants.Intern_Role_ID).get();
        Role pRole = this.roleRepo.findById(AppConstants.Placement_Role_ID).get();
        role = "ROLE_".concat(role);

        if(role.matches(iRole.getRole_name())){
            company.getRoles().add(iRole);
        }
        else if(role.matches(pRole.getRole_name())){
            company.getRoles().add(pRole);
        }
        else{
            company.getRoles().add(iRole);
            company.getRoles().add(pRole);
        }

        Company savedCompany = this.companyRepo.save(company);

        return this.modelMapper.map(savedCompany, CompanyDto.class);
    }

    @Override
    public List<CompanyDto> fetchAllCompanies(String role) {
        List<Company> companyList = new ArrayList<>();
        companyList = this.companyRepo.findAll();
        role = "ROLE_" + role;
        List<CompanyDto> retC = new ArrayList<>();
        for(Company company: companyList){
            Set<Role> companyRoles = company.getRoles();
            for(Role c : companyRoles){
                if(c.getRole_name().matches(role)) {
                    System.out.println(company.getCid() + " debug mode");
                    retC.add(this.modelMapper.map(company, CompanyDto.class));
                }
            }
        }
        System.out.println(retC.get(0).getCid());
        return retC;
    }
}