# Tech Challenge Backend

## Descrição do Projeto
O Tech Challenge Backend é um sistema robusto projetado para gerenciar operações de restaurantes e interações com clientes. Este projeto tem como objetivo fornecer um sistema de gerenciamento compartilhado para múltiplos restaurantes, permitindo que os clientes escolham opções de refeições com base na culinária, em vez da qualidade do sistema de gerenciamento.

## Funcionalidades
- Gerenciamento de usuários para proprietários de restaurantes e clientes.
- Registro de usuários, validação de login e gerenciamento de dados.
- Integração com um banco de dados relacional (PostgreSQL, MySQL ou H2).
- Suporte ao Docker para fácil implantação e escalabilidade.

## Tecnologias Utilizadas
- Spring Boot: Framework para construção da aplicação backend.
- Docker: Plataforma de conteinerização para implantação da aplicação.
- JPA (Java Persistence API): Para interações com o banco de dados.
- Maven: Ferramenta de automação de build para projetos Java.

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
│   │   │               ├── model
│   │   │               │   └── User.java
│   │   │               ├── repository
│   │   │               │   └── UserRepository.java
│   │   │               └── service
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
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## Instruções de Configuração
1. Clone o repositório:
   ```
   git clone <https://github.com/Rennoi123/Tech-Challenge.git>
   cd tech-challenge-backend
   ```

2. Compile o projeto usando o Maven:
   ```
   mvn clean install
   ```

3. Configure a conexão com o banco de dados em `src/main/resources/application.properties`.

4. Inicie a aplicação usando o Docker Compose:
   ```
   docker-compose up
   ```

5. Acesse a aplicação em `http://localhost:8080`.

6. Acesso nossaS collections do Postman para teste.

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/22747303-00183e48-4730-488a-a651-b28b0f9106a7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D22747303-00183e48-4730-488a-a651-b28b0f9106a7%26entityType%3Dcollection%26workspaceId%3D492ed606-853d-4a06-87d3-27a14c08eaa8)

## Endpoints da API
- **Registro de Usuário**: `POST /api/userEntities/register`
- **Login de Usuário**: `POST /api/userEntities/login`
- **Obter Detalhes do Usuário**: `GET /api/userEntities/{id}`
- **Atualizar Usuário**: `PUT /api/userEntities/{id}`
- **Excluir Usuário**: `DELETE /api/userEntities/{id}`

## Testes
Os testes unitários estão localizados no diretório `src/test/java/com/example/techchallenge`. Execute os testes usando:
```
mvn test
```


