## ADDED Requirements

### Requirement: Capacidade da Grave controla disponibilidade para Burial
O sistema SHALL usar `bodyCapacity` e a contagem de Burials ativos para determinar se uma Grave pode receber outro Burial.

#### Scenario: Burial é criado abaixo da capacidade da Grave
- **WHEN** uma solicitação de Burial tem como alvo uma Grave cuja contagem de Burials ativos é menor que `bodyCapacity`
- **THEN** o sistema permite o Burial quando todas as demais regras de sepultamento forem atendidas

#### Scenario: Burial é criado no limite da capacidade da Grave
- **WHEN** uma solicitação de Burial tem como alvo uma Grave cuja contagem de Burials ativos é igual a `bodyCapacity`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

### Requirement: Status da Grave acompanha a contagem de Burials ativos
O sistema SHALL manter o status da Grave alinhado com a contagem de Burials ativos.

#### Scenario: Grave recebe o primeiro Burial ativo
- **WHEN** um Burial é criado em uma Grave com zero Burials ativos
- **THEN** o status da Grave passa a ser `OCCUPIED`

#### Scenario: Contagem de Burials ativos da Grave retorna a zero
- **WHEN** o último Burial ativo de uma Grave é cancelado
- **THEN** o status da Grave passa a ser `AVAILABLE`

### Requirement: Graves bloqueadas não podem receber Burials
O sistema MUST rejeitar a criação ou movimentação de Burial para uma Grave bloqueada.

#### Scenario: Burial tem como alvo uma Grave bloqueada
- **WHEN** uma solicitação de Burial tem como alvo uma Grave com `blocked=true`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

### Requirement: Graves inativas ou em manutenção não podem receber Burials
O sistema MUST rejeitar a criação ou movimentação de Burial para Graves inativas ou em manutenção.

#### Scenario: Burial tem como alvo uma Grave inativa
- **WHEN** uma solicitação de Burial tem como alvo uma Grave com `active=false`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

#### Scenario: Burial tem como alvo uma Grave em manutenção
- **WHEN** uma solicitação de Burial tem como alvo uma Grave com status `MAINTENANCE`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio
