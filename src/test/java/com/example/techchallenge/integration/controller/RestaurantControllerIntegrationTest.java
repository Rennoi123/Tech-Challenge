package com.example.techchallenge.integration.controller;

import com.example.techchallenge.TechChallengeApplication;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
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
public class RestaurantControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long createdRestaurantId;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

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
    void deveCriarRestaurante() {
        String payload = """
            {
                "name": "Restaurante do Zé",
                "address": {
                    "street": "Rua Teste",
                    "number": "123",
                    "complement": "Apto 1",
                    "neighborhood": "Centro",
                    "city": "São Paulo",
                    "state": "SP",
                    "postalCode": "12345000"
                },
                "cuisineType": "Brasileira",
                "openingTime": "08:00:00",
                "closingTime": "23:00:00",
                "capacity": 40,
                "qtdTable": 20
            }
        """;

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/restaurants")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo("Restaurante do Zé"))
                .body("capacity", equalTo(40))
                .body("qtdTable", equalTo(20))
                .body("address.street", equalTo("Rua Teste"));

        createdRestaurantId = Long.valueOf(response.extract().path("id").toString());
        Assertions.assertNotNull(createdRestaurantId, "O ID do restaurante criado não deve ser nulo");
    }

    @Test
    @Order(2)
    void deveListarRestaurantes() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/restaurants")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].name", notNullValue());
    }

    @Test
    @Order(3)
    void deveBuscarRestaurantePorId() {
        Assertions.assertNotNull(createdRestaurantId, "O ID do restaurante criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdRestaurantId)
                .when()
                .get("/api/restaurants/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdRestaurantId.intValue()))
                .body("name", equalTo("Restaurante do Zé"))
                .body("capacity", equalTo(40));
    }

    @Test
    @Order(4)
    void deveAtualizarRestaurante() {
        Assertions.assertNotNull(createdRestaurantId, "O ID do restaurante criado não pode ser nulo");

        String updatePayload = """
            {
                "name": "Restaurante Atualizado",
                "address": {
                    "id": 999,
                    "street": "Rua Nova",
                    "number": "500",
                    "complement": "Loja 2",
                    "neighborhood": "Centro",
                    "city": "Uberlândia",
                    "state": "MG",
                    "postalCode": "38400000"
                },
                "cuisineType": "Japonesa",
                "openingTime": "10:00:00",
                "closingTime": "22:00:00",
                "capacity": 60,
                "qtdTable": 25
            }
        """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdRestaurantId)
                .body(updatePayload)
                .when()
                .put("/api/restaurants/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Restaurante Atualizado"))
                .body("qtdTable", equalTo(25));
    }

    @Test
    @Order(5)
    void deveExcluirRestaurante() {
        Assertions.assertNotNull(createdRestaurantId, "O ID do restaurante criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdRestaurantId)
                .when()
                .delete("/api/restaurants/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(6)
    void deveRetornar404ParaRestauranteInexistente() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 9999)
                .when()
                .get("/api/restaurants/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(7)
    void deveRetornar401AoCriarSemToken() {
        String payload = """
            {
                "name": "Restaurante Sem Token",
                "address": {
                    "street": "Rua Teste",
                    "number": "123",
                    "complement": "Apto 1",
                    "neighborhood": "Centro",
                    "city": "São Paulo",
                    "state": "SP",
                    "postalCode": "12345000"
                },
                "cuisineType": "Italiana",
                "openingTime": "09:00:00",
                "closingTime": "21:00:00",
                "capacity": 30,
                "qtdTable": 10
            }
        """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/restaurants")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
