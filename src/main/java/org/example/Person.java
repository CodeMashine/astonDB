package org.example;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "age")
    private String age;

    public Person() {
    }

    public Person(String fullName, String age) {
        this.fullName = fullName;
        this.age = age;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "persons_roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    public int getId() {
        return id;
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

    public void resetRoles() {
        for (Role role : roles) {
            role.getPersons().remove(this); // Удаляем ссылку на Person из каждой Role
        }
        roles.clear();
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role... roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    @Override
    public String toString() {
        return "id : " + id + ", fullName : " + fullName + ", age : " + age;
    }
}