## 1. OpenSpec

- [x] 1.1 Criar `proposal.md` para o modulo de contratos.
- [x] 1.2 Criar `design.md` com decisoes de modelagem, transicoes e riscos.
- [x] 1.3 Criar delta spec `contract-management` com requisitos e cenarios.

## 2. Modelo E Persistencia

- [x] 2.1 Completar `ContractStatus` com `INACTIVE`.
- [x] 2.2 Ajustar `ContractEntity.status` para enum persistido como string.
- [x] 2.3 Alterar relacionamento Grave/Contract para permitir historico por sepultura.
- [x] 2.4 Adicionar repositories de contrato e titular.

## 3. API E Regras De Dominio

- [x] 3.1 Criar DTOs de request/response para contrato, titular e sepultura criada pelo contrato.
- [x] 3.2 Criar `ContractMapper` MapStruct.
- [x] 3.3 Criar `ContractService` com validacoes de numero unico, periodo, quadra, sepultura, titular e contrato aberto.
- [x] 3.4 Criar endpoints `/contracts` para registrar, consultar, atualizar, transicionar e inativar contrato.
- [x] 3.5 Adicionar scheduler de vencimento automatico para `OVERDUE`.
- [x] 3.6 Adicionar mensagens padronizadas de contrato.

## 4. Testes E Validacao

- [x] 4.1 Cobrir criacao com sepultura nova e vinculo com sepultura existente.
- [x] 4.2 Cobrir rejeicoes de numero duplicado, quadra inexistente, periodo invalido, sepultura incompatível e sepultura com contrato aberto.
- [x] 4.3 Cobrir transicoes de estado e bloqueio de reativacao de contratos encerrados.
- [x] 4.4 Cobrir endpoints principais de controller.
- [x] 4.5 Executar `./mvnw clean test`.
