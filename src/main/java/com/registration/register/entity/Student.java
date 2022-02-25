package com.registration.register.entity;

import javax.persistence.*;

@Entity
@Table(name = "STUDENT")
public class Student extends BaseEntity {

    @Column(name = "firstname", nullable = false)
    public String firstName;

    @Column(name = "lastname", nullable = false)
    public String lastName;

    public int age = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    public Course course;

    // other attributes...etc

}
