## Context

Contratos fazem parte do dominio de cemiterio porque representam o titulo de posse sobre uma sepultura. O projeto ja organiza esse dominio em camadas Controller, Service, Repository, Entity, DTO e Mapper, usando MapStruct, Bean Validation, excecoes padronizadas e mensagens em `messages.properties`.

## Goals / Non-Goals

**Goals:**
- Implementar CRUD funcional de contrato, exceto exclusao fisica.
- Criar ou vincular sepultura durante a criacao do contrato.
- Validar todas as regras de dominio na camada Service.
- Manter historico de contratos encerrados.
- Atualizar contratos vencidos de `ACTIVE` para `OVERDUE` automaticamente.

**Non-Goals:**
- Criar CRUD publico de titular de contrato.
- Criar migrations versionadas, pois o projeto atualmente usa `ddl-auto=update`.
- Alterar fluxos existentes de sepultura, sepultamento, obito, falecido ou velorio.
- Implementar auditoria ou relatorios de contrato.

## Decisions

- O endpoint principal sera `/contracts`, com `POST` aceitando `blockId`, dados do titular e exatamente uma das opcoes: `graveId` para sepultura existente ou `newGrave` para criacao de sepultura.
- Titular sera embutido no request do contrato e reutilizado por CPF, sem superficie HTTP propria nesta mudanca.
- Contrato usara `ContractStatus` com `ACTIVE`, `SUSPENDED`, `OVERDUE`, `EXPIRED` e `INACTIVE`.
- Contratos abertos sao `ACTIVE`, `SUSPENDED` e `OVERDUE`; `EXPIRED` e `INACTIVE` sao encerrados e permanecem como historico.
- `GraveEntity` tera colecao de contratos, enquanto cada `ContractEntity` referencia uma unica sepultura.
- O vencimento automatico sera um metodo `@Scheduled` diario que atualiza contratos `ACTIVE` com `endDate` anterior ao dia atual para `OVERDUE`.

## Risks / Trade-offs

- [Risk] A unicidade de contrato aberto por sepultura e garantida no Service, nao por indice parcial de banco. -> Mitigacao: repository dedicado e testes cobrindo a regra.
- [Risk] `ddl-auto=update` nao expressa todas as restricoes fisicas possiveis. -> Mitigacao: manter consistencia com o padrao atual do projeto e validar regras no Service.
- [Risk] Springdoc atual tem incompatibilidade no contexto de testes com Spring Boot 4. -> Mitigacao: desabilitar Springdoc apenas em `src/test/resources/application.properties`.
