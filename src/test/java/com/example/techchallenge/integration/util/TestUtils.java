/*package com.example.techchallenge.integration.util;

import com.example.techchallenge.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;

    static {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    public int criarUsuarioParaTeste() {
        return criarUsuarioParaTeste(null);
    }

    public int criarUsuarioParaTeste(String email) {
        email = (email != null && !email.isEmpty()) ? email : "user_" + System.currentTimeMillis() + "@test.com";

        String payload = """
            {
              "name": "Usuário Teste",
              "email": "%s",
              "password": "senha123",
              "role": "CLIENTE",
              "address": {
                "street": "Rua Teste",
                "number": "10",
                "complement": "Bloco B",
                "neighborhood": "Bairro",
                "city": "Cidade",
                "state": "ST",
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
                .statusCode(201);

        return userRepository.findByEmail(email).get().getId().intValue();
    }

    public int criarUsuarioAdmin() {
        String email = "admin_" + System.currentTimeMillis() + "@test.com";

        String payload = """
            {
              "name": "Usuário Admin Teste",
              "email": "%s",
              "password": "senha123",
              "address": {
                "street": "Rua Exemplo",
                "number": "123",
                "complement": "Sala 10",
                "neighborhood": "Centro",
                "city": "Cidade Teste",
                "state": "SP",
                "postalCode": "12345000"
              }
            }
        """.formatted(email);

        given()
                .basePath("/api/users")
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/register-admin")
                .then()
                .statusCode(201);

        return userRepository.findByEmail(email).get().getId().intValue();
    }

    public String createRestaurantPayload() {
        int id = criarUsuarioAdmin();
        return createRestaurantPayload(id);
    }

    public static String createRestaurantPayload(int ownerId) {
        return """
            {
              "name": "Restaurante Teste",
              "address": {
                "street": "Rua das Flores",
                "number": "123",
                "complement": "Apto 10",
                "neighborhood": "Centro",
                "city": "São Paulo",
                "state": "SP",
                "postalCode": "01234567"
              },
              "cuisineType": "Italiana",
              "openingTime": "10:00:00",
              "closingTime": "22:00:00",
              "ownerId": %d
            }
        """.formatted(ownerId);
    }

    public int criarRestauranteParaTeste() {
        int ownerId = criarUsuarioAdmin();
        String payload = createRestaurantPayload(ownerId);

        return given()
                .basePath("/api/restaurants")
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}*/