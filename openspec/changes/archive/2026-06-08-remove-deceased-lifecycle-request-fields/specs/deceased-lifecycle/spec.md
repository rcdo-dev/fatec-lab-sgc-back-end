## MODIFIED Requirements

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
