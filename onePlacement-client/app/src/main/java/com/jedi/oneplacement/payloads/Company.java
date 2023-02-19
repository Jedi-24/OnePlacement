package com.jedi.oneplacement.payloads;

import java.util.HashSet;
import java.util.Set;

public class Company {

    public Company() {}

    private int cid;
    private String cname;
    private String profile;
    private String stipend;
    private String ctc;
    private String ppo;

    private Set<RoleDto> roles = new HashSet<>();
    private Set<User> users = new HashSet<>();

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStipend() {
        return stipend;
    }

    public void setStipend(String stipend) {
        this.stipend = stipend;
    }

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public String getPpo() {
        return ppo;
    }

    public void setPpo(String ppo) {
        this.ppo = ppo;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Company{" +
                "cId=" + cid +
                ", cname='" + cname + '\'' +
                ", profile='" + profile + '\'' +
                ", stipend='" + stipend + '\'' +
                ", ctc='" + ctc + '\'' +
                ", ppo='" + ppo + '\'' +
                ", roles=" + roles +
                ", users=" + users +
                '}';
    }
}