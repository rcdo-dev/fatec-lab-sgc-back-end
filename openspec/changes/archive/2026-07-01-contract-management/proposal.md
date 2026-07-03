## Why

O SGC ainda nao expunha um modulo funcional para registrar contratos que representem o titulo de posse de sepulturas. O codigo ja possuia entidades parciais de contrato e titular, mas sem Controller, Service, Repository, DTOs, MapStruct, regras de negocio ou endpoints publicos.

## What Changes

- Implementa o gerenciamento de contratos com cadastro, consulta, atualizacao e inativacao logica.
- Permite criar contrato criando uma nova sepultura ou vinculando uma sepultura existente.
- Garante que contratos sejam vinculados a uma unica sepultura e que uma sepultura nao possua mais de um contrato aberto simultaneamente.
- Aplica as regras de sepultura criada por contrato: ativa, `MAUSOLEUM`, `PERPETUAL` e `AVAILABLE`.
- Adiciona transicoes de estado de contrato e atualizacao automatica de `ACTIVE` vencido para `OVERDUE`.
- Mantem historico de contratos encerrados sem exclusao fisica.

## Capabilities

### New Capabilities
- `contract-management`: registra, consulta, atualiza, inativa e gerencia estados de contratos vinculados a sepulturas.

### Modified Capabilities
- `sgc-current-baseline`: a lacuna de Contract deixa de representar ausencia funcional total e passa a ter implementacao inicial.

## Impact

- Adiciona endpoints `/contracts`.
- Altera o relacionamento entre `GraveEntity` e `ContractEntity` para permitir historico de contratos por sepultura.
- Adiciona repositories, DTOs, mapper, service, controller, mensagens e testes de contrato.
- Adiciona `INACTIVE` ao enum `ContractStatus` e habilita scheduling na aplicacao.
