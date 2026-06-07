## Por Que

A implementação atual do SGC mistura estado de ciclo de vida, comportamento de CRUD e efeitos colaterais de sepultamento sem um contrato de domínio explícito. Vários defeitos já afetam rotas públicas, respostas de status de falecido, atualização de óbito, capacidade de sepultura e regras de arquivamento/exclusão; por isso, esta estabilização deve acontecer antes da adição de novas funcionalidades de ciclo de vida.

## O Que Muda

- Corrigir contratos HTTP quebrados para atualização/exclusão de Death e arquivamento de falecido não identificado.
- Tornar o estado de ciclo de vida do falecido controlado pelo sistema: novos registros de falecido iniciam ativos e não arquivados, e as responses expõem os campos de ciclo de vida persistidos.
- Impedir que falecidos arquivados recebam óbito, sepultamento ou atualizações que alterem ciclo de vida.
- Exigir óbito cadastrado antes que um falecido possa ser sepultado.
- Manter um único óbito por falecido e corrigir a atualização de óbito para não gerar conflito com sua própria associação.
- Aplicar a capacidade da sepultura pela contagem de sepultamentos ativos, em vez de bloquear qualquer segundo sepultamento na mesma sepultura.
- Rejeitar sepultamento em sepulturas inativas, bloqueadas, em manutenção ou sem capacidade disponível.
- Manter o status da sepultura sincronizado com a contagem de sepultamentos ativos.
- Impedir a exclusão de declarantes que ainda possuem falecidos relacionados.
- Corrigir bugs conhecidos em Services e inconsistências em Mappers antes de implementar novas funcionalidades.
- Manter renomes físicos de banco de dados e baseline Flyway como dívida técnica documentada, salvo quando puderem ser tratados com segurança por migrations explícitas.

## Capacidades

### Novas Capacidades
- `deceased-lifecycle`: valores padrão de ciclo de vida, restrições de arquivamento, estado em response e restrições de atualização para registros de falecidos.
- `death-registration`: comportamento de um óbito por falecido e óbito como pré-requisito para sepultamento.
- `burial-lifecycle`: criação, atualização e cancelamento de sepultamento, incluindo efeitos colaterais de status em falecido e sepultura.
- `grave-availability`: semântica de capacidade, bloqueio, manutenção, ativação e disponibilidade de sepulturas.

### Capacidades Modificadas
- Nenhuma. Não há capacidades OpenSpec existentes neste repositório.

## Impacto

- Afeta Controllers, Services, Repositories e Mappers dos módulos de pessoa, sepultamento e cemitério.
- Afeta o comportamento da API pública para Death, Deceased Identified, Deceased Unidentified, Deceased Pet, Declarant, Burial e Grave.
- Adiciona ou atualiza testes para Services de ciclo de vida e rotas de Controller afetadas.
- Exige ajuste no runtime de testes para que `./mvnw test` execute corretamente no ambiente atual com JDK 21.
