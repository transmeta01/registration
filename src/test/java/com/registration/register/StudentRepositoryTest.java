package com.registration.register;

import com.registration.register.entity.Student;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.jdbc.Sql;

import org.springframework.web.client.RestTemplate;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
//@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    RestTemplate restTemplate;

//    @Container
//    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
//                                                    .withDatabaseName("test")
//                                                    .withUsername("usr")
//                                                    .withPassword("pass")
//                                                    .withInitScript("create-database.sql")
//                                                    .start();
//
//    @BeforeAll
//    void init() {
//        // save a few students
//        log.info("Saving a few users ");
//        Student sam = Student
//                .builder()
//                .age(37)
//                .firstName("A User")
//                .lastName("A Sam")
//                .build();
//
//        Student smith = Student
//                .builder()
//                .age(40)
//                .firstName("Another Student")
//                .lastName("Another Smith")
//                .build();
//
//        studentRepository.saveAllAndFlush(
//                Arrays.asList(sam, smith)
//        );
//    }
//
//    @Test
//    void test() {
//        assertTrue(mySQLContainer.isRunning());
//    }
//
//    @Test
//    void getAllStudents() {
//        List<Student> students = studentRepository.findAll();
//        assertEquals(2, students.size());
//    }
//
//    @Test
//    void testStudentById() {
//        Student student = studentRepository.getById(1L);
//        assertNotNull(student);
//    }
}
