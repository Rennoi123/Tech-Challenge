package com.example.techchallenge.integration.controller;

import com.example.techchallenge.TechChallengeApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TechChallengeApplication.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long createdUserId;
    private String createdUserEmail;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;

        String loginPayload = """
            {
                "email": "admin@example.com",
                "password": "senha123"
            }
        """;

        token = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();
    }

    @Test
    @Order(1)
    void deveCriarUsuario() {
        createdUserEmail = "user_" + System.currentTimeMillis() + "@test.com";

        String payload = """
            {
                "name": "Cliente",
                "email": "%s",
                "password": "123456",
                "address": {
                    "street": "Rua Arduino Grande",
                    "number": "123",
                    "complement": "casa",
                    "neighborhood": "Pacaembu",
                    "city": "Uberlândia",
                    "state": "MG",
                    "postalCode": "01234567"
                }
            }
        """.formatted(createdUserEmail);

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo("Cliente"))
                .body("email", equalTo(createdUserEmail))
                .body("userRoles", equalTo("CLIENTE"))
                .body("address.street", equalTo("Rua Arduino Grande"));

        createdUserId = Long.valueOf(response.extract().path("id").toString());
        Assertions.assertNotNull(createdUserId, "O ID do usuário criado não deve ser nulo");
    }

    @Test
    @Order(2)
    void deveAutenticarUsuario() {
        String responseBody = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "email": "%s",
                        "password": "123456"
                    }
                """.formatted(createdUserEmail))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.startsWith("ey"), "O token JWT deve iniciar com 'ey'");
    }

    @Test
    @Order(3)
    void deveListarUsuarios() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(4)
    void deveBuscarUsuarioPorId() {
        Assertions.assertNotNull(createdUserId, "O ID do usuário criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdUserId)
                .when()
                .get("/api/users/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdUserId.intValue()))
                .body("email", equalTo(createdUserEmail))
                .body("userRoles", notNullValue());
    }


    @Test
    @Order(5)
    void deveAtualizarUsuario() {
        Assertions.assertNotNull(createdUserId, "O ID do usuário criado não pode ser nulo");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                {
                    "id": %d,
                    "name": "Usuário Teste Atualizado",
                    "email": "%s",
                    "password": "novaSenha123",
                    "address": {
                        "id": %d,
                        "street": "Rua das Flores",
                        "number": "123",
                        "complement": "Apto 10",
                        "neighborhood": "Centro",
                        "city": "São Paulo",
                        "state": "SP",
                        "postalCode": "01234567"
                    }
                }
            """.formatted(createdUserId, createdUserEmail, createdUserId))
                .when()
                .put("/api/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdUserId.intValue()))
                .body("email", equalTo(createdUserEmail));
    }

    @Test
    @Order(6)
    void deveExcluirUsuario() {
        Assertions.assertNotNull(createdUserId, "O ID do usuário criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdUserId)
                .when()
                .delete("/api/users/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(7)
    void deveRetornar404UsuarioInexistente() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 99999)
                .when()
                .get("/api/users/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
