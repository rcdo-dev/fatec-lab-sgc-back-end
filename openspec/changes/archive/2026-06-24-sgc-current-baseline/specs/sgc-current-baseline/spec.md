## ADDED Requirements

### Requirement: Baseline documenta arquitetura e organizacao do projeto
A baseline SHALL documentar que o SGC e uma API Spring Boot 3.5.x em Java 21, usando Spring Web, Spring Data JPA, Bean Validation, Flyway, PostgreSQL em runtime, H2 para testes, MapStruct, Lombok e Springdoc OpenAPI.

#### Scenario: Arquitetura atual e registrada
- **WHEN** a baseline for revisada
- **THEN** ela descreve a stack tecnica atual
- **AND** registra que a aplicacao esta organizada por pacotes de dominio e camadas Controller, Service, Repository, Entity, DTO e Mapper
- **AND** registra que esta mudanca nao altera codigo, schema de banco, dependencias ou contratos HTTP

### Requirement: Baseline documenta pacotes principais
A baseline SHALL documentar os pacotes `cemetery`, `person`, `burial`, `common` e `fee` conforme o estado atual do codigo.

#### Scenario: Pacotes sao descritos por responsabilidade
- **WHEN** a baseline listar a estrutura de pacotes
- **THEN** `cemetery` cobre Cemetery, Block, Grave e WakeConfiguration
- **AND** `person` cobre Declarant, Deceased, DeceasedIdentified, DeceasedUnidentified, DeceasedPet e Death
- **AND** `burial` cobre Burial e Wake
- **AND** `common` cobre configuracoes, enums, excecoes e utilitarios compartilhados
- **AND** `fee` e registrado como pacote presente sem fluxo funcional completo

### Requirement: Baseline documenta entidades e relacionamentos implementados
A baseline SHALL documentar as Entities implementadas, seus relacionamentos JPA e os esqueletos que ainda nao sao entidades persistentes funcionais.

#### Scenario: Relacionamentos principais sao registrados
- **WHEN** a baseline descrever o modelo de dados atual
- **THEN** ela registra `CemeteryEntity` com `BlockEntity` e `WakeConfigurationEntity`
- **AND** registra `BlockEntity` com `GraveEntity`
- **AND** registra `GraveEntity` com `BurialEntity`
- **AND** registra `DeclarantEntity` com `DeceasedIdentifiedEntity`, `DeceasedUnidentifiedEntity` e `DeceasedPetEntity`
- **AND** registra `DeceasedEntity` como classe abstrata persistida com heranca `JOINED`
- **AND** registra `DeathEntity` vinculado a `DeceasedEntity`
- **AND** registra `BurialEntity` vinculado a `DeceasedEntity` e `GraveEntity`
- **AND** registra `WakeEntity` vinculado a `DeceasedEntity` e `CemeteryEntity`

#### Scenario: Classes incompletas sao classificadas como gap
- **WHEN** a baseline mencionar `ExhumationEntity`, `FileEntity`, `FeeEntity` ou `EventFee`
- **THEN** ela registra que essas classes nao possuem fluxo funcional completo no estado atual
- **AND** nao as apresenta como modulos publicos implementados

### Requirement: Baseline documenta Controllers e endpoints disponiveis
A baseline SHALL documentar a superficie HTTP atualmente exposta pelos Controllers existentes.

#### Scenario: Endpoints publicos sao listados
- **WHEN** a baseline documentar Controllers
- **THEN** ela lista `/cemeteries` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **AND** lista `/blocks` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **AND** lista `/graves` com POST, GET, GET por id, PUT por id e PATCH `/{id}/inactive`
- **AND** lista `/declarant` com POST, GET, GET por id, PUT por id e DELETE por id
- **AND** lista `/deceased-identified`, `/deceased-unidentified` e `/pet` com POST, GET, GET por id, PUT por id e PATCH `/{id}/archive`
- **AND** lista `/death` com POST, GET, GET por id, PUT por id e DELETE por id
- **AND** lista `/burials` com POST, GET, GET por id, PUT por id e PATCH `/{id}/cancel`
- **AND** lista `/wakes` com POST, GET, GET por id e PATCH `/{id}/cancel`

### Requirement: Baseline documenta Services e regras de negocio codificadas
A baseline SHALL documentar as regras de negocio efetivamente codificadas nos Services atuais.

#### Scenario: Regras de Cemetery, Block e Grave sao registradas
- **WHEN** a baseline documentar regras do pacote `cemetery`
- **THEN** ela registra unicidade de nome de Cemetery sem diferenca de caixa
- **AND** registra unicidade de numero de Block por Cemetery
- **AND** registra unicidade de numero de Grave por Block
- **AND** registra que novas Graves iniciam ativas e com status `AVAILABLE`
- **AND** registra que Grave de area `COMMON` deve ser `EARTH`
- **AND** registra que Grave de area `PERPETUAL` deve ser `EARTH` ou `MAUSOLEUM`
- **AND** registra capacidade maxima de 2 corpos para `EARTH` e 4 para `MAUSOLEUM`

#### Scenario: Regras de falecido e declarante sao registradas
- **WHEN** a baseline documentar regras do pacote `person`
- **THEN** ela registra que falecidos identificados, nao identificados e pets sao criados com `DeceasedStatus.ACTIVE`, `archived=false` e `archivedAt=null`
- **AND** registra validacao de documentos duplicados para Declarant e DeceasedIdentified
- **AND** registra que falecido arquivado nao pode ser atualizado nem receber Death ou Burial
- **AND** registra que arquivamento de falecido e bloqueado quando existe Burial em `IN_PROGRESS`
- **AND** registra que Declarant nao pode ser excluido enquanto houver falecidos relacionados

#### Scenario: Regras de Death, Burial e Wake sao registradas
- **WHEN** a baseline documentar regras dos pacotes `person` e `burial`
- **THEN** ela registra que existe no maximo um Death por Deceased
- **AND** registra que update de Death nao pode reassociar outro Deceased
- **AND** registra que Burial exige Death previamente cadastrado
- **AND** registra que Burial e criado como `IN_PROGRESS`
- **AND** registra que Burial muda Deceased para `BURIED` e Grave para `OCCUPIED`
- **AND** registra que Burial respeita capacidade, bloqueio, atividade e manutencao da Grave
- **AND** registra que cancelamento de Burial muda status para `CANCELLED`, reativa o Deceased e libera a Grave quando nao houver outro Burial ativo
- **AND** registra que Wake valida periodo, duracao maxima configurada por Cemetery e conflito de agenda para status `SCHEDULED`

### Requirement: Baseline documenta Repositories e consultas relevantes
A baseline SHALL documentar os Repositories existentes e as consultas derivadas ou customizadas relevantes para regras de negocio.

#### Scenario: Consultas relevantes sao registradas
- **WHEN** a baseline documentar persistencia
- **THEN** ela registra verificacoes de unicidade por Cemetery, Block, Grave, Declarant e DeceasedIdentified
- **AND** registra consultas de Burial por Deceased, Grave, status e contagem de Burials ativos por Grave
- **AND** registra `DeathRepository.existsByDeceasedId`
- **AND** registra `WakeRepository.existsScheduleConflict`
- **AND** registra `WakeConfigurationRepository.findByCemeteryId`
- **AND** registra `CemeteryRepository.countByActiveTrue` e `findFirstByActiveTrue`

### Requirement: Baseline documenta DTOs, Mappers e enums
A baseline SHALL documentar os DTOs de request/response, os Mappers MapStruct e os enums usados pela API.

#### Scenario: Contratos auxiliares sao registrados
- **WHEN** a baseline documentar contratos de entrada e saida
- **THEN** ela registra DTOs de request e response para Cemetery, Block, Grave, Declarant, DeceasedIdentified, DeceasedUnidentified, DeceasedPet, Death, Burial e Wake
- **AND** registra DTOs de suporte para documento, contato e endereco
- **AND** registra Mappers MapStruct com `componentModel = "spring"` para Cemetery, Block, Grave, Declarant, DeceasedIdentified, DeceasedUnidentified, DeceasedPet, Death, Burial e Wake
- **AND** registra os enums `AreaType`, `BurialStatus`, `DeceasedStatus`, `EyeType`, `GenderIdentityType`, `GenderType`, `GraveStatus`, `GraveType`, `HairType`, `SkinColor` e `WakeStatus`

### Requirement: Baseline documenta excecoes, mensagens e tratamento de erro
A baseline SHALL documentar o tratamento centralizado de erros e as mensagens configuradas no estado atual.

#### Scenario: Erros padronizados sao registrados
- **WHEN** a baseline documentar excecoes
- **THEN** ela registra `BusinessException` como HTTP 400
- **AND** registra `ResourceNotFoundException` como HTTP 404
- **AND** registra `ConflictException` como HTTP 409
- **AND** registra tratamento para JSON malformado e validacao de campos como HTTP 400
- **AND** registra `ApiErrorResponse` como formato de resposta de erro
- **AND** registra que mensagens de dominio estao em `messages.properties`

### Requirement: Baseline documenta gaps, riscos e pendencias
A baseline MUST separar lacunas do dominio esperado das capacidades ja implementadas.

#### Scenario: Gaps principais sao registrados
- **WHEN** a baseline documentar gaps
- **THEN** ela registra que `Contract` nao foi encontrado no codigo atual
- **AND** registra que regras de exumacao nao estao implementadas
- **AND** registra que `ExhumationEntity` e `FileEntity` sao classes esqueleto sem Entity, Service, Repository ou Controller funcional
- **AND** registra que exclusao fisica ainda existe para `Death` e `Declarant` em cenarios permitidos
- **AND** registra que `BurialEntity` usa associacao `@OneToOne` com Deceased, embora Services usem historico por status
- **AND** registra que transicoes de `GraveService` nao possuem endpoints publicos dedicados
- **AND** registra que inativacao de Cemetery, Block e Grave nao valida todos os vinculos administrativos relevantes
- **AND** registra inconsistencias pontuais de mensagens, nomes e tratamento de erro como risco de padronizacao

### Requirement: Baseline recomenda proximas specs
A baseline SHALL recomendar proximas specs para evolucao controlada do SGC.

#### Scenario: Recomendacoes sao priorizadas
- **WHEN** a baseline apresentar recomendacoes
- **THEN** ela recomenda specs futuras para Contract, Exhumation, historico/auditoria de registros administrativos, padronizacao de exclusao logica, endpoints de transicao de Grave, hardening de inativacao e padronizacao de mensagens
- **AND** ela recomenda reconciliar `BurialEntity` com o modelo de historico antes de ampliar regras de sepultamento
