# language: pt
Funcionalidade: Cancelar e verificar solicitação de apólice de seguro

  Cenário: Criar, cancelar e verificar status da apólice
    Dado que o cliente criou uma nova apólice
    Quando o cliente solicita o cancelamento da apólice
    E o cliente busca a apólice pelo seu ID
    Então a resposta da busca deve ter o status 200
    E o corpo da resposta deve indicar status cancelado

  Cenário: Cancelar apólice inexistente
    Dado que não existe apólice cadastrada com o ID "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    Quando solicito o cancelamento da apólice pelo seu ID
    Então a resposta deve ter o status 404
    E a resposta deve conter a mensagem de erro:
      | mensagem                                                                                   |
      | Solicitação de apólice não encontrada para o id informado: 3fa85f64-5717-4562-b3fc-2c963f66afa6. |

  Cenário: Cancelar apólice já cancelada
    Dado que existe uma apólice válida cadastrada
    Quando solicito o cancelamento da apólice pelo seu ID
    Então a resposta deve ter o status 204

    Quando solicito o cancelamento da apólice pelo seu ID
    Então a resposta deve ter o status 400
    E a resposta deve conter a mensagem de erro:
      | mensagem                                      |
      | A apólice {id} já está cancelada.             |