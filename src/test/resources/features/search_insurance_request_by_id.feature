# language: pt
# language: pt
Funcionalidade: Buscar solicitação de apólice de seguro por ID

  Cenário: Buscar apólice existente por ID
    Dado que existe uma apólice válida cadastrada
    Quando solicito a apólice pelo seu ID
    Então a resposta deve ter o status 200
    E o corpo da resposta deve conter os campos:
      | campo                      | valor                                      |
      | customerId                 | adc56d77-348c-4bf0-908f-22d402ee715c       |
      | productId                  | 1b2da7cc-b367-4196-8a78-9cfeec21f587       |
      | category                   | AUTO                                       |
      | salesChannel               | MOBILE                                     |
      | paymentMethod              | CREDIT_CARD                                |
      | status                     | VALIDATED                                  |
      | totalMonthlyPremiumAmount  | 75.25                                      |
      | insuredAmount              | 275000.5                                   |
    E o campo coverages deve conter:
      | cobertura                  | valor      |
      | Roubo                      | 100000.25  |
      | Perda Total                | 100000.25  |
      | Colisão com Terceiros      | 75000.0    |
    E o campo assistances deve conter:
      | assistência                |
      | Guincho até 250km          |
      | Troca de Óleo              |
      | Chaveiro 24h               |

  Cenário: Buscar apólice inexistente por ID
    Dado que não existe apólice cadastrada com o ID "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    Quando solicito a apólice pelo seu ID inexistente
    Então a resposta deve ter o status 404
    E a resposta deve conter a mensagem de erro:
      | mensagem                                                                                   |
      | Solicitação de apólice não encontrada para o id informado: 3fa85f64-5717-4562-b3fc-2c963f66afa6. |