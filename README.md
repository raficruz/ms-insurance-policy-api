# Insurance Policy Service

Microsserviço responsável pela gestão do ciclo de vida de solicitações de apólices de seguros da ACME.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Apache Kafka
- PostgreSQL
- Docker e Docker Compose
- OpenAPI (Swagger)
- OpenTelemetry (removido na versão simplificada)
- Lombok

## Requisitos

- Docker
- Docker Compose

## Subindo o projeto com Docker

Para construir e subir os containers:

```bash
docker-compose up --build
```

> A aplicação estará disponível em: `http://localhost:8080/swagger-ui.html`

## Documentação da API

A documentação interativa está disponível via Swagger em:

```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

- `br.com.acme.insurance.adapter.controller`: Controllers e APIs expostas
- `br.com.acme.insurance.application.usecase`: Casos de uso
- `br.com.acme.insurance.domain`: Modelos de domínio e repositórios
- `br.com.acme.insurance.infrastructure`: Integrações com banco de dados (JPA)

## Variáveis de Ambiente Importantes

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`

## Exemplo de Chamadas via Postman / cURL

### Criar uma solicitação de apólice (POST)

**POST** `/v1/insurance-requests`

```json
{
  "customerId": "7c5ff6a1-8fc5-4c22-ae0f-2f9c4f9d5f56",
  "productId": "3d734ab5-bf35-4782-b010-e183b6c06d2f",
  "category": "AUTO",
  "salesChannel": "website",
  "paymentMethod": "credit_card",
  "totalMonthlyPremiumAmount": 120.50,
  "insuredAmount": 50000.00,
  "coverages": ["ROUBO", "INCENDIO"],
  "assistances": ["GUINCHO", "CHAVEIRO"]
}
```

```bash
curl -X POST http://localhost:8080/v1/insurance-requests   -H "Content-Type: application/json"   -d '{
    "customerId": "7c5ff6a1-8fc5-4c22-ae0f-2f9c4f9d5f56",
    "productId": "3d734ab5-bf35-4782-b010-e183b6c06d2f",
    "category": "AUTO",
    "salesChannel": "website",
    "paymentMethod": "credit_card",
    "totalMonthlyPremiumAmount": 120.50,
    "insuredAmount": 50000.00,
    "coverages": ["ROUBO", "INCENDIO"],
    "assistances": ["GUINCHO", "CHAVEIRO"]
  }'
```

### Consultar solicitação por ID (GET)

**GET** `/v1/insurance-requests/{id}`

```bash
curl http://localhost:8080/v1/insurance-requests/123e4567-e89b-12d3-a456-426614174000
```

### Consultar solicitações por ID de cliente (GET)

**GET** `/v1/insurance-requests?customer_id=...`

```bash
curl http://localhost:8080/v1/insurance-requests?customer_id=7c5ff6a1-8fc5-4c22-ae0f-2f9c4f9d5f56
```

### Cancelar solicitação (DELETE)

**DELETE** `/v1/insurance-requests/{id}`

```bash
curl -X DELETE http://localhost:8080/v1/insurance-requests/123e4567-e89b-12d3-a456-426614174000
```

## Observações

- As chamadas devem respeitar o formato UUID válido.
- As categorias e estados seguem os enums definidos no domínio.

## Contato

Rafael Cruz