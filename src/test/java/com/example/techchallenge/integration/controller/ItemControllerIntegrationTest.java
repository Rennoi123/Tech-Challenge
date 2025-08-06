package com.example.techchallenge.integration.controller;

import com.example.techchallenge.TechChallengeApplication;
import com.example.techchallenge.integration.util.TestUtils;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TechChallengeApplication.class)
@ActiveProfiles("test")
@Transactional
class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private int idRestaurant = 0;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api/items";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void beforeEachTest() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Deve criar um item com sucesso")
    void deveCriarItemComSucesso() {
        idRestaurant = getIdRestaurant();
        String payload = gerarPayloadItem("Pizza Margherita", "Tradicional italiana", 39.90);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post()
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Pizza Margherita"))
            .body("restaurantId", equalTo(idRestaurant));
    }

    @Test
    @DisplayName("Deve atualizar item com sucesso")
    void deveAtualizarItemComSucesso() {
        int id = criarItemParaTeste("Pizza Mussarela");

        String payloadAtualizado = gerarPayloadItem("Pizza Calabresa", "Com cebola e azeitona", 44.90);

        given()
            .contentType(ContentType.JSON)
            .body(payloadAtualizado)
            .pathParam("id", id)
        .when()
            .put("/{id}")
        .then()
            .statusCode(200)
            .body("name", equalTo("Pizza Calabresa"))
            .body("price", equalTo(44.90F));
    }

    @Test
    @DisplayName("Deve listar itens por restaurante")
    void deveListarItensPorRestaurante() {
        idRestaurant = getIdRestaurant();
        criarItemParaTeste("Pizza Portuguesa");

        given()
            .pathParam("restaurantId", idRestaurant)
        .when()
            .get("/restaurant/{restaurantId}")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @DisplayName("Deve excluir item com sucesso")
    void deveExcluirItemComSucesso() {
        int id = criarItemParaTeste("Pizza Quatro Queijos");

        given()
            .pathParam("id", id)
        .when()
            .delete("/{id}")
        .then()
            .statusCode(204);
    }

    private int criarItemParaTeste(String nome) {
        String payload = gerarPayloadItem(nome, "Descrição padrão", 49.90);

        return given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post()
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private String gerarPayloadItem(String name, String description, double price) {
        idRestaurant = getIdRestaurant();

        return """
            {
              "name": "%s",
              "description": "%s",
              "price": %s,
              "dineInOnly": false,
              "photoPath": "/images/default.jpg",
              "restaurantId": %d
            }
        """.formatted(
                name,
                description,
                BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).toPlainString(),
                idRestaurant
            );
    }

    private int getIdRestaurant() {
        if (idRestaurant == 0) {
            idRestaurant = testUtils.criarRestauranteParaTeste();
        }
        return idRestaurant;
    }
}