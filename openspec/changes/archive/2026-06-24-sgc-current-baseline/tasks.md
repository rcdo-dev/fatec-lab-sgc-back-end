## 1. Inventario Do Estado Atual

- [x] 1.1 Mapear stack, configuracoes e estrutura de pacotes do projeto.
- [x] 1.2 Levantar Entities, relacionamentos JPA e classes esqueleto.
- [x] 1.3 Levantar Controllers, endpoints, Services, Repositories, DTOs, Mappers, enums, excecoes e mensagens.
- [x] 1.4 Conferir specs OpenSpec existentes para evitar alterar requisitos ja documentados.

## 2. Baseline Documental

- [x] 2.1 Criar `proposal.md` para justificar a baseline documental.
- [x] 2.2 Criar `design.md` com decisoes, nao objetivos e riscos da documentacao.
- [x] 2.3 Criar `specs/sgc-current-baseline/spec.md` com requisitos documentais da baseline.
- [x] 2.4 Registrar regras implementadas separadamente de gaps, riscos e pendencias.

## 3. Validacao

- [x] 3.1 Executar `openspec status --change sgc-current-baseline`.
- [x] 3.2 Executar validacao OpenSpec disponivel para a mudanca.
- [x] 3.3 Executar `./mvnw test` como checagem de nao regressao.
- [x] 3.4 Revisar diff para confirmar que nenhum arquivo Java foi alterado.
