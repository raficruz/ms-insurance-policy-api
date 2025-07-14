# language: pt
Funcionalidade: Criar solicitação de apólice de seguro

  Cenário: Criar solicitação com dados válidos
    Dado que o cliente deseja criar uma nova apólice
    Quando enviar uma requisição POST
    Então a resposta deve ter o status 201

  Cenário: Enviar solicitação com campos obrigatórios ausentes
    Dado que a requisição está com os campos obrigatorios nulos ou em branco
    Quando enviar uma requisição POST
    Então a resposta deve ter o status 400
    E a resposta deve conter as mensagens de validação:
      | field                    | message                  |
      | insuredAmount            | não deve ser nulo        |
      | category                 | não deve ser nulo        |
      | assistances              | não deve ser nulo        |
      | totalMonthlyPremiumAmount| não deve ser nulo        |
      | customerId               | não deve ser nulo        |
      | productId                | não deve ser nulo        |
      | paymentMethod            | não deve estar em branco |
      | salesChannel             | não deve estar em branco |
      | coverages                | não deve ser nulo        |

  Cenário: Enviar solicitação com valores inválidos para insuredAmount e totalMonthlyPremiumAmount
    Dado que o cliente deseja criar uma nova apólice
    E que o campo totalMonthlyPremiumAmount é 0 e o campo insuredAmount é -275000.5
    Quando enviar uma requisição POST
    Então a resposta deve ter o status 400
    E a resposta deve conter as mensagens de validação:
      | field                    | message                                 |
      | insuredAmount            | insuredAmount deve ser maior que zero.  |
      | totalMonthlyPremiumAmount| totalMonthlyPremiumAmount deve ser maior que zero. |

  Cenário: Enviar solicitação sem assistances e coverages
    Dado que o cliente deseja criar uma nova apólice sem assistances e coverages
    Quando enviar uma requisição POST
    Então a resposta deve ter o status 400
    E a resposta deve conter as mensagens de validação:
      | field      | message                                 |
      | assistances| assistances deve ter pelo menos 1 item(ns). |
      | coverages  | coverages deve ter pelo menos 1 item(ns).   |
