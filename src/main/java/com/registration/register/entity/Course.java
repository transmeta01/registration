package com.registration.register.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COURSE")
public class Course extends BaseEntity {

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public String name;

    // other attributes...etc

}
