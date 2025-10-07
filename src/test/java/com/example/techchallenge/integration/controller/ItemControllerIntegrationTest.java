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
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long createdItemId;
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

        String restaurantPayload = """
            {
                "name": "Restaurante dos Itens",
                "address": {
                    "street": "Rua das Comidas",
                    "number": "200",
                    "complement": "Loja 3",
                    "neighborhood": "Centro",
                    "city": "Uberlândia",
                    "state": "MG",
                    "postalCode": "38400000"
                },
                "cuisineType": "Mineira",
                "openingTime": "09:00:00",
                "closingTime": "22:00:00",
                "capacity": 50,
                "qtdTable": 15
            }
        """;

        ValidatableResponse restaurantResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(restaurantPayload)
                .when()
                .post("/api/restaurants")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());

        createdRestaurantId = Long.valueOf(restaurantResponse.extract().path("id").toString());
        Assertions.assertNotNull(createdRestaurantId, "O ID do restaurante deve ser criado antes dos testes de item.");
    }

    @Test
    @Order(1)
    void deveCriarItem() {
        String payload = """
            {
                "name": "Prato Executivo",
                "description": "Arroz, feijão, bife e batata frita",
                "price": 29.90,
                "available": true,
                "restaurantId": %d
            }
        """.formatted(createdRestaurantId);

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/items")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo("Prato Executivo"))
                .body("price", equalTo(29.90F));

        createdItemId = Long.valueOf(response.extract().path("id").toString());
        Assertions.assertNotNull(createdItemId, "O ID do item criado não deve ser nulo");
    }

    @Test
    @Order(2)
    void deveListarItens() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/items")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].name", notNullValue());
    }

    @Test
    @Order(3)
    void deveBuscarItemPorId() {
        Assertions.assertNotNull(createdItemId, "O ID do item criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdItemId)
                .when()
                .get("/api/items/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdItemId.intValue()))
                .body("name", equalTo("Prato Executivo"))
                .body("price", equalTo(29.90F));
    }

    @Test
    @Order(4)
    void deveAtualizarItem() {
        Assertions.assertNotNull(createdItemId, "O ID do item criado não pode ser nulo");

        String updatePayload = """
            {
                "name": "Prato Atualizado",
                "description": "Arroz, feijão, frango grelhado e salada",
                "price": 34.50,
                "available": true,
                "restaurantId": %d
            }
        """.formatted(createdRestaurantId);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdItemId)
                .body(updatePayload)
                .when()
                .put("/api/items/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Prato Atualizado"))
                .body("price", equalTo(34.50F));
    }

    @Test
    @Order(5)
    void deveExcluirItem() {
        Assertions.assertNotNull(createdItemId, "O ID do item criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdItemId)
                .when()
                .delete("/api/items/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(6)
    void deveRetornar404ParaItemInexistente() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 9999)
                .when()
                .get("/api/items/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(7)
    void deveRetornar401AoCriarSemToken() {
        String payload = """
            {
                "name": "Item Sem Token",
                "description": "Item inválido",
                "price": 19.90,
                "available": true,
                "restaurantId": %d
            }
        """.formatted(createdRestaurantId);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/items")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}