# Tech Challenge Backend

## Descrição do Projeto
O Tech Challenge Backend é um sistema robusto projetado para gerenciar operações de restaurantes e interações com clientes. Este projeto tem como objetivo fornecer um sistema de gerenciamento compartilhado para múltiplos restaurantes, permitindo que os clientes escolham opções de refeições com base na culinária, em vez da qualidade do sistema de gerenciamento.

## Funcionalidades
- Gerenciamento completo de usuários para proprietários de restaurantes e clientes.
- Registro de usuários, validação de login e gerenciamento de dados.
- Gerenciamento de restaurantes e seus itens de menu (comidas).
- Autenticação de usuários usando Spring Security com BCrypt para codificação de senhas.
- Validação de dados de entrada com Jakarta Validation.
- Integração com um banco de dados relacional (PostgreSQL).
- Suporte ao Docker para fácil implantação e escalabilidade.

## Tecnologias Utilizadas
- Spring Boot: Framework para construção da aplicação backend.
- Docker: Plataforma de conteinerização para implantação da aplicação.
- JPA (Java Persistence API): Para interações com o banco de dados.
- Maven: Ferramenta de automação de build para projetos Java.
- Spring Security: permissão de acesso total e codificação de senha.
- Lombok: Para reduzir boilerplate code.
- ModelMapper: Para mapeamento de objetos.
- Jakarta Validation: Para validação de dados.
- PostgreSQL: Banco de dados relacional.
- Rest Assured: Framework para testes de integração de API.
- JUnit 5 & Mockito: Para testes unitários e de integração.

## Estrutura do Projeto
```
tech-challenge-backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── techchallenge
│   │   │               ├── TechChallengeApplication.java
│   │   │               ├── controller
│   │   │               │   ├── ItemController.java
│   │   │               │   ├── RestaurantController.java
│   │   │               │   └── UserController.java
│   │   │               ├── dto
│   │   │               │   ├── Request
│   │   │               │   │   ├── AddressRequest.java
│   │   │               │   │   ├── ItemRequest.java
│   │   │               │   │   ├── RestaurantRequest.java
│   │   │               │   │   ├── UpdatePasswordRequest.java
│   │   │               │   │   └── UserRequest.java
│   │   │               │   └── Response
│   │   │               │   │   ├── AddressResponse.java
│   │   │               │   │   ├── ItemResponse.java
│   │   │               │   │   ├── RestaurantResponse.java
│   │   │               │   │   └── UserResponse.java
│   │   │               ├── entities
│   │   │               │   ├── AddressEntity.java
│   │   │               │   ├── ItemEntity.java
│   │   │               │   ├── RestaurantEntity.java
│   │   │               │   └── UserEntity.java
│   │   │               ├── enums
│   │   │               │   └── UserRoles.java
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── InvalidCredentialsException.java
│   │   │               │   └── UserNotFoundException.java
│   │   │               ├── repository
│   │   │               │   ├── AddressRepository.java
│   │   │               │   ├── ItemRepository.java
│   │   │               │   ├── RestaurantRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── security
│   │   │               │   └── SecurityConfig.java
│   │   │               └── service
│   │   │                   ├── AddressService.java
│   │   │                   ├── ItemService.java
│   │   │                   ├── RestaurantService.java
│   │   │                   └── UserService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── techchallenge
│                       ├── integration
│                       │   ├── controller
│                       │   │   ├── ItemControllerIntegrationTest.java
│                       │   │   ├── RestaurantControllerIntegrationTest.java
│                       │   │   └── UserControllerIntegrationTest.java
│                       │   └── util
│                       │       └── TestUtils.java
│                       └── unit
│                           ├── controller
│                           │   ├── ItemControllerTest.java
│                           │   ├── RestaurantControllerTest.java
│                           │   └── UserControllerTest.java
│                           └── service
│                               ├── AddressServiceTest.java
│                               ├── ItemServiceTest.java
│                               ├── RestaurantServiceTest.java
│                               └── UserServiceTest.java
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Instruções de Configuração
1. Clone o repositório:
   ```bash
   git clone [https://github.com/Rennoi123/Tech-Challenge.git](https://github.com/Rennoi123/Tech-Challenge.git)
   cd tech-challenge-backend
   ```

2. Compile o projeto usando o Maven:
   ```
   mvn clean install
   ```

3. Execute os testes (opcional):
   ```
   mvn test
   ```
   Os testes de unidade e integração foram executados com sucesso.


4. Configure a conexão com o banco de dados. O projeto está configurado para usar PostgreSQL com as seguintes variáveis de ambiente no docker-compose.yml e application.properties:
   ```
   SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tech_challenge_db
   SPRING_DATASOURCE_USERNAME: admin
   SPRING_DATASOURCE_PASSWORD: 123456
   ```

5. Inicie a aplicação usando o Docker Compose para subir a aplicação e o banco de dados PostgreSQL:
   ```
   docker-compose up --build
   ```
   
6. A aplicação estará acessível em `http://localhost:8080`. O banco de dados PostgreSQL estará acessível na porta 5432.

7. Acesse as collections do Postman para teste.

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/22747303-00183e48-4730-488a-a651-b28b0f9106a7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D22747303-00183e48-4730-488a-a651-b28b0f9106a7%26entityType%3Dcollection%26workspaceId%3D492ed606-853d-4a06-87d3-27a14c08eaa8)

# Endpoints da API 

## 📌 Endpoints de Usuário (UserController)

### Registro de Usuário (Cliente)
- **POST** `/api/users/register`  
Cria um novo usuário com a role `CLIENTE`.

### Registro de Usuário (Admin/Restaurante)
- **POST** `/api/users/register-admin`  
Cria um novo usuário com a role `RESTAURANTE`.

### Login de Usuário
- **POST** `/api/users/login`  
Valida as credenciais do usuário.

### Obter Todos os Usuários
- **GET** `/api/users`  
Retorna uma lista de todos os usuários registrados.

### Obter Detalhes do Usuário por ID
- **GET** `/api/users/{id}`  
Retorna os detalhes de um usuário específico.

### Atualizar Usuário
- **PUT** `/api/users/{id}`  
Atualiza os dados de um usuário existente.

### Excluir Usuário
- **DELETE** `/api/users/{id}`  
Remove um usuário do sistema.

### Atualizar Senha
- **POST** `/api/users/update-password`  
Atualiza a senha de um usuário.

## 🍕 Endpoints de Restaurante (RestaurantController)

### Criar Restaurante
- **POST** `/api/restaurants`

### Cria um novo restaurante.
- **GET** `/api/restaurants`  
Obter Todos os Restaurantes

### Retorna uma lista de todos os restaurantes.
- **GET** `/api/restaurants/{id}`   
Obter Detalhes do Restaurante por ID

### Retorna os detalhes de um restaurante específico.
- **DELETE** `/api/restaurants/{id}`   
Remove um restaurante do sistema.

## 🍔 Endpoints de Item (ItemController)

### Criar Item
- **POST** `/api/items`    
Cria um novo item no menu de um restaurante.

### Atualizar Item
- **PUT** `/api/items/{id}`   
Atualiza os dados de um item existente.

### Obter Itens de um Restaurante
- **GET** `/api/items/restaurant/{restaurantId}`   
Retorna todos os itens do menu de um restaurante específico.

### Excluir Item
- **DELETE** `/api/items/{id}`   
Remove um item do sistema.

---


