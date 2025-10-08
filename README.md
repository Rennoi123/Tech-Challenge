# 🍽️ Tech Challenge Backend – Fase 3

## Descrição do Projeto
O **Tech Challenge Backend** é um sistema robusto desenvolvido em **Spring Boot**, projetado para gerenciar operações de restaurantes e interações com clientes.  
Esta fase expande o projeto para incluir **autenticação com JWT**, **comunicação assíncrona com RabbitMQ**, **consultas flexíveis com GraphQL**, e **testes de integração**, tudo estruturado em **Clean Architecture**.

O objetivo é permitir que múltiplos restaurantes ofereçam seus serviços em uma mesma plataforma, onde clientes possam explorar cardápios, realizar reservas e pedidos de forma simples e segura.

---

## Funcionalidades
- ✅ **Autenticação e autorização** com JWT (expiração de 6 horas).
- ✅ **Perfis de acesso:** `ADMIN` (gerente) e `CLIENT` (cliente).
- ✅ **CRUD completo** de:
    - Usuários
    - Restaurantes
    - Cardápios
    - Reservas de mesas (com verificação de conflito de horário)
    - Pedidos (delivery e presencial)
- ✅ **Notificações de pedidos** (criado/finalizado) via **RabbitMQ**.
- ✅ **GraphQL API** para histórico de pedidos, reservas e cardápios.
- ✅ **Testes automatizados**.
- ✅ **Execução via Docker Compose** (banco, aplicação e mensageria integrados).

---

## Tecnologias Utilizadas
- **Spring Boot 3:** Framework principal do backend.
- **Spring Security + JWT:** Autenticação e controle de acesso.
- **RabbitMQ:** Mensageria para eventos assíncronos de pedidos.
- **Spring for GraphQL:** API de consultas flexíveis (histórico, reservas, cardápio).
- **Spring Data JPA:** Persistência com banco relacional.
- **PostgreSQL:** Banco de dados principal.
- **Maven:** Automação de build e dependências.
- **Docker & Docker Compose:** Conteinerização e orquestração.
- **Lombok:** Redução de código repetitivo.
- **ModelMapper:** Conversão de DTOs e entidades.
- **Jakarta Validation:** Validação de dados.
- **JUnit 5 & Mockito:** Testes unitários.
- **Rest Assured:** Testes de integração de API.
- **JaCoCo:** Geração de relatórios de cobertura de testes.

---

## Estrutura do Projeto
```
tech-challenge-backend
├── adapters
│   ├── controllers        # Endpoints REST
│   ├── presenters         # Conversão entre entidades e DTOs
│   └── gateways           # Integração com repositórios e mensageria
├── core
│   ├── domain             # Entidades de negócio
│   ├── dto                # Objetos de transferência de dados (Request/Response)
│   ├── enums              # Constantes de domínio (Roles, Status, etc.)
│   ├── exception          # Exceções customizadas
│   └── usecase            # Casos de uso (regras de aplicação)
├── infrastructure
│   ├── entities           # Entidades JPA
│   ├── repository         # Interfaces de persistência
│   ├── security           # Configuração de segurança e JWT
│   ├── messaging          # Configuração e consumers RabbitMQ
│   ├── graphql            # Schemas e resolvers GraphQL
│   └── config             # Beans e propriedades de ambiente
├── test
│   ├── unit               # Testes unitários
│   └── integration        # Testes de integração (RestAssured)
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Instruções de Configuração

1️⃣ **Clone o repositório:**
```bash
git clone https://github.com/Rennoi123/Tech-Challenge.git
cd tech-challenge-backend
```

2️⃣ **Compile o projeto com Maven:**
```bash
mvn clean install
```

3️⃣ **Execute os testes (opcional):**
```bash
mvn test
```

4️⃣ **Configuração do banco de dados e mensageria (via Docker):**
```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tech_challenge_db
SPRING_DATASOURCE_USERNAME: admin
SPRING_DATASOURCE_PASSWORD: 123456
SPRING_RABBITMQ_HOST: rabbitmq
SPRING_RABBITMQ_PORT: 5672
SPRING_RABBITMQ_USERNAME: guest
SPRING_RABBITMQ_PASSWORD: guest
JWT_EXPIRATION: 21600000  # 6 horas
```

5️⃣ **Suba a aplicação com Docker Compose:**
```bash
docker-compose up --build
```

- API: <http://localhost:8080>
- RabbitMQ Dashboard: <http://localhost:15672> (guest / guest)
- PostgreSQL: Porta 5432

---

# Endpoints da API

## 📌 Endpoints de Usuário (UserController)

### Registro de Usuário (Cliente)
- **POST** `/api/users/register`  
  Cria um novo usuário com a role `CLIENT`.

### Registro de Usuário (Gerente)
- **POST** `/api/users/register-admin`  
  Cria um novo usuário com a role `ADMIN`.

### Login de Usuário
- **POST** `/api/users/login`  
  Valida as credenciais do usuário e retorna o token JWT.

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
  Atualiza a senha de um usuário autenticado.

---

## 🍕 Endpoints de Restaurante (RestaurantController)

### Criar Restaurante
- **POST** `/api/restaurants`  
  Cria um novo restaurante.

### Obter Todos os Restaurantes
- **GET** `/api/restaurants`  
  Retorna uma lista de todos os restaurantes.

### Obter Detalhes do Restaurante por ID
- **GET** `/api/restaurants/{id}`  
  Retorna os detalhes de um restaurante específico.

### Atualizar Restaurante
- **PUT** `/api/restaurants/{id}`  
  Atualiza as informações de um restaurante existente.

### Excluir Restaurante
- **DELETE** `/api/restaurants/{id}`  
  Remove um restaurante do sistema.

---

## 🍔 Endpoints de Item (ItemController)

### Criar Item
- **POST** `/api/items`  
  Cria um novo item no cardápio de um restaurante.

### Atualizar Item
- **PUT** `/api/items/{id}`  
  Atualiza os dados de um item existente.

### Obter Itens de um Restaurante
- **GET** `/api/items/restaurant/{restaurantId}`  
  Retorna todos os itens do cardápio de um restaurante específico.

### Excluir Item
- **DELETE** `/api/items/{id}`  
  Remove um item do cardápio.

---

## 📅 Endpoints de Reserva (ReservationController)

### Criar Reserva
- **POST** `/api/reservations`  
  Cria uma nova reserva de mesa, validando conflitos de horário.

### Obter Reservas de um Cliente
- **GET** `/api/reservations/user/{userId}`  
  Retorna todas as reservas associadas a um cliente específico.

### Obter Todas as Reservas
- **GET** `/api/reservations`  
  Retorna todas as reservas realizadas no sistema.

### Atualizar Reserva
- **PUT** `/api/reservations/{id}`  
  Atualiza os dados de uma reserva existente.

### Cancelar Reserva
- **DELETE** `/api/reservations/{id}`  
  Cancela uma reserva.

---

## 🛵 Endpoints de Pedido (OrderController)

### Criar Pedido
- **POST** `/api/orders`  
  Cria um novo pedido (delivery ou presencial) e publica evento de criação.

### Obter Pedidos de um Usuário
- **GET** `/api/orders/user/{userId}`  
  Retorna todos os pedidos feitos por um cliente específico.

### Obter Todos os Pedidos
- **GET** `/api/orders`  
  Retorna uma lista de todos os pedidos cadastrados no sistema.

### Atualizar Status do Pedido
- **PUT** `/api/orders/{id}/status`  
  Atualiza o status de um pedido (ex: `PENDING`, `PREPARING`, `DELIVERED`, `CANCELLED`).

### Listar Pedidos em Preparo
- **GET** `/api/orders/preparing`  
  Retorna todos os pedidos com status “EM PREPARO”.

---

## 🔔 Mensageria (RabbitMQ)

### Eventos Publicados
- **pedido.criado** — Publicado automaticamente quando um novo pedido é registrado.
- **pedido.finalizado** — Publicado quando o status de um pedido é alterado para “FINALIZADO”.

### Serviço de Notificação
- **notification-service**  
  Consome os eventos `pedido.criado` e `pedido.finalizado` e realiza o mock de envio de notificações (log em console).

---

## 🧩 GraphQL (Consultas Flexíveis)

### Endpoint GraphQL
- **POST** `/graphql`  
  Permite consultas personalizadas para histórico de pedidos, reservas e cardápio.

### Exemplos de Consultas
```graphql
query {
  ordersHistory(userId: 1, status: "DELIVERED") {
    id
    total
    status
  }

  reservationsFuture(userId: 1) {
    id
    date
    restaurant {
      name
    }
  }

  menuByRestaurant(id: 2) {
    name
    price
    available
  }
}
```

---

## 🧪 Testes Automatizados

### Testes Unitários
- Implementados com **JUnit 5** e **Mockito**.
- Cobrem os principais casos de uso (CRUD, autenticação, reservas, pedidos).

### Testes de Integração
- Executados com **Rest Assured** para validação dos endpoints e autenticação JWT.

### Cobertura de Código
- **JaCoCo**.
- Relatórios disponíveis em:  
  `target/site/jacoco/index.html`.

---

## 📦 Coleção do Postman
A coleção Postman permite testar todos os endpoints REST e GraphQL do sistema:

Acesso nossa Collection

https://www.postman.com/red-resonance-119741/workspace/collection-tech-challenge-r-m/collection/22747303-4e65fbcc-27b0-42fb-b97e-665e6ac1b293?action=share&source=copy-link&creator=22747303

---
