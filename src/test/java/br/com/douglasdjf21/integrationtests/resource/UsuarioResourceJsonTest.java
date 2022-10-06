package br.com.douglasdjf21.integrationtests.resource;

import static io.restassured.RestAssured.given;

import java.util.LinkedHashMap;

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
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioResourceJsonTest extends AbstractIntegretationTest {
	
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
				   .log().all()
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
			 .body(usuario)
			 .when()
			 	.post()
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().asString();
		
		UsuarioDTO createUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = createUsuario;
		
		Assertions.assertNotNull(createUsuario);
		Assertions.assertNotNull(createUsuario.getId());
		Assertions.assertNotNull(createUsuario.getIdade());
		Assertions.assertNotNull(createUsuario.getNome());
		Assertions.assertTrue(createUsuario.getId() > 0);
		Assertions.assertTrue(createUsuario.getAtivo());
		
		
		Assertions.assertEquals("Douglas", createUsuario.getNome());
		Assertions.assertEquals(27,createUsuario.getIdade());
		
	
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		usuario.setNome("Fulano");
		
		var content =
			given()
			 .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .pathParam("id", usuario.getId())
			 .body(usuario)
			 .when()
			 	.put("{id}")
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().asString();
		
		UsuarioDTO updateUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = updateUsuario;
		
		Assertions.assertNotNull(updateUsuario);
		Assertions.assertNotNull(updateUsuario.getId());
		Assertions.assertNotNull(updateUsuario.getIdade());
		Assertions.assertNotNull(updateUsuario.getNome());
		Assertions.assertTrue(updateUsuario.getId() > 0);
		Assertions.assertTrue(updateUsuario.getAtivo());
		
		Assertions.assertEquals(usuario.getId(),updateUsuario.getId());
		Assertions.assertEquals("Fulano", updateUsuario.getNome());
		Assertions.assertEquals(27,updateUsuario.getIdade());
		
	
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
			   .log().all()
			 .extract()
			    .body().asString();
		
		UsuarioDTO persitidoUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = persitidoUsuario;
		
		Assertions.assertNotNull(persitidoUsuario);
		Assertions.assertNotNull(persitidoUsuario.getId());
		Assertions.assertNotNull(persitidoUsuario.getIdade());
		Assertions.assertNotNull(persitidoUsuario.getNome());
		Assertions.assertTrue(persitidoUsuario.getId() > 0);
		Assertions.assertTrue(persitidoUsuario.getAtivo());
		
		
		
		Assertions.assertEquals("Fulano", persitidoUsuario.getNome());
		Assertions.assertEquals(27,persitidoUsuario.getIdade());
		
	
	}
	
	@Test
	@Order(4)
	public void testDisableById() throws JsonMappingException, JsonProcessingException {
		
		var content =
			 given()
				 .spec(specification)
				 .contentType(TestConfigs.CONTENT_TYPE_JSON)
				 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.patch("{id}")
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().asString();
		
		UsuarioDTO persitidoUsuario = objectMapper.readValue(content, UsuarioDTO.class);
		usuario = persitidoUsuario;
		
		Assertions.assertNotNull(persitidoUsuario);
		Assertions.assertNotNull(persitidoUsuario.getId());
		Assertions.assertNotNull(persitidoUsuario.getIdade());
		Assertions.assertNotNull(persitidoUsuario.getNome());
		Assertions.assertTrue(persitidoUsuario.getId() > 0);
		Assertions.assertFalse(persitidoUsuario.getAtivo());
		
		
	
	}
	
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
	
		given()
			  .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.delete("{id}")
			 .then()
			   .statusCode(204);
	
	}
	
	@Test
	@Order(6)
	public void testFindAllJsonPath() throws JsonMappingException, JsonProcessingException {
		JsonPath response =
			given()
			 .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .when()
			 	.get()
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			 	.jsonPath();
		
		LinkedHashMap lista = response.get();

		
		Assertions.assertNotNull(lista);
		Assertions.assertNotNull(lista.get("content"));
		Assertions.assertNotNull(lista.get("totalElements"));
		Assertions.assertNotNull(lista.get("size"));
		Assertions.assertTrue(Integer.valueOf(lista.get("totalElements").toString()) > 0);
	
	}
	
	@Test
	@Order(7)
	public void testFindAllJsonPathSemToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationSemToken = new RequestSpecBuilder()
																			.setBasePath("/api/v1/usuarios")
																			.setPort(TestConfigs.SERVER_PORT)
																			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
																			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
																			.build();
			given()
			 .spec(specificationSemToken)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .when()
			 	.get()
			 .then()
			   .statusCode(403)
			   .log().all();

	
	}
	
/*	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		var content =
			given()
			 .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_JSON)
			 .when()
			 	.get()
			 .then()
			   .statusCode(200)
			 .extract()
			 	.body()		 	
			 	.asString();
		
		List<UsuarioDTO> list = objectMapper.readValue(content, new TypeReference<List<UsuarioDTO>>() {});

		Assertions.assertNotNull(list);
		Assertions.assertTrue(list.size() > 0);
	
	}
*/

	private void mockUsuario() {
		usuario.setIdade(27);
		usuario.setNome("Douglas");	
		usuario.setAtivo(true);
	}

}
