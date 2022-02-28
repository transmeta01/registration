package com.registration.register.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Table(name = "REGISTRATION")
@NoArgsConstructor
@AllArgsConstructor
public class Registration extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "lastModified", nullable = false)
    public Date lastModified = new Date();

    @ManyToOne
    @JoinColumn(name = "student_id")
    public Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    public Course course;
}
