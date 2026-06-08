## Context

Os DTOs de request `DeceasedIdentifiedRequestDTO`, `DeceasedPetRequestDTO` e `DeceasedUnidentifiedRequestDTO` expunham `deceasedStatus`, `archived` e `archivedAt`. Esses campos representam estado interno persistido, mas a regra vigente de `deceased-lifecycle` define que o ciclo de vida do falecido é controlado pelo sistema.

Os Services já atribuem o estado inicial na criação e preservam as regras de transição. Portanto, a presença desses campos no request não é necessária para a implementação e gera ambiguidade no contrato público.

## Goals / Non-Goals

**Goals:**

- Remover controle de ciclo de vida dos DTOs de entrada de falecido.
- Manter Services como fonte única de `ACTIVE`, `archived=false` e `archivedAt=null` na criação.
- Manter DTOs de response expondo o estado persistido para consulta pelos consumidores da API.
- Atualizar testes e spec para refletir o contrato correto.

**Non-Goals:**

- Não alterar entidades, schema de banco, migrations ou repositories.
- Não alterar regras de arquivamento, óbito, sepultamento ou transições de ciclo de vida.
- Não remover `deceasedStatus`, `archived` ou `archivedAt` das responses.

## Decisions

- Os campos de ciclo de vida serão removidos dos request DTOs em vez de apenas ignorados pelos Services, porque o contrato de entrada deve comunicar que clientes não controlam esse estado.
- As responses permanecem inalteradas, porque consumidores precisam observar o estado de ciclo de vida persistido.
- A documentação arquivada não será alterada, preservando histórico. A spec principal e a delta spec desta mudança registram a decisão atual.

## Risks / Trade-offs

- [Risk] Clientes que montam requests usando os campos removidos precisarão ajustar seus payloads ou SDKs gerados a partir da documentação atualizada. → Mitigation: manter responses inalteradas e registrar explicitamente a mudança de contrato na OpenSpec.
- [Risk] Clientes que esperavam controlar ciclo de vida por payload podem interpretar a remoção como regressão. → Mitigation: documentar que esses campos sempre pertenceram à regra de domínio dos Services e manter consulta do estado pelas responses.
