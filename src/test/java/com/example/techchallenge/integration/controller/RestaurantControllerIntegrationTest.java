package com.example.techchallenge.integration.controller;

import com.example.techchallenge.integration.util.TestUtils;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestaurantControllerIntegrationTest {

    private int idRestaurante = 0;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/restaurants";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void deveListarTodosRestaurantes() {
        idRestaurante = getIdRestaurante();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    @DisplayName("Deve cadastrar um restaurante com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        String payload = testUtils.createRestaurantPayload();

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("name", equalTo("Restaurante Teste"));
    }

    @Test
    @DisplayName("Deve buscar um restaurante pelo ID com sucesso")
    void deveBuscarRestaurantePorIdComSucesso() {
        idRestaurante = getIdRestaurante();

        given()
                .pathParam("id", idRestaurante)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(idRestaurante))
                .body("name", equalTo("Restaurante Teste"));
    }

    @Test
    @DisplayName("Deve deletar restaurante com sucesso")
    void deveDeletarRestauranteComSucesso() {
        idRestaurante = getIdRestaurante();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}", idRestaurante)
                .then()
                .statusCode(204);
    }

    private int getIdRestaurante() {
        if (idRestaurante == 0) {
            idRestaurante = testUtils.criarRestauranteParaTeste();
        }
        return idRestaurante;
    }
}