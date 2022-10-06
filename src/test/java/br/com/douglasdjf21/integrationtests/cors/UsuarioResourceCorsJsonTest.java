package br.com.douglasdjf21.integrationtests.cors;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.douglasdjf21.confis.TestConfigs;
import br.com.douglasdjf21.dto.security.TokenDTO;
import br.com.douglasdjf21.integrationtests.dto.AccountCredentialsDTO;
import br.com.douglasdjf21.integrationtests.dto.UsuarioDTO;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioResourceCorsJsonTest extends AbstractIntegretationTest {
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static UsuarioDTO usuario;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		usuario = new UsuarioDTO();
	}
	

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
		
		
		var accessToken =
				given()
					 .basePath("/auth/login")
					 .port(TestConfigs.SERVER_PORT)
					 .contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
				 .when()
				 	.post()
				 .then()
				   .statusCode(200)
				 .extract()
				    .body().as(TokenDTO.class)
				      .getAccessToken();
		
	
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+ accessToken)
				.setBasePath("/api/v1/usuarios")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockUsuario();
		
		var content =
			given()
			 .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS)
			 .body(usuario)
			 .when()
			 	.post()
			 .then()
			   .statusCode(200)
			 .extract()
			    .body().asString();
		
		UsuarioDTO createUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = createUsuario;
		
		Assertions.assertNotNull(createUsuario);
		Assertions.assertNotNull(createUsuario.getId());
		Assertions.assertNotNull(createUsuario.getIdade());
		Assertions.assertNotNull(createUsuario.getNome());
		Assertions.assertTrue(createUsuario.getId() > 0);
		
		
		Assertions.assertEquals("Douglas", createUsuario.getNome());
		Assertions.assertEquals(27,createUsuario.getIdade());
		
	
	}
	
	@Test
	@Order(2)
	public void testCreateComErorOrigin() throws JsonMappingException, JsonProcessingException {
		mockUsuario();
		
		var content =
				given()
				 .spec(specification)
				 .contentType(TestConfigs.CONTENT_TYPE_JSON)
				 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS_NOVO)
				     .body(usuario)
				 .when()
				 	.post()
				 .then()
				   .statusCode(403)
				 .extract()
				    .body().asString();
		
		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request",content);
		
		
	}
	
	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockUsuario();
		
		var content =
			 given()
				 .spec(specification)
				 .contentType(TestConfigs.CONTENT_TYPE_JSON)
				 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.get("{id}")
			 .then()
			   .statusCode(200)
			 .extract()
			    .body().asString();
		
		UsuarioDTO persitidoUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = persitidoUsuario;
		
		Assertions.assertNotNull(persitidoUsuario);
		Assertions.assertNotNull(persitidoUsuario.getId());
		Assertions.assertNotNull(persitidoUsuario.getIdade());
		Assertions.assertNotNull(persitidoUsuario.getNome());
		Assertions.assertTrue(persitidoUsuario.getId() > 0);
		
		
		Assertions.assertEquals("Douglas", persitidoUsuario.getNome());
		Assertions.assertEquals(27,persitidoUsuario.getIdade());
		
	
	}
	
	@Test
	@Order(4)
	public void testFindByIdComOriginError() throws JsonMappingException, JsonProcessingException {
		mockUsuario();
		
		var content =
				given()
				 .spec(specification)
				 .contentType(TestConfigs.CONTENT_TYPE_JSON)
				 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS_NOVO)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.get("{id}")
			 .then()
			   .statusCode(403)
			 .extract()
			    .body().asString();
		
		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request",content);
		
	
	}



	private void mockUsuario() {
		usuario.setIdade(27);
		usuario.setNome("Douglas");		
	}

}
