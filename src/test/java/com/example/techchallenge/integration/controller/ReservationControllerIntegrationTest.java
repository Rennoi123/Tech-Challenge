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
public class ReservationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long createdReservationId;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Autenticação do usuário admin
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
    void deveCriarReserva() {
        String payload = """
            {
              "restaurantId": 1,
              "userId": 1,
              "reservationTime": "15/10/2025 20:00",
              "numberOfPeople": 2
            }
        """;

        ValidatableResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("restaurantId", equalTo(1))
                .body("numberOfPeople", equalTo(2))
                .body("status", equalTo("PENDENTE"));

        createdReservationId = Long.valueOf(response.extract().path("id").toString());
        Assertions.assertNotNull(createdReservationId, "O ID da reserva criada não deve ser nulo");
    }

    @Test
    @Order(2)
    void deveListarReservas() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(3)
    void deveAtualizarStatusDaReserva() {
        Assertions.assertNotNull(createdReservationId, "O ID da reserva criada não pode ser nulo");

        String payload = """
            {
              "status": "APROVADA"
            }
        """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", createdReservationId)
                .body(payload)
                .when()
                .put("/api/reservations/status/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdReservationId.intValue()))
                .body("status", equalTo("APROVADA"));
    }

    @Test
    @Order(4)
    void deveRetornar404ParaReservaInexistente() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", 9999)
                .body("""
                    {
                        "status": "CANCELADA"
                    }
                """)
                .when()
                .put("/api/reservations/status/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(containsString("Reserva não encontrada com id: 9999"));
    }
}
