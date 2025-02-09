package org.example;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(name = "role", unique = true)
    private String role;


    public Role(String role) {
        this.role = role;
    }

    ;

    @ManyToMany(mappedBy = "roles")
    private Set<Person> persons;

    public Long getId() {
        return role_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
