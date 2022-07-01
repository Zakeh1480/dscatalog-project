package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entity.Role;

import javax.validation.constraints.NotBlank;

public class RoleDTO {

    private Long id;

    @NotBlank
    private String authority;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role role) {
        id = role.getId();
        authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}