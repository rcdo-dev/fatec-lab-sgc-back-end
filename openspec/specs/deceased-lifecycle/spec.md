# deceased-lifecycle Specification

## Purpose
TBD - created by archiving change life-cycle-correction. Update Purpose after archive.
## Requirements
### Requirement: Estado de ciclo de vida do falecido é controlado pelo sistema
O sistema SHALL criar todo registro de falecido com status `ACTIVE`, `archived=false` e `archivedAt=null`, sem aceitar campos de controle de ciclo de vida nos DTOs de request de falecido.

#### Scenario: Falecido identificado é criado com estado padrão de ciclo de vida
- **WHEN** um falecido identificado é criado por meio de um DTO de request sem campos de ciclo de vida
- **THEN** o status persistido do falecido é `ACTIVE`
- **AND** `archived` é `false`
- **AND** `archivedAt` é `null`

#### Scenario: Falecido pet é criado com estado padrão de ciclo de vida
- **WHEN** um falecido pet é criado por meio de um DTO de request sem campos de ciclo de vida
- **THEN** o status persistido do falecido é `ACTIVE`
- **AND** `archived` é `false`
- **AND** `archivedAt` é `null`

### Requirement: Responses de falecido expõem o estado de ciclo de vida persistido
O sistema SHALL incluir os valores persistidos de `deceasedStatus`, `archived` e `archivedAt` nos DTOs de response de falecido.

#### Scenario: Falecido arquivado é retornado
- **WHEN** um registro de falecido arquivado é consultado
- **THEN** a response contém `deceasedStatus=ARCHIVED`
- **AND** a response contém `archived=true`
- **AND** a response contém o timestamp de arquivamento persistido

### Requirement: Arquivamento é bloqueado para falecido com sepultamento ativo
O sistema MUST rejeitar solicitações de arquivamento para falecido que possui sepultamento ativo.

#### Scenario: Falecido com sepultamento ativo é arquivado
- **WHEN** uma solicitação de arquivamento é feita para um falecido com Burial em `IN_PROGRESS`
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio
- **AND** o estado de ciclo de vida do falecido permanece inalterado

### Requirement: Falecido arquivado não pode sofrer mutações de ciclo de vida
O sistema MUST rejeitar criação de Death, criação de Burial e operações de update de falecido para registros de falecido arquivados.

#### Scenario: Death é criado para falecido arquivado
- **WHEN** uma solicitação de cadastro de Death referencia um falecido arquivado
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

#### Scenario: Burial é criado para falecido arquivado
- **WHEN** uma solicitação de Burial referencia um falecido arquivado
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

#### Scenario: Falecido arquivado é atualizado
- **WHEN** uma solicitação de update tem como alvo um falecido arquivado
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

### Requirement: Exclusão de Declarant é protegida por falecidos relacionados
O sistema MUST rejeitar a exclusão de um Declarant enquanto qualquer registro de falecido referenciar esse Declarant.

#### Scenario: Declarant com registros de falecido é excluído
- **WHEN** uma solicitação de delete tem como alvo um Declarant que possui registros de falecido relacionados
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio
- **AND** o Declarant e os registros de falecido relacionados permanecem persistidos
