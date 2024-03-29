package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.ApiResponse;
import com.Jedi.OnePlacementServer.payloads.CompanyDto;
import com.Jedi.OnePlacementServer.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/company")
public class CompanyController {
    final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/save/{role}")
    public ResponseEntity<CompanyDto> addCompany(@PathVariable("role") String role, @RequestBody CompanyDto companyDto) {
        return new ResponseEntity<CompanyDto>(this.companyService.addCompany(companyDto, role), HttpStatus.OK);
    }

    @GetMapping("/{role}")
    public List<CompanyDto> fetchAllCompanies(@PathVariable("role") String role, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageN,
                                              @RequestParam(value = "pageSize", defaultValue = "8", required = false) Integer pageS,
                                              @RequestParam(value = "sortBy", defaultValue = "cname", required = false) String sortBy){
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        List<CompanyDto> companyDtoList = this.companyService.fetchAllCompanies(role, pageN, pageS, sortBy);
        return companyDtoList;
    }
}