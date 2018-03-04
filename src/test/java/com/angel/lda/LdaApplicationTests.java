package com.angel.lda;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LdaApplicationTests {

	@Test
	public void contextLoads() {

		Response response = given().port(8090).auth().basic("doctor1@test.com", "qwerty").get("/api/treatment/completed");
		response.then().body("id", hasItem(5));
	}

}
