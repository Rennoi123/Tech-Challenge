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
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long createdOrderId;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Login para obter token de administrador
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
    void deveCriarPedido() {
        String payload = """
            {
               "restaurantId": 1,
               "userId": 4,
               "deliveryOrder": true,
               "ItemOrderDTO": [
                 {
                   "itemId": 1,
                   "quantity": 6
                 }
               ]
             }
        """;

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("status", equalTo("PENDENTE"));

        createdOrderId = Long.valueOf(response.extract().path("id").toString());
        Assertions.assertNotNull(createdOrderId, "O ID do pedido criado não deve ser nulo");
    }

    @Test
    @Order(2)
    void deveBuscarPedidoPorId() {
        Assertions.assertNotNull(createdOrderId, "O ID do pedido criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdOrderId)
                .when()
                .get("/api/orders/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdOrderId.intValue()))
                .body("status", equalTo("PENDENTE"));
    }

    @Test
    @Order(3)
    void deveListarPedidosPendentes() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/orders/pending")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(4)
    void deveAtualizarPedidoParaEmPreparo() {
        Assertions.assertNotNull(createdOrderId, "O ID do pedido criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdOrderId)
                .when()
                .put("/api/orders/{id}/preparing")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("EM_PREPARO"));
    }

    @Test
    @Order(5)
    void deveAtualizarPedidoParaFinalizado() {
        Assertions.assertNotNull(createdOrderId, "O ID do pedido criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdOrderId)
                .when()
                .put("/api/orders/{id}/complete")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("FINALIZADO"));
    }

    @Test
    @Order(6)
    void deveCancelarPedido() {
        Assertions.assertNotNull(createdOrderId, "O ID do pedido criado não pode ser nulo");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdOrderId)
                .when()
                .put("/api/orders/{id}/cancel")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("CANCELADO"));
    }

    @Test
    @Order(7)
    void deveListarPedidosCancelados() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/orders/cancelled")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(8)
    void deveListarPedidosFinalizados() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/orders/completed")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(9)
    void deveRetornar404ParaPedidoInexistente() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 9999)
                .when()
                .get("/api/orders/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
