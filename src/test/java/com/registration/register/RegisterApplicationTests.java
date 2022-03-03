package com.registration.register;

import com.registration.register.controller.RegistrationController;
import com.registration.register.entity.Student;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(locations = "classpath:./application-test.properties")
@Sql(scripts = "classpath:./schema.sql")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	private RestTemplateBuilder builder;

	@Autowired
	RegistrationController registrationController;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void contextLoads() {
		assertThat(registrationController).isNotNull();
	}

	@Test
	void injectedComponentsNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(studentRepository).isNotNull();
	}

	@Test
	void testStudent() {
		RestTemplate template = builder.rootUri("http://localhost:" + port).build();
		ResponseEntity<Student> response = template.exchange(RequestEntity.get("/v1/registration/student/1").build(), Student.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
