package com.example.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.example.test.entity.User;
import com.example.test.service.ValidationService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	ValidationService service = new ValidationService();

	@Test
	public void whenIsNullOrEmpty_isNull_thenReturnTrue() {
		Assertions.assertEquals(service.isNullOrEmpty(null), true);
	}

	@Test
	public void whenIsNullOrEmpty_isNotNullAndNotEmpty_thenReturnTrue() {
		Assertions.assertEquals(service.isNullOrEmpty("Testing"), false);
	}

	@Test
	public void whenAddUser_thenReturnSuccess() throws Exception {

		User user = new User();
		user.setName("Testing 10");
		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/users",
				user, String.class);
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
	}
}
