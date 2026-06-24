## Context

O SGC e uma API Spring Boot organizada por dominio, com camadas Controller, Service, Repository, Entity, DTO e Mapper. O OpenSpec foi introduzido depois de parte do sistema ja existir, entao ha regras implementadas sem uma baseline documental ampla.

O estado atual observado no codigo inclui os modulos `cemetery`, `person`, `burial`, `common` e `fee`. Os fluxos funcionais principais cobrem cemiterios, quadras, sepulturas, declarantes, falecidos identificados, nao identificados e pets, obitos, sepultamentos e velorios. O pacote `fee` existe apenas com classes vazias, e `ExhumationEntity`/`FileEntity` existem como esqueletos sem anotacoes JPA ou superficie publica. Nenhuma implementacao de `Contract` foi encontrada.

## Goals / Non-Goals

**Goals:**
- Criar uma baseline documental unica para o estado atual do SGC.
- Separar capacidades ja implementadas de gaps, riscos e pendencias.
- Registrar arquitetura, pacotes, entidades, relacionamentos, endpoints, Services, Repositories, DTOs, Mappers, enums, excecoes e mensagens.
- Usar portugues do Brasil e preservar termos tecnicos como Controller, Service, Repository, DTO, Mapper e Entity.

**Non-Goals:**
- Nao alterar codigo Java, migrations, testes, configuracoes runtime ou dependencias.
- Nao implementar Contract, Exhumation, File, Fee ou qualquer nova funcionalidade.
- Nao corrigir divergencias encontradas; apenas documenta-las.
- Nao modificar as specs existentes de ciclo de vida, obito ou disponibilidade de sepultura.

## Decisions

- A baseline sera uma capability unica chamada `sgc-current-baseline`. Alternativa considerada: criar uma spec por modulo. A spec unica foi escolhida porque o objetivo e congelar uma fotografia transversal do estado atual, nao redefinir contratos por bounded context.
- A baseline sera criada como delta spec em `openspec/changes/sgc-current-baseline/specs/sgc-current-baseline/spec.md`. Alternativa considerada: editar diretamente `openspec/specs`. A mudanca OpenSpec preserva revisao antes de arquivamento.
- A documentacao classificara fatos do codigo como capacidades implementadas e divergencias como gaps ou riscos. Alternativa considerada: transformar lacunas em requisitos desejados. Isso foi rejeitado para evitar que a baseline pareca prometer funcionalidades inexistentes.
- A verificacao sera documental e estrutural: `openspec status --change sgc-current-baseline`, validacao OpenSpec disponivel e `./mvnw test`. Como nao ha alteracao de codigo, os testes servem apenas como regressao de seguranca.

## Risks / Trade-offs

- [Risk] A baseline pode ficar extensa por cobrir varios modulos em uma unica spec. -> Mitigacao: agrupar por requisitos documentais e manter detalhes operacionais no nivel necessario para orientar proximas specs.
- [Risk] Gaps podem ser confundidos com funcionalidades implementadas. -> Mitigacao: marcar explicitamente o que nao existe ou esta apenas como esqueleto.
- [Risk] O codigo pode mudar antes do arquivamento da baseline. -> Mitigacao: validar a spec contra o inventario atual antes de concluir.
- [Risk] Algumas mensagens usam chaves diretamente ou possuem inconsistencias textuais. -> Mitigacao: registrar como risco de padronizacao, sem tentar corrigir nesta mudanca.
