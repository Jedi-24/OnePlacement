package com.jedi.oneplacement.payloads;

import java.util.HashSet;
import java.util.Set;
public class User {
    private int id;
    private String name;
    private String regNo;
    private String email;
    private String password;
    private String jwtToken;
    private String branch;
    private String phoneNumber;
    private String tpoCredits;
    private String roleStatus;
    private String profileStatus;
    private String fcmToken;

    private Set<RoleDto> roles;
    private Set<Company> companies = new HashSet<>();

    public User() {}

    public User(int id, String name, String regNo, String email, String password, String jwtToken, String branch, String phoneNumber, String tpoCredits, String roleStatus, String profileStatus, String fcmToken, Set<RoleDto> roles, Set<Company> companies) {
        this.id = id;
        this.name = name;
        this.regNo = regNo;
        this.email = email;
        this.password = password;
        this.jwtToken = jwtToken;
        this.branch = branch;
        this.phoneNumber = phoneNumber;
        this.tpoCredits = tpoCredits;
        this.roleStatus = roleStatus;
        this.profileStatus = profileStatus;
        this.fcmToken = fcmToken;
        this.roles = roles;
        this.companies = companies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTpoCredits() {
        return tpoCredits;
    }

    public void setTpoCredits(String tpoCredits) {
        this.tpoCredits = tpoCredits;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(String roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }
}
