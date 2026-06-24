## Why

O OpenSpec foi adotado depois que uma parte relevante do SGC ja estava implementada. Esta mudanca cria uma baseline documental do estado atual para que proximas evolucoes tenham uma referencia de arquitetura, dominio, regras ja codificadas e lacunas conhecidas.

## What Changes

- Adiciona uma especificacao documental unica para o estado atual do projeto SGC.
- Documenta arquitetura, pacotes, entidades, relacionamentos, Services, Controllers, endpoints, Repositories, DTOs, Mappers, enums, excecoes e mensagens existentes.
- Registra regras de negocio ja codificadas no backend, sem alterar comportamento ou criar novas funcionalidades.
- Registra gaps, riscos e pendencias encontrados no codigo atual, incluindo modulos esperados que nao possuem implementacao funcional.
- Nao altera arquivos Java, migrations, testes, configuracoes runtime, contratos HTTP ou schema de banco.

## Capabilities

### New Capabilities
- `sgc-current-baseline`: documenta o estado atual implementado do SGC, incluindo arquitetura, superficie publica, regras existentes, gaps e recomendacoes para proximas specs.

### Modified Capabilities
- Nenhuma. As specs existentes (`burial-lifecycle`, `death-registration`, `deceased-lifecycle`, `grave-availability`) permanecem sem alteracao de requisitos.

## Impact

- Afeta apenas artefatos OpenSpec em `openspec/changes/sgc-current-baseline/`.
- Nao altera APIs, dependencias, entidades, Services, Controllers, Repositories, DTOs, Mappers ou testes.
- Serve como referencia documental para priorizacao de proximas specs e para reconciliar divergencias entre dominio esperado e codigo atual.
