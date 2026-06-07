## 1. OpenSpec e Contratos

- [x] 1.1 Criar proposal, design e delta specs para as regras de ciclo de vida de falecido, óbito, sepultamento e sepultura.
- [x] 1.2 Corrigir os mappings das rotas de atualização/exclusão de Death e o binding da rota de arquivamento de falecido não identificado.
- [x] 1.3 Adicionar ou atualizar as chaves de mensagem necessárias para erros de validação de ciclo de vida.

## 2. Ciclo De Vida De Falecido E Óbito

- [x] 2.1 Fazer a criação de falecido sempre persistir `ACTIVE`, `archived=false` e `archivedAt=null`.
- [x] 2.2 Mapear os campos persistidos de ciclo de vida do falecido para os DTOs de response de identificado, não identificado e pet.
- [x] 2.3 Bloquear updates e criação de Death para falecidos arquivados.
- [x] 2.4 Bloquear arquivamento quando o falecido possui sepultamento ativo.
- [x] 2.5 Corrigir o update de pet e o update de `genderIdentity` em falecido identificado.
- [x] 2.6 Corrigir o update de Death para preservar a associação com o falecido e evitar falso conflito com a própria associação.

## 3. Ciclo De Vida De Sepultamento E Sepultura

- [x] 3.1 Exigir Death cadastrado antes de criar ou mover um Burial ativo para um falecido.
- [x] 3.2 Garantir um sepultamento ativo por falecido, permitindo múltiplos sepultamentos ativos por sepultura até `bodyCapacity`.
- [x] 3.3 Rejeitar sepultamento em sepulturas inativas, bloqueadas, em manutenção ou sem capacidade disponível.
- [x] 3.4 Manter o status da sepultura sincronizado com a contagem de sepultamentos ativos durante criação, atualização e cancelamento.
- [x] 3.5 Corrigir a lógica interna de ativação de sepultura.

## 4. Declarant E Consistência

- [x] 4.1 Impedir a exclusão de declarantes que ainda possuem falecidos relacionados.
- [x] 4.2 Padronizar os erros dos Services alterados usando `MessageSource`.
- [x] 4.3 Registrar typos de banco e baseline Flyway como dívida técnica, sem renomes inseguros de schema.

## 5. Testes E Verificação

- [x] 5.1 Desbloquear `./mvnw test` no ambiente atual com JDK 21.
- [x] 5.2 Adicionar testes de Service para defaults de falecido, restrições de arquivamento, unicidade/update de Death, exigência de Death para Burial, capacidade de sepultura, cancelamento e estados rejeitados de sepultura.
- [x] 5.3 Adicionar testes de Controller para rotas corrigidas de Death, binding de archive de falecido não identificado e status codes de validação/erro.
- [x] 5.4 Executar `./mvnw test` e `./mvnw -DskipTests package` com sucesso.
