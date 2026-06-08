## 1. Contrato De Request

- [x] 1.1 Remover `deceasedStatus`, `archived` e `archivedAt` de `DeceasedIdentifiedRequestDTO`.
- [x] 1.2 Remover `deceasedStatus`, `archived` e `archivedAt` de `DeceasedPetRequestDTO`.
- [x] 1.3 Remover `deceasedStatus`, `archived` e `archivedAt` de `DeceasedUnidentifiedRequestDTO`.
- [x] 1.4 Remover imports e anotações que ficaram sem uso.
- [x] 1.5 Explicitar nos Mappers que `archived` e `archivedAt` não são populados a partir dos requests.

## 2. Testes E Documentação

- [x] 2.1 Atualizar helpers de testes para construir os request DTOs sem campos de ciclo de vida.
- [x] 2.2 Atualizar a spec principal de `deceased-lifecycle` para registrar que requests de falecido não expõem campos de controle de ciclo de vida.
- [x] 2.3 Executar `./mvnw test`.
- [x] 2.4 Executar `./mvnw -DskipTests package`.
