## Por Que

Os DTOs de request de falecido ainda expõem `deceasedStatus`, `archived` e `archivedAt`, embora o ciclo de vida do falecido seja controlado pelos Services. Essa exposição cria ruído no contrato público, sugere que o cliente pode controlar estado interno e contradiz a decisão de domínio de manter o estado de ciclo de vida sob responsabilidade do sistema.

## O Que Muda

- Remover campos de ciclo de vida dos DTOs de request de falecido identificado, falecido não identificado e pet falecido.
- Manter os Services responsáveis por definir `ACTIVE`, `archived=false` e `archivedAt=null` na criação.
- Manter os DTOs de response expondo os valores persistidos de `deceasedStatus`, `archived` e `archivedAt`.
- Atualizar testes e documentação OpenSpec para refletir que requests de falecido não aceitam controle de ciclo de vida.

## Impacto

- Afeta apenas o contrato de entrada dos endpoints de falecido e os testes que constroem esses request DTOs.
- Não altera entidades, banco de dados, regras de Service, Repositories ou DTOs de response.
- Reforça a rastreabilidade da decisão já validada em `deceased-lifecycle`: ciclo de vida é controlado pelo sistema.
