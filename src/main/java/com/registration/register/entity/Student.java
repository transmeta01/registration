package com.registration.register.entity;

import lombok.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
public class Student extends BaseEntity {

    @Column(name = "firstname", nullable = false)
    public String firstName;

    @Column(name = "lastname", nullable = false)
    public String lastName;

    public int age = 0;

    @ElementCollection
    @CollectionTable(
            name = "STUDENT_COURSES",
            joinColumns = @JoinColumn(name = "student_id")
    )
    public Set<Course> course = new HashSet<>();

    // other attributes...etc

}
