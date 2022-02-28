package com.registration.register;

import com.registration.register.entity.Course;
import com.registration.register.entity.Student;
import com.registration.register.service.CourseService;
import com.registration.register.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(StudentService studentService, CourseService courseService) {
		return (args -> {

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

			studentService.addStudent(sam);
			studentService.addStudent(smith);

			// save a few courses
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

			courseService.addCourse(course1);
			courseService.addCourse(course2);
			courseService.addCourse(course3);

		});
	}

}
