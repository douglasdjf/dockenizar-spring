package br.com.douglasdjf21.integrationtests.resource;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import br.com.douglasdjf21.integrationtests.mapper.YMLMapper;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthResourceYAMLTest extends AbstractIntegretationTest {

	private static TokenDTO tokenVO;
	private static YMLMapper ymlMapper;
	
	@BeforeAll
	public static void  setupe() {
		ymlMapper = new YMLMapper();
	}
	
	
	@Test
	@Order(1)
	public void testLogin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
			
		tokenVO =
				given()
				     .config(RestAssuredConfig
				    		 .config()
				    		 .encoderConfig(EncoderConfig.encoderConfig()
				    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				     .accept(TestConfigs.CONTENT_TYPE_YML)
					 .basePath("/auth/login")
					 .port(TestConfigs.SERVER_PORT)
					 .contentType(TestConfigs.CONTENT_TYPE_YML)
				.body(user,ymlMapper)
				 .when()
				 	.post()
				 .then()
				   .statusCode(200)
				 .extract()
				    .body().as(TokenDTO.class,ymlMapper);
		
	
		Assertions.assertNotNull(tokenVO.getAccessToken());
		Assertions.assertNotNull(tokenVO.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newtokenVO =
				given()
			     .config(RestAssuredConfig
			    		 .config()
			    		 .encoderConfig(EncoderConfig.encoderConfig()
			    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			     .accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				    .pathParam("username", tokenVO.getUsername())
				    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION,"Bearer "+ tokenVO.getRefreshToken())
				.when()
				   .put("{username}")
				.then()
				   .statusCode(200)
				.extract()
				   .body().as(TokenDTO.class,ymlMapper);
		
		
		Assertions.assertNotNull(newtokenVO.getAccessToken());
		Assertions.assertNotNull(newtokenVO.getRefreshToken());
	}
	
}
