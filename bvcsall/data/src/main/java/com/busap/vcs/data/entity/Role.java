package com.busap.vcs.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * User: djyin
 * Date: 12/3/13
 * Time: 9:34 AM
 */
@Entity
@Table(name = "role")
@JsonIgnoreProperties(value = {"permissions"})
public class Role extends BaseEntity {

    private static final long serialVersionUID = -6614052029623997372L;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, updatable = false)
    private Boolean isSystem;

    @Column(length = 1024, nullable = true)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Permission.class)
    @JoinTable(name = "role_permission")
    private List<Permission> permissions = new ArrayList<Permission>();

    private Long status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> authorities) {
        this.permissions = authorities;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        if (isSystem != null ? !isSystem.equals(role.isSystem) : role.isSystem != null) return false;
        if (name != null ? !name.equals(role.name) : role.name != null) return false;
        if (permissions != null ? !permissions.equals(role.permissions) : role.permissions != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isSystem != null ? isSystem.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }
}