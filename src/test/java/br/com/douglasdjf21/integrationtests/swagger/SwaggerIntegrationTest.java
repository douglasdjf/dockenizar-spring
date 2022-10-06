package br.com.douglasdjf21.integrationtests.swagger;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.douglasdjf21.confis.TestConfigs;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegretationTest {

	@Test
	public void deveriaMostrarSwagger() {
		var content =
			given()
			 .basePath("/swagger-ui/index.html")
			 .port(TestConfigs.SERVER_PORT)
			 .when()
			 	.get()
			 .then()
			   .statusCode(200)
			 .extract()
			    .body().asString();
		
		Assertions.assertTrue(content.contains("Swagger UI"));
	}

}
