package br.com.douglasdjf21.integrationtests.resource;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

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
import br.com.douglasdjf21.dto.security.TokenDTO;
import br.com.douglasdjf21.integrationtests.dto.AccountCredentialsDTO;
import br.com.douglasdjf21.integrationtests.dto.UsuarioDTO;
import br.com.douglasdjf21.integrationtests.mapper.YMLMapper;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioResourceYAMLTest extends AbstractIntegretationTest {
	
	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	private static UsuarioDTO usuario;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();	
		usuario = new UsuarioDTO();
	}
	

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
		
		
		var accessToken =
				given()
				  .config(RestAssuredConfig
			    		 .config()
			    		 .encoderConfig(EncoderConfig.encoderConfig()
			    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
					 .basePath("/auth/login")
					 .port(TestConfigs.SERVER_PORT)
					 .contentType(TestConfigs.CONTENT_TYPE_YML)
					 .accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user,objectMapper)
				 .when()
				 	.post()
				 .then()
				   .statusCode(200)
				   .log().all()
				 .extract()
				    .body().as(TokenDTO.class,objectMapper)
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
			 .config(RestAssuredConfig
		    		 .config()
		    		 .encoderConfig(EncoderConfig.encoderConfig()
		    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
			 .accept(TestConfigs.CONTENT_TYPE_YML)
			 .body(usuario,objectMapper)
			 .when()
			 	.post()
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().
			       as(UsuarioDTO.class,objectMapper);
		
	
		usuario = content;
		
		Assertions.assertNotNull(content);
		Assertions.assertNotNull(content.getId());
		Assertions.assertNotNull(content.getIdade());
		Assertions.assertNotNull(content.getNome());
		Assertions.assertTrue(content.getId() > 0);
		
		
		Assertions.assertEquals("Douglas", content.getNome());
		Assertions.assertEquals(27,content.getIdade());
		
	
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		usuario.setNome("Fulano");
		
		var content =
			given()
			 .spec(specification)
			 .config(RestAssuredConfig
		    		 .config()
		    		 .encoderConfig(EncoderConfig.encoderConfig()
		    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
			 .accept(TestConfigs.CONTENT_TYPE_YML)
			 .pathParam("id", usuario.getId())
			 .body(usuario,objectMapper)
			 .when()
			 	.put("{id}")
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().
			       as(UsuarioDTO.class,objectMapper);
		
	
		usuario = content;
		
		Assertions.assertNotNull(content);
		Assertions.assertNotNull(content.getId());
		Assertions.assertNotNull(content.getIdade());
		Assertions.assertNotNull(content.getNome());
		Assertions.assertTrue(content.getId() > 0);
		
		Assertions.assertEquals(usuario.getId(),content.getId());
		Assertions.assertEquals("Fulano", content.getNome());
		Assertions.assertEquals(27,content.getIdade());
		
	
	}

	
	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockUsuario();
		
		var persitidoUsuario =
			 given()
				 .spec(specification)
				 .config(RestAssuredConfig
			    		 .config()
			    		 .encoderConfig(EncoderConfig.encoderConfig()
			    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				 .contentType(TestConfigs.CONTENT_TYPE_YML)
				 .accept(TestConfigs.CONTENT_TYPE_YML)
				 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_DOUGLAS)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.get("{id}")
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().
			       as(UsuarioDTO.class,objectMapper);
		
		
		usuario = persitidoUsuario;
		
		Assertions.assertNotNull(persitidoUsuario);
		Assertions.assertNotNull(persitidoUsuario.getId());
		Assertions.assertNotNull(persitidoUsuario.getIdade());
		Assertions.assertNotNull(persitidoUsuario.getNome());
		Assertions.assertTrue(persitidoUsuario.getId() > 0);
		
		
		Assertions.assertEquals("Fulano", persitidoUsuario.getNome());
		Assertions.assertEquals(27,persitidoUsuario.getIdade());
		
	
	}
	
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
	
		given()
			  .spec(specification)
			  .config(RestAssuredConfig
			    		 .config()
			    		 .encoderConfig(EncoderConfig.encoderConfig()
			    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
			 .accept(TestConfigs.CONTENT_TYPE_YML)
			 .pathParam("id", usuario.getId())
			 .when()
			 	.delete("{id}")
			 .then()
			   .statusCode(204);
	
	}
	
	@Test
	@Order(5)
	public void testFindAllYmlPath() throws JsonMappingException, JsonProcessingException {
		var response =
			given()
			 .spec(specification)
			 .config(RestAssuredConfig
		    		 .config()
		    		 .encoderConfig(EncoderConfig.encoderConfig()
		    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
			 .accept(TestConfigs.CONTENT_TYPE_YML)
			 .when()
			 	.get()
			 .then()
			   .statusCode(200)
			   .log().all()
			 .extract()
			    .body().
			       as(Object.class,objectMapper);
		
		
		Assertions.assertNotNull(response);
	
	}
	
	@Test
	@Order(5)
	public void testFindAllYmlPathSemToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationSemToken = new RequestSpecBuilder()
																			.setBasePath("/api/v1/usuarios")
																			.setPort(TestConfigs.SERVER_PORT)
																			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
																			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
																			.build();
			given()
			 .spec(specificationSemToken)
			 .config(RestAssuredConfig
		    		 .config()
		    		 .encoderConfig(EncoderConfig.encoderConfig()
		    				 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
			 .accept(TestConfigs.CONTENT_TYPE_YML)
			 .when()
			 	.get()
			 .then()
			   .statusCode(403)
			   .log().all()
			   .extract()
			    .body().
			       as(UsuarioDTO[].class,objectMapper);

	
	}
	
/*	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		var content =
			given()
			 .spec(specification)
			 .contentType(TestConfigs.CONTENT_TYPE_YML)
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
	}

}
