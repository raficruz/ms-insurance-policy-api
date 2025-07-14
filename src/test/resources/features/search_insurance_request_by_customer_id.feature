# language: pt
Funcionalidade: Buscar solicitação de apólice de seguro pelo ID do cliente

  Cenário: Buscar apólices existentes por customerId
    Dado que existe uma ou mais apólices cadastradas para o customerId "adc56d77-348c-4bf0-908f-22d402ee715c"
    Quando solicito as apólices pelo customerId "adc56d77-348c-4bf0-908f-22d402ee715c"
    Então a resposta deve ter o status 200
    E o corpo da resposta deve conter uma lista de apólices

  Cenário: Buscar apólices por customerId inexistente
    Dado que não existe apólice cadastrada para o customerId "00000000-0000-0000-0000-000000000000"
    Quando solicito as apólices pelo customerId "00000000-0000-0000-0000-000000000000"
    Então a resposta deve ter o status 404
    E a resposta deve conter a mensagem de erro:
      | mensagem                                                                                                 |
      | Solicitação de apólice não encontrada para o customerId informado: 00000000-0000-0000-0000-000000000000. |