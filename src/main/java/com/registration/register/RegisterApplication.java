package com.registration.register;

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

			// save a few courses
			log.info("Saving a few courses ");
		});
	}

}
