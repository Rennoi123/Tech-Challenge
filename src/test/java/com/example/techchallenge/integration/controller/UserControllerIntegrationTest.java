package com.example.techchallenge.integration.controller;

import com.example.techchallenge.integration.util.TestUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerIntegrationTest {

    private int idUsuario = 0;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/users";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarNovoUsuarioComSucesso() {
        String email = "mairany_" + System.currentTimeMillis() + "@test.com";

        String payload = """
            {
              "name": "Mairany Teste",
              "email": "%s",
              "password": "123456",
              "role": "CLIENTE",
              "address": {
                "street": "Rua Teste",
                "number": "123",
                "complement": "Apto 1",
                "neighborhood": "Centro",
                "city": "São Paulo",
                "state": "SP",
                "postalCode": "12345000"
              }
            }
        """.formatted(email);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/register")
        .then()
            .statusCode(201)
            .contentType(ContentType.TEXT)
            .body(equalTo("Usuário criado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar um usuário por ID")
    void deveRetornarUsuarioPorId() {
        idUsuario = getIdUsuario();

        given()
            .pathParam("id", idUsuario)
        .when()
            .get("/{id}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(idUsuario));
    }

    @Test
    @DisplayName("Deve validar login com sucesso")
    void deveValidarLoginComSucesso() {
        String payload = """
            {
              "email": "user_1754175006607@test.com",
              "password": "senha123"
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body(equalTo("Usuário logado com sucesso."));
    }

    @Test
    @DisplayName("Deve atualizar a senha com sucesso")
    void deveAtualizarSenhaComSucesso() {
        idUsuario = getIdUsuario();

        String payload = """
            {
              "oldPassword": "senha123",
              "newPassword": "novaSenha456"
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/update-password?id=" + idUsuario)
        .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso")
    void deveExcluirUsuarioComSucesso() {
        idUsuario = getIdUsuario();

        given()
            .pathParam("id", idUsuario)
        .when()
            .delete("/{id}")
        .then()
            .statusCode(204);
    }

    private int getIdUsuario() {
        if (idUsuario == 0) {
            idUsuario = testUtils.criarUsuarioParaTeste();
        }
        return idUsuario;
    }
}