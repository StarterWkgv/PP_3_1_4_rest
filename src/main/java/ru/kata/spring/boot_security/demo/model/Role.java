package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.util.RoleType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
    }

    public Role(Long id, RoleType role, List<User> users) {
        this.id = id;
        this.role = role;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}
