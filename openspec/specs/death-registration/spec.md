# death-registration Specification

## Purpose
TBD - created by archiving change life-cycle-correction. Update Purpose after archive.
## Requirements
### Requirement: Um Death por falecido
O sistema SHALL permitir no máximo um registro de Death para cada falecido.

#### Scenario: Death é criado para falecido que já possui Death
- **WHEN** uma solicitação de cadastro de Death referencia um falecido que já possui registro de Death
- **THEN** o sistema rejeita a solicitação como conflito

### Requirement: Update de Death preserva a associação com o falecido
O sistema MUST manter um registro de Death associado ao mesmo falecido após sua criação.

#### Scenario: Detalhes de Death são atualizados
- **WHEN** uma solicitação de update altera os detalhes de um Death existente
- **THEN** o sistema atualiza os detalhes de Death
- **AND** o falecido associado permanece inalterado
- **AND** o update não gera conflito com a própria associação do registro de Death

#### Scenario: Update de Death solicita outro falecido
- **WHEN** uma solicitação de update de Death inclui um id de falecido diferente
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

### Requirement: Death é obrigatório antes de Burial
O sistema MUST rejeitar a criação de Burial para falecido que não possui registro de Death.

#### Scenario: Burial é criado sem Death cadastrado
- **WHEN** uma solicitação de criação de Burial referencia um falecido sem registro de Death
- **THEN** o sistema rejeita a solicitação como violação de regra de negócio

