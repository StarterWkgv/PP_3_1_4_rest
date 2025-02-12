package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.*;
import java.util.Set;

public class UserDto {

    private long id;

    @Size(min = 1, max = 50, message = "Name should be between 1 and 50 characters")
    private String firstName;

    @Size(min = 1, max = 50, message = "Last name should be between 1 and 50 characters")
    private String lastName;

    @Min(value = 0, message = "Age should be greater then 0")
    @Max(value = 127, message = "Age should be less then 128")
    private int age;

    @NotEmpty(message = "The field should not be empty")
    @Email(message = "Wrong format")
    private String email;

    @NotEmpty(message = "The field should not be empty")
    private Set<String> roles;

    //    @NotEmpty(message = "The field should not be empty")
    private String password;

    public UserDto() {
    }

    public UserDto(long id, String firstName, String lastName, byte age, String email, Set<String> roles, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.roles = roles;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                '}';
    }

}
