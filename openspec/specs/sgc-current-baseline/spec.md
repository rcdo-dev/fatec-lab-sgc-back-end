# sgc-current-baseline Specification

## Purpose
Esta especificação registra a baseline documental do estado atual do SGC após a adoção do OpenSpec no meio do desenvolvimento. Ela serve como referência para desenvolvedores entenderem a arquitetura, os módulos, as regras já implementadas, as lacunas conhecidas e as próximas specs recomendadas, sem introduzir novas funcionalidades.

## Requirements
### Requirement: Baseline documenta arquitetura e organização do projeto
A baseline SHALL (deve) documentar que o SGC é uma API Spring Boot 3.5.x em Java 21, usando Spring Web, Spring Data JPA, Bean Validation, Flyway, PostgreSQL em runtime, H2 para testes, MapStruct, Lombok e Springdoc OpenAPI.

#### Scenario: Arquitetura atual é registrada
- **QUANDO** a baseline for revisada
- **ENTÃO** ela descreve a stack técnica atual
- **E** registra que a aplicação está organizada por pacotes de domínio e camadas Controller, Service, Repository, Entity, DTO e Mapper
- **E** registra que esta mudança não altera código, schema de banco, dependências ou contratos HTTP

### Requirement: Baseline documenta pacotes principais
A baseline SHALL (deve) documentar os pacotes `cemetery`, `person`, `burial`, `common` e `fee` conforme o estado atual do código.

#### Scenario: Pacotes são descritos por responsabilidade
- **QUANDO** a baseline listar a estrutura de pacotes
- **ENTÃO** `cemetery` cobre Cemetery, Block, Grave e WakeConfiguration
- **E** `person` cobre Declarant, Deceased, DeceasedIdentified, DeceasedUnidentified, DeceasedPet e Death
- **E** `burial` cobre Burial e Wake
- **E** `common` cobre configurações, enums, exceções e utilitários compartilhados
- **E** `fee` é registrado como pacote presente sem fluxo funcional completo

### Requirement: Baseline documenta entidades e relacionamentos implementados
A baseline SHALL (deve) documentar as Entities implementadas, seus relacionamentos JPA e os esqueletos que ainda não são entidades persistentes funcionais.

#### Scenario: Relacionamentos principais são registrados
- **QUANDO** a baseline descrever o modelo de dados atual
- **ENTÃO** ela registra `CemeteryEntity` com `BlockEntity` e `WakeConfigurationEntity`
- **E** registra `BlockEntity` com `GraveEntity`
- **E** registra `GraveEntity` com `BurialEntity`
- **E** registra `DeclarantEntity` com `DeceasedIdentifiedEntity`, `DeceasedUnidentifiedEntity` e `DeceasedPetEntity`
- **E** registra `DeceasedEntity` como classe abstrata persistida com herança `JOINED`
- **E** registra `DeathEntity` vinculado a `DeceasedEntity`
- **E** registra `BurialEntity` vinculado a `DeceasedEntity` e `GraveEntity`
- **E** registra `WakeEntity` vinculado a `DeceasedEntity` e `CemeteryEntity`

#### Scenario: Classes incompletas são classificadas como gap
- **QUANDO** a baseline mencionar `ExhumationEntity`, `FileEntity`, `FeeEntity` ou `EventFee`
- **ENTÃO** ela registra que essas classes não possuem fluxo funcional completo no estado atual
- **E** não as apresenta como módulos públicos implementados

### Requirement: Baseline documenta Controllers e endpoints disponíveis
A baseline SHALL (deve) documentar a superfície HTTP atualmente exposta pelos Controllers existentes.

#### Scenario: Endpoints públicos são listados
- **QUANDO** a baseline documentar Controllers
- **ENTÃO** ela lista `/cemeteries` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **E** lista `/blocks` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **E** lista `/graves` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **E** lista `/declarant` com POST, GET, GET por id, PUT por id e DELETE por id
- **E** lista `/deceased-identified`, `/deceased-unidentified` e `/pet` com POST, GET, GET por id, PUT por id e PATCH `/{id}/archive`
- **E** lista `/death` com POST, GET, GET por id, PUT por id e DELETE por id
- **E** lista `/burials` com POST, GET, GET por id, PUT por id e PATCH `/{id}/cancel`
- **E** lista `/wakes` com POST, GET, GET por id e PATCH `/{id}/cancel`

### Requirement: Baseline documenta Services e regras de negócio codificadas
A baseline SHALL (deve) documentar as regras de negócio efetivamente codificadas nos Services atuais.

#### Scenario: Regras de Cemetery, Block e Grave são registradas
- **QUANDO** a baseline documentar regras do pacote `cemetery`
- **ENTÃO** ela registra unicidade de nome de Cemetery sem diferença de caixa
- **E** registra unicidade de número de Block por Cemetery
- **E** registra unicidade de número de Grave por Block
- **E** registra que novas Graves iniciam ativas e com status `AVAILABLE`
- **E** registra que Grave de área `COMMON` deve ser `EARTH`
- **E** registra que Grave de área `PERPETUAL` deve ser `EARTH` ou `MAUSOLEUM`
- **E** registra capacidade máxima de 2 corpos para `EARTH` e 4 para `MAUSOLEUM`

#### Scenario: Regras de falecido e declarante são registradas
- **QUANDO** a baseline documentar regras do pacote `person`
- **ENTÃO** ela registra que falecidos identificados, não identificados e pets são criados com `DeceasedStatus.ACTIVE`, `archived=false` e `archivedAt=null`
- **E** registra validação de documentos duplicados para Declarant e DeceasedIdentified
- **E** registra que falecido arquivado não pode ser atualizado nem receber Death ou Burial
- **E** registra que arquivamento de falecido é bloqueado quando existe Burial em `IN_PROGRESS`
- **E** registra que Declarant não pode ser excluído enquanto houver falecidos relacionados

#### Scenario: Regras de Death, Burial e Wake são registradas
- **QUANDO** a baseline documentar regras dos pacotes `person` e `burial`
- **ENTÃO** ela registra que existe no máximo um Death por Deceased
- **E** registra que update de Death não pode reassociar outro Deceased
- **E** registra que Burial exige Death previamente cadastrado
- **E** registra que Burial é criado como `IN_PROGRESS`
- **E** registra que Burial muda Deceased para `BURIED` e Grave para `OCCUPIED`
- **E** registra que Burial respeita capacidade, bloqueio, atividade e manutenção da Grave
- **E** registra que cancelamento de Burial muda status para `CANCELLED`, reativa o Deceased e libera a Grave quando não houver outro Burial ativo
- **E** registra que Wake valida período, duração máxima configurada por Cemetery e conflito de agenda para status `SCHEDULED`

### Requirement: Baseline documenta Repositories e consultas relevantes
A baseline SHALL (deve) documentar os Repositories existentes e as consultas derivadas ou customizadas relevantes para regras de negócio.

#### Scenario: Consultas relevantes são registradas
- **QUANDO** a baseline documentar persistência
- **ENTÃO** ela registra verificações de unicidade por Cemetery, Block, Grave, Declarant e DeceasedIdentified
- **E** registra consultas de Burial por Deceased, Grave, status e contagem de Burials ativos por Grave
- **E** registra `DeathRepository.existsByDeceasedId`
- **E** registra `WakeRepository.existsScheduleConflict`
- **E** registra `WakeConfigurationRepository.findByCemeteryId`
- **E** registra `CemeteryRepository.countByActiveTrue` e `findFirstByActiveTrue`

### Requirement: Baseline documenta DTOs, Mappers e enums
A baseline SHALL (deve) documentar os DTOs de request/response, os Mappers MapStruct e os enums usados pela API.

#### Scenario: Contratos auxiliares são registrados
- **QUANDO** a baseline documentar contratos de entrada e saída
- **ENTÃO** ela registra DTOs de request e response para Cemetery, Block, Grave, Declarant, DeceasedIdentified, DeceasedUnidentified, DeceasedPet, Death, Burial e Wake
- **E** registra DTOs de suporte para documento, contato e endereço
- **E** registra Mappers MapStruct com `componentModel = "spring"` para Cemetery, Block, Grave, Declarant, DeceasedIdentified, DeceasedUnidentified, DeceasedPet, Death, Burial e Wake
- **E** registra os enums `AreaType`, `BurialStatus`, `DeceasedStatus`, `EyeType`, `GenderIdentityType`, `GenderType`, `GraveStatus`, `GraveType`, `HairType`, `SkinColor` e `WakeStatus`

### Requirement: Baseline documenta exceções, mensagens e tratamento de erro
A baseline SHALL (deve) documentar o tratamento centralizado de erros e as mensagens configuradas no estado atual.

#### Scenario: Erros padronizados são registrados
- **QUANDO** a baseline documentar exceções
- **ENTÃO** ela registra `BusinessException` como HTTP 400
- **E** registra `ResourceNotFoundException` como HTTP 404
- **E** registra `ConflictException` como HTTP 409
- **E** registra tratamento para JSON malformado e validação de campos como HTTP 400
- **E** registra `ApiErrorResponse` como formato de resposta de erro
- **E** registra que mensagens de domínio estão em `messages.properties`

### Requirement: Baseline documenta gaps, riscos e pendências
A baseline MUST (deve obrigatoriamente) separar lacunas do domínio esperado das capacidades já implementadas.

#### Scenario: Gaps principais são registrados
- **QUANDO** a baseline documentar gaps
- **ENTÃO** ela registra que `Contract` não foi encontrado no código atual
- **E** registra que regras de exumação não estão implementadas
- **E** registra que `ExhumationEntity` e `FileEntity` são classes esqueleto sem Entity, Service, Repository ou Controller funcional
- **E** registra que exclusão física ainda existe para `Death` e `Declarant` em cenários permitidos
- **E** registra que `BurialEntity` usa associação `@OneToOne` com Deceased, embora Services usem histórico por status
- **E** registra que transições de `GraveService` não possuem endpoints públicos dedicados
- **E** registra que inativação de Cemetery, Block e Grave não valida todos os vínculos administrativos relevantes
- **E** registra inconsistências pontuais de mensagens, nomes e tratamento de erro como risco de padronização

### Requirement: Baseline recomenda proximas specs
A baseline SHALL (deve) recomendar próximas specs para evolução controlada do SGC.

#### Scenario: Recomendações são priorizadas
- **QUANDO** a baseline apresentar recomendações
- **ENTÃO** ela recomenda specs futuras para Contract, Exhumation, histórico/auditoria de registros administrativos, padronização de exclusão lógica, endpoints de transição de Grave, hardening de inativação e padronização de mensagens
- **E** ela recomenda reconciliar `BurialEntity` com o modelo de histórico antes de ampliar regras de sepultamento
