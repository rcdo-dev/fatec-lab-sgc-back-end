## Contexto

A implementação atual possui uma estrutura CRUD funcional, mas o comportamento de ciclo de vida está distribuído entre Services e não é totalmente consistente. O estado de Deceased é parcialmente aceito nos DTOs de request, parcialmente ignorado pelos Services e parcialmente omitido nos Mappers de response. Burial já atualiza o status de Deceased e Grave, mas trata a capacidade da sepultura como binária e não exige um óbito cadastrado.

Esta mudança estabiliza o domínio existente antes da adição de novas funcionalidades, como exumação, transferência, velório, arquivos ou taxas.

## Objetivos / Fora De Escopo

**Objetivos:**
- Tornar o estado de ciclo de vida determinístico e controlado pelos Services.
- Corrigir rotas públicas quebradas e bugs conhecidos em Services.
- Aplicar as regras de óbito antes de sepultamento e de capacidade da sepultura.
- Manter os estados de Grave, Deceased, Death e Burial sincronizados.
- Adicionar cobertura de testes suficiente para permitir a continuidade do desenvolvimento com segurança.

**Fora de escopo:**
- Não adicionar novos endpoints públicos para exumação, transferência, velório, arquivos ou taxas.
- Não renomear fisicamente colunas do banco de dados com typos sem migrations explícitas.
- Não redesenhar DTOs de request de forma incompatível durante esta estabilização.
- Não introduzir autenticação ou autorização.

## Decisões

- O estado de ciclo de vida pertence aos Services. Os campos de ciclo de vida nos DTOs de request permanecem por compatibilidade, mas os Services de criação/atualização definem ou preservam `status`, `archived` e `archivedAt` conforme as regras de domínio.
- Burial exige Death. `DeathRepository.existsByDeceasedId(...)` passa a ser uma pré-condição para criar ou mover um sepultamento ativo para um falecido.
- A capacidade da sepultura é calculada pela contagem de sepultamentos ativos. `BurialStatus.IN_PROGRESS` consome capacidade; `CANCELLED` não consome. Uma sepultura com um ou mais sepultamentos ativos fica `OCCUPIED`; uma sepultura com zero sepultamentos ativos fica `AVAILABLE`.
- Um falecido continua tendo no máximo um sepultamento ativo. Isso preserva a invariante atual do falecido enquanto permite que uma sepultura receba múltiplos falecidos até sua capacidade.
- Arquivamento é uma operação de ciclo de vida, não uma exclusão. Falecidos arquivados não podem receber óbito, sepultamento ou atualizações que alterem ciclo de vida, e sepultamentos ativos bloqueiam o arquivamento.
- A exclusão de Declarant é protegida. Como declarantes possuem falecidos por relações com cascade, excluir um declarante com falecidos relacionados é rejeitado como regra de negócio.
- A correção do runtime de testes faz parte da mudança. A implementação deve permitir que `./mvnw test` execute no ambiente atual com JDK 21, sem depender de skip de testes.

## Riscos / Trade-offs

- DTOs de request ainda expõem campos de ciclo de vida mesmo que os Services os ignorem → documentar como dívida de compatibilidade e evitar quebra de clientes durante a estabilização.
- Typos existentes em colunas do banco permanecem → evitar risco acidental de migração de schema/dados; tratar com Flyway em uma mudança dedicada.
- Grave `OCCUPIED` passa a significar "possui ao menos um sepultamento ativo", e não "não possui capacidade restante" → consumidores da API devem usar as regras de capacidade/contagem para disponibilidade exata.
- O schema gerado por H2/JPA ainda é usado em desenvolvimento → manter baseline de migrations como dívida técnica posterior.

## Plano De Migração

Nenhuma migração explícita de dados é necessária para esta mudança. Registros existentes são interpretados a partir dos campos atuais de status e das linhas de sepultamento ativo. Se houver dados em produção, executar smoke checks para falecidos com campos de ciclo de vida nulos e para sepulturas cujo status não corresponda à contagem de sepultamentos ativos antes do rollout.

O rollback é em nível de código: reverter esta mudança. Nenhum rename físico de schema está incluído.

## Questões Em Aberto

- Mudanças futuras devem definir a semântica de `COMPLETED`, `EXHUMED` e `TRANSFERRED`.
- Mudanças futuras devem decidir se os campos de ciclo de vida nos DTOs devem ser removidos ou substituídos por endpoints de comando separados.
