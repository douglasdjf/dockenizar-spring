package br.com.douglasdjf21.integrationtests.resource;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.douglasdjf21.confis.TestConfigs;
import br.com.douglasdjf21.integrationtests.dto.AccountCredentialsDTO;
import br.com.douglasdjf21.integrationtests.dto.TokenDTO;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthResourceXMLTest extends AbstractIntegretationTest {

	private static TokenDTO tokenVO;
	
	
	@Test
	@Order(1)
	public void testLogin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
			
		tokenVO =
				given()
					 .basePath("/auth/login")
					 .port(TestConfigs.SERVER_PORT)
					 .contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
				 .when()
				 	.post()
				 .then()
				   .statusCode(200)
				 .extract()
				    .body().as(TokenDTO.class);
		
	
		Assertions.assertNotNull(tokenVO.getAccessToken());
		Assertions.assertNotNull(tokenVO.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newtokenVO =
				given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				    .pathParam("username", tokenVO.getUsername())
				    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION,"Bearer "+ tokenVO.getRefreshToken())
				.when()
				   .put("{username}")
				.then()
				   .statusCode(200)
				.extract()
				   .body().as(TokenDTO.class);
		
		
		Assertions.assertNotNull(newtokenVO.getAccessToken());
		Assertions.assertNotNull(newtokenVO.getRefreshToken());
	}
	
}
