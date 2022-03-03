package com.registration.register.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends AbstractEntity {

    @Column(name = "firstname", nullable = false)
    public String firstName;

    @Column(name = "lastname", nullable = false)
    public String lastName;

    public int age = 0;

    @Column(name = "course_count")
    public int count = 0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    public Set<Course> course = new HashSet<>();

    // other attributes...etc

}
