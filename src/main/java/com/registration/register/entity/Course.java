package com.registration.register.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "COURSE")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course extends AbstractEntity {

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public String name;

    @Column(name = "student_count")
    public int count = 0;

}
