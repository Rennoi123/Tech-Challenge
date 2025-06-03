# Tech Challenge Backend

## Descrição do Projeto
O Tech Challenge Backend é um sistema robusto projetado para gerenciar operações de restaurantes e interações com clientes. Este projeto tem como objetivo fornecer um sistema de gerenciamento compartilhado para múltiplos restaurantes, permitindo que os clientes escolham opções de refeições com base na culinária, em vez da qualidade do sistema de gerenciamento.

## Funcionalidades
- Gerenciamento de usuários para proprietários de restaurantes e clientes.
- Registro de usuários, validação de login e gerenciamento de dados.
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
│   │   │               │   └── UserController.java
│   │   │               ├── dto
│   │   │               │   ├── AddressRequest.java
│   │   │               │   ├── AddressResponse.java
│   │   │               │   ├── UpdatePasswordRequest.java
│   │   │               │   ├── UserRequest.java
│   │   │               │   └── UserResponse.java
│   │   │               ├── entities
│   │   │               │   ├── AddressEntity.java
│   │   │               │   └── UserEntity.java
│   │   │               ├── enums
│   │   │               │   └── UserRoles.java
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── InvalidCredentialsException.java
│   │   │               │   └── UserNotFoundException.java
│   │   │               ├── repository
│   │   │               │   ├── AddressRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── security
│   │   │               │   └── SecurityConfig.java
│   │   │               └── service
│   │   │                   ├── AddressService.java
│   │   │                   └── UserService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── techchallenge
│                       └── TechChallengeApplicationTests.java
├── .idea
│   ├── compiler.xml
│   ├── jarRepositories.xml
│   ├── misc.xml
│   ├── uiDesigner.xml
│   └── vcs.xml
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

3. Configure a conexão com o banco de dados. O projeto está configurado para usar PostgreSQL com as seguintes variáveis de ambiente no docker-compose.yml e application.properties:
   ```
   SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tech_challenge_db
   SPRING_DATASOURCE_USERNAME: admin
   SPRING_DATASOURCE_PASSWORD: 123456
   ```

4. Inicie a aplicação usando o Docker Compose para subir a aplicação e o banco de dados PostgreSQL:
   ```
   docker-compose up --build
   ```
   
6. A aplicação estará acessível em `http://localhost:8080`. O banco de dados PostgreSQL estará acessível na porta 5432.

7. Acesse as collections do Postman para teste.

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/22747303-00183e48-4730-488a-a651-b28b0f9106a7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D22747303-00183e48-4730-488a-a651-b28b0f9106a7%26entityType%3Dcollection%26workspaceId%3D492ed606-853d-4a06-87d3-27a14c08eaa8)

# Endpoints da API

Os endpoints da API são gerenciados pelo **UserController**.

## 📌 Endpoints de Usuário

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

---


