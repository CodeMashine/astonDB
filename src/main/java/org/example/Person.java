package org.example;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long person_id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "age")
    private String age;


    public Person(String fullName, String age) {
        this.fullName = fullName;
        this.age = age;
    }

    @ManyToMany
    @JoinTable(
            name = "Persons_Roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
        return person_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role... roles) {
        for (Role role : roles) {
            this.roles.add(role);
        }

    }
}