package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.util.RoleType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority, Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public Role() {
    }

    public Role(int id, RoleType role) {
        this.id = id;
        this.role = role;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    @Override
    public String toString() {
        return this.role.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role1 = (Role) o;
        return getRole() == role1.getRole();
    }

    @Override
    public int hashCode() {
        return getRole().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return this.role.ordinal() - ((Role) o).getRole().ordinal();
    }
}
