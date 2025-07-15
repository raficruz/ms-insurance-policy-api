# Insurance Policy Service

Microsserviço responsável pela gestão do ciclo de vida de solicitações de apólices de seguros da ACME.

## Repositório do Projeto

O código-fonte completo está disponível em:

[https://github.com/raficruz/ms-insurance-policy-service-api](https://github.com/raficruz/ms-insurance-policy-api)

## Sobre o Serviço de Solicitações de Seguro

O serviço de solicitações de seguro gerencia todo o fluxo de uma solicitação de apólice, desde a criação até a aprovação, rejeição ou cancelamento. O ciclo de vida da aplicação é o seguinte:

1. **Cliente realiza nova solicitação**
2. **FrontEnd UI** realiza integração REST com o serviço de solicitações
3. **Serviço de solicitações** persiste a solicitação, gera o ID e publica atualização de estado `RECEBIDO` via mensageria (Kafka)
4. **Serviço de solicitações** realiza integração REST com o serviço de fraudes (mock) e aplica as regras de negócio
5. **Serviço de solicitações** atualiza o estado para `VALIDADO` ou `REJEITADO` e publica o evento via mensageria
6. **Serviços de pagamento e subscrição** processam a solicitação
7. **Serviços de pagamento e subscrição** publicam eventos do resultado do processamento
8. **Serviço de solicitações** consome eventos de pagamentos e subscrição, atualiza o status da solicitação para `APROVADO` ou `REJEITADO` e publica eventos na mensageria
9. **Serviço de notificações** informa o cliente sobre o status da solicitação
10. **Cliente** pode consultar ou cancelar a solicitação a qualquer momento

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- Spring Actuator
- Apache Kafka
- Kafka UI (interface para visualizar e produzir eventos Kafka)
- PostgreSQL
- Docker e Docker Compose
- Wiremock (mock de serviços externos, com resposta fixa via `fraud-analysis-mock.json`)
- OpenAPI (Swagger)
- Lombok
- JUnit 5 & Cucumber (Testes)
- Jacoco (Cobertura de testes)

## Requisitos

- Docker
- Docker Compose
- Gradle (para rodar/testar localmente sem Docker)

## Subindo o projeto com Docker

Para construir e subir todos os containers (aplicação, banco, Kafka, Kafka UI e Wiremock):

```bash
docker-compose up --build
```

- Aplicação: http://localhost:8080/swagger-ui.html
- Kafka UI: http://localhost:8081 Interface web para monitorar tópicos, visualizar e produzir eventos de pagamento e subscrição no Kafka.
- Wiremock: http://localhost:8089 Mock server já configurado com o arquivo mappings/fraud-analysis-mock.json para simular sempre a mesma resposta da análise de fraude.
- PostgreSQL: localhost:5432 (usuário: postgres, senha: postgres)

## Serviços Auxiliares
- Kafka UI: Interface para monitorar e produzir eventos nos tópicos Kafka, facilitando testes de integração.
- Wiremock: Mock server para simular integrações externas (ex: análise de fraude). O arquivo mappings/fraud-analysis-mock.json já está embutido no projeto e retorna sempre a mesma resposta para facilitar testes e desenvolvimento.
  Documentação da API
  A documentação interativa está disponível via Swagger em:
```bash
http://localhost:8080/swagger-ui.html
```

## Acessando o Kafka UI e Tópicos Disponíveis

O Kafka UI é uma interface web para monitorar, visualizar e produzir eventos nos tópicos Kafka utilizados pelo serviço.

- **Acesse o Kafka UI:**  
  [http://localhost:8081](http://localhost:8081)

- **Tópicos principais disponíveis:**
    - `insurance-requests-status` — Publicação de atualizações de status das solicitações de seguro.
    - `payment-events` — Eventos de processamento de pagamento.
    - `subscription-events` — Eventos de subscrição de apólices.

Utilize o Kafka UI para inspecionar mensagens, criar novos eventos para testes e acompanhar o fluxo de dados entre os microsserviços.

## Estrutura do Projeto
- br.com.acme.insurance.adapter.controller: Controllers e APIs expostas
- br.com.acme.insurance.application.usecase: Casos de uso
- br.com.acme.insurance.domain: Modelos de domínio e repositórios
- br.com.acme.insurance.infrastructure: Integrações com banco de dados (JPA)
- br.com.acme.insurance.policy: Policies e regras de negócio
- br.com.acme.insurance.consumer: Consumers de eventos Kafka

### Criar uma solicitação de apólice

**POST** `/v1/insurance-requests`

**Payload de entrada:**
```json
{
  "customer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "category": "AUTO",
  "salesChannel": "MOBILE",
  "paymentMethod": "CREDIT_CARD",
  "total_monthly_premium_amount": 75.25,
  "insured_amount": 10000.0,
  "coverages": {
    "Roubo": 100000.25,
    "Perda Total": 100000.25,
    "Colisão com Terceiros": 75000
  },
  "assistances": [
    "Guincho até 250km",
    "Troca de Óleo",
    "Chaveiro 24h"
  ]
}
```
**cURL:**
``` bash
curl --location 'http://localhost:8080/v1/insurance-requests' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--data '{
  "customer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "category": "AUTO",
  "salesChannel": "MOBILE",
  "paymentMethod": "CREDIT_CARD",
  "total_monthly_premium_amount": 75.25,
  "insured_amount": 10000.0,
  "coverages": {
    "Roubo": 100000.25,
    "Perda Total": 100000.25,
    "Colisão com Terceiros": 75000
  },
  "assistances": [
    "Guincho até 250km",
    "Troca de Óleo",
    "Chaveiro 24h"
  ]
}'
```

<hr></hr>

### Consultar solicitação por ID
**GET** `/v1/insurance-requests/{id}`

**cURL:**
``` bash
curl --location 'http://localhost:8080/v1/insurance-requests/2e9d6f99-eeab-4d63-9f1a-00c693a7f516'
```


<hr></hr>

### Consultar solicitação por ID do Cliente
**GET** `/v1/insurance-requests?customer_id={customerId}`

**cURL:**
``` bash
curl http://localhost:8080/v1/insurance-requests?customer_id=7c5ff6a1-8fc5-4c22-ae0f-2f9c4f9d5f56
```


<hr></hr>

### Cancelar solicitação
**DELETE** `/v1/insurance-requests/{id}`

**cURL:**
``` bash
curl -X DELETE http://localhost:8080/v1/insurance-requests/123e4567-e89b-12d3-a456-426614174000
```

## Rodando os Testes e Visualizando Cobertura

Para executar os testes automatizados e gerar o relatório de cobertura de código, siga os passos abaixo:

### Pré-requisitos

- **Banco de dados e dependências externas não são necessários** para rodar os testes unitários, pois o projeto utiliza H2 em memória e mocks.
- Certifique-se de que **nenhum serviço externo está usando as portas do projeto** para evitar conflitos.

### Executando os testes

Utilize o Gradle (localmente ou via Docker):

```bash
./gradlew test
```

### Gerando o relatório de cobertura
Após rodar os testes, gere o relatório Jacoco:
```bash
./gradlew jacocoTestReport
```

O relatório HTML estará disponível em:
```
build/reports/jacoco/test/html/index.html
```

Abra este arquivo no navegador para visualizar a cobertura de código dos testes.
#### **Observações**
- Os testes podem ser executados diretamente pelo Gradle Wrapper, sem necessidade de containers Docker.
- Para rodar testes de integração que dependam de Kafka ou outros serviços, certifique-se de que o ambiente Docker está ativo (docker-compose up).

## Contato

Rafael Cruz - raficruz@gmail.com