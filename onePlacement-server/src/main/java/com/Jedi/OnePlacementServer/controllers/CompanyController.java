package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.CompanyDto;
import com.Jedi.OnePlacementServer.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/save/{role}")
    ResponseEntity<CompanyDto> addCompany(@PathVariable("role") String role, @RequestBody CompanyDto companyDto){
        return new ResponseEntity<CompanyDto>(this.companyService.addCompany(companyDto, role), HttpStatus.OK);
    }
}