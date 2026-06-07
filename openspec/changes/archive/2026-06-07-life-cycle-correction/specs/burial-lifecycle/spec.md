## ADDED Requirements

### Requirement: Criação de Burial altera o estado de ciclo de vida
O sistema SHALL criar registros de Burial como `IN_PROGRESS` e atualizar o estado de ciclo de vida relacionado.

#### Scenario: Burial é criado para falecido e sepultura elegíveis
- **WHEN** um Burial é criado para falecido `ACTIVE`, não arquivado, com Death cadastrado e sepultura com capacidade disponível
- **THEN** o status do Burial é `IN_PROGRESS`
- **AND** o status do falecido passa a ser `BURIED`
- **AND** o status da sepultura passa a ser `OCCUPIED`

### Requirement: Falecido possui no máximo um Burial ativo
O sistema MUST rejeitar um novo Burial ativo para falecido que já possui Burial em `IN_PROGRESS`.

#### Scenario: Segundo Burial ativo é criado para o mesmo falecido
- **WHEN** uma solicitação de criação de Burial referencia um falecido que já possui Burial em `IN_PROGRESS`
- **THEN** o sistema rejeita a solicitação como conflito

### Requirement: Cancelamento de Burial libera estado de ciclo de vida
O sistema SHALL cancelar apenas Burials ativos e liberar o estado de ciclo de vida relacionado.

#### Scenario: Burial ativo é cancelado
- **WHEN** uma solicitação de cancelamento tem como alvo um Burial em `IN_PROGRESS`
- **THEN** o status do Burial passa a ser `CANCELLED`
- **AND** o status do falecido passa a ser `ACTIVE`
- **AND** a capacidade da sepultura consumida por esse Burial é liberada

#### Scenario: Burial cancelado é cancelado novamente
- **WHEN** uma solicitação de cancelamento tem como alvo um Burial que não está em `IN_PROGRESS`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

### Requirement: Update de Burial é restrito a Burials ativos
O sistema MUST permitir updates de Burial apenas enquanto o Burial está em `IN_PROGRESS`.

#### Scenario: Burial cancelado é atualizado
- **WHEN** uma solicitação de update tem como alvo um Burial `CANCELLED`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio
