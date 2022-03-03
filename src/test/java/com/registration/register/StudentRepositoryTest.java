package com.registration.register;

import com.registration.register.entity.Student;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @BeforeAll
    void populateData() {
        // save a few students
        log.info("Saving a few users ");
        Student sam = Student
                .builder()
                .age(37)
                .firstName("A User")
                .lastName("A Sam")
                .build();

        Student smith = Student
                .builder()
                .age(40)
                .firstName("Another Student")
                .lastName("Another Smith")
                .build();

        studentRepository.saveAllAndFlush(
                Arrays.asList(sam, smith)
        );
    }

}
