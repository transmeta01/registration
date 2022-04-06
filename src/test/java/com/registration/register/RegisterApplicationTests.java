package com.registration.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.registration.register.controller.RegistrationController;
import com.registration.register.entity.Student;
import com.registration.register.messages.ResponseMessage;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
//@TestPropertySource(locations = "classpath:application-test.properties")
//@Sql(scripts = "classpath:./schema.sql")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterApplicationTests {

	@LocalServerPort
	int port;

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

	@Autowired
	private ObjectMapper objectMapper;

	//@TODO instantiate a json schema and validator
//	new ObjectMapper().readValue(jsonString, Map.class);

//	private final String rootURI = "http://localhost:" + port + "/v1/registration";
//
//	@Test
//	void contextLoads() {
//		assertThat(registrationController).isNotNull();
//	}
//
//	@Test
//	void injectedComponentsNotNull() {
//		assertThat(dataSource).isNotNull();
//		assertThat(jdbcTemplate).isNotNull();
//		assertThat(entityManager).isNotNull();
//		assertThat(studentRepository).isNotNull();
//	}
//
//	@Test
//	void testStudent() {
//		RestTemplateBuilder builder = new RestTemplateBuilder();
//		RestTemplate restTemplate = builder.rootUri(rootURI).build();
//		final HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
//		final ResponseEntity<Student> response = restTemplate.exchange(
//				"/student/1", HttpMethod.GET, request, new ParameterizedTypeReference<>() {});
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//	}
//
//	@Test
//	void getAllStudents() {
//		RestTemplateBuilder builder = new RestTemplateBuilder();
//		RestTemplate restTemplate = builder.rootUri(rootURI).build();
//		final HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
//		final ResponseEntity<List<Student>> response = restTemplate.exchange(
//				"/student/", HttpMethod.GET, request, new ParameterizedTypeReference<List<Student>>() {});
//
//		final List<Student> students = response.getBody();
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(2, students.size());
//	}

}
