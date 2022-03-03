package com.registration.register;

import com.registration.register.entity.Course;
import com.registration.register.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @BeforeAll
    void populateData() {
        log.info("Saving a few courses ");
        Course course1 = Course
                .builder()
                .name("CS 1-0-1")
                .description("First CS course")
                .build();
        Course course2 = Course
                .builder()
                .name("CS 1-0-2")
                .description("Second CS course")
                .build();
        Course course3 = Course
                .builder()
                .name("CS 1-0-3")
                .description("Third CS course")
                .build();

        courseRepository.saveAllAndFlush(
                Arrays.asList(course1, course2, course3)
        );
    }
}
