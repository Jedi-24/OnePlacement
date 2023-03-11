package com.jedi.oneplacement.payloads;


public class RoleDto {
    private int id;
    private String name;

    public RoleDto() {
    }

    public RoleDto(int id, String role_name) {
        this.id = id;
        this.name = role_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return name;
    }

    public void setRoleName(String role_name) {
        this.name = role_name;
    }
}
