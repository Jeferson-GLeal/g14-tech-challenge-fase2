# g14-tech-challenge-fase2

# Foodlink API - Guia de Configuracao e Execucao

Este repositorio contem a API do projeto Foodlink para o Tech Challenge - Fase 2. A aplicacao foi desenvolvida em Java com Spring Boot, organizada com separacao entre dominio, casos de uso, infraestrutura e interfaces HTTP.

O objetivo desta fase e disponibilizar cadastros e consultas para:

- Tipos de usuario
- Usuarios com endereco
- Restaurantes com dono, endereco e horarios de funcionamento
- Itens de cardapio por restaurante

Tecnologias principais:

- Java 21
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- Flyway
- PostgreSQL
- Docker / Docker Compose
- Springdoc OpenAPI / Swagger UI
- Maven Wrapper

---

# Checklist rapido

- [x] Ter Docker e Docker Compose instalados
- [x] Subir PostgreSQL e API com Docker Compose
- [x] Executar migrations com Flyway automaticamente
- [x] Acessar Swagger UI
- [x] Importar collection do Postman
- [x] Executar testes automatizados

---

# Pre-requisitos

Antes de iniciar, verifique se possui:

- Docker
- Docker Compose
- Java 21, caso execute localmente fora do Docker

Verifique com:

```bash
docker version
docker compose version
java -version
```

---

# Como executar via Docker Compose

No diretorio raiz do projeto `foodlink-api`, execute:

```bash
docker compose up --build
```

O Docker Compose sobe:

- PostgreSQL 15
- Foodlink API

Configuracao padrao do banco no Docker:

- Database: `db_foodlink_v2`
- Usuario: `postgres`
- Senha: `postgres`
- Porta: `5432`

A API ficara disponivel em:

```text
http://localhost:8080
```

Para parar os containers:

```bash
docker compose down
```

Para recriar o banco do zero, removendo o volume:

```bash
docker compose down -v
docker compose up --build
```

---

# Como executar localmente

Para executar a aplicacao fora do Docker, mantenha um PostgreSQL local ativo com:

- Host: `localhost`
- Porta: `5432`
- Database: `db_foodlink_v2`
- Usuario: `postgres`
- Senha: `postgres`

Depois execute:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

O profile `local` usa o arquivo:

```text
src/main/resources/application-local.yaml
```

O profile `docker` usa:

```text
src/main/resources/application-docker.yaml
```

---

# Banco de dados e migrations

As migrations ficam em:

```text
src/main/resources/db/migration
```

Migrations atuais:

- `V1__criar_tabelas.sql` - cria as tabelas principais
- `V2__adicionar_codigo_tipo_usuario.sql` - adiciona codigo em tipos de usuario
- `V3__popular_dados_teste.sql` - popula dados para teste

O Flyway roda automaticamente ao iniciar a aplicacao.

---

# Swagger

Com a aplicacao rodando, acesse:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

# Endpoints principais

Base URL local:

```text
http://localhost:8080
```

Recursos disponiveis:

- `GET /api/user-types`
- `POST /api/user-types`
- `GET /api/user-types/{id}`
- `PUT /api/user-types/{id}`
- `DELETE /api/user-types/{id}`
- `GET /api/users`
- `POST /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `PATCH /api/users/{id}/password`
- `DELETE /api/users/{id}`
- `GET /restaurants`
- `POST /restaurants`
- `GET /restaurants/{id}`
- `PUT /restaurants/{id}`
- `DELETE /restaurants/{id}`
- `GET /api/menu-items/{restaurantId}`
- `POST /api/menu-items/{restaurantId}`
- `GET /api/menu-items/{restaurantId}/{id}`
- `PUT /api/menu-items/{restaurantId}/{id}`
- `DELETE /api/menu-items/{restaurantId}/{id}`

---

# Testes

Para rodar os testes automatizados:

```bash
./mvnw test
```

Observacao: o teste de contexto da aplicacao usa o profile padrao `local`, entao precisa de PostgreSQL local disponivel em `localhost:5432` ou do Docker Compose rodando.

Para rodar somente testes unitarios especificos:

```bash
./mvnw -Dtest=UserUseCaseTest test
```

---

# Postman Collection

Este repositorio inclui uma collection para facilitar os testes da API.

Arquivo:

```text
foodlink-api.postman_collection.json
```

Para importar:

1. Abra o Postman
2. Clique em `File -> Import`
3. Selecione o arquivo `foodlink-api.postman_collection.json`

Variaveis incluidas na collection:

- `baseUrl` - URL base da API, padrao `http://localhost:8080`
- `userTypeDonoId`
- `userTypeClienteId`
- `ownerUserId`
- `clientUserId`
- `restaurantId`
- `sushiRestaurantId`
- `burgerRestaurantId`
- `brasilRestaurantId`
- `menuItemId`
- `createdUserId`
- `createdUserTypeId`
- `createdRestaurantId`
- `createdMenuItemId`

---

# Dados de teste

A migration `V3__popular_dados_teste.sql` cria dados iniciais para facilitar os testes.

IDs uteis:

- Tipo dono de restaurante: `11111111-1111-1111-1111-111111111111`
- Tipo cliente: `22222222-2222-2222-2222-222222222222`
- Restaurantes de exemplo:
  - `dddddddd-dddd-dddd-dddd-ddddddddddd1`
  - `dddddddd-dddd-dddd-dddd-ddddddddddd2`
  - `dddddddd-dddd-dddd-dddd-ddddddddddd3`

---

# Estrutura do projeto

Resumo dos principais pacotes:

```text
src/main/java/com/fiap/foodlink_api
├── application
│   └── usecase
├── domain
│   ├── entity
│   ├── exception
│   └── gateway
├── infrastructure
│   ├── config
│   └── persistence
└── interfaces
    └── controller
```

Responsabilidades:

- `domain` - regras de dominio, entidades, exceptions e contratos de gateway
- `application` - casos de uso da aplicacao
- `infrastructure` - JPA, repositories, gateways de banco e configuracoes
- `interfaces` - controllers HTTP, DTOs, mappers e handlers

---

# Comandos uteis

Compilar sem testes:

```bash
./mvnw -DskipTests compile
```

Gerar pacote:

```bash
./mvnw clean package
```

Subir containers:

```bash
docker compose up --build
```

Parar containers:

```bash
docker compose down
```

Recriar banco:

```bash
docker compose down -v
docker compose up --build
```
