# contract-management Specification

## Purpose
Esta especificacao define o modulo de gerenciamento de contratos do SGC. Um contrato representa o titulo de posse de uma sepultura e deve permitir registro, consulta, atualizacao, inativacao logica e vinculo com sepultura nova ou existente.

## Requirements
### Requirement: Contrato pode ser registrado com sepultura nova
O sistema SHALL registrar contrato criando uma sepultura quando o request informar os dados minimos da nova sepultura.

#### Scenario: Contrato cria sepultura com regras do dominio
- **DADO** um request valido de contrato com `newGrave` e `blockId`
- **QUANDO** o contrato for registrado
- **ENTAO** o sistema cria a sepultura como ativa
- **E** define `graveType=MAUSOLEUM`
- **E** define `areaType=PERPETUAL`
- **E** define `status=AVAILABLE`
- **E** vincula o contrato a essa sepultura

### Requirement: Contrato pode ser registrado com sepultura existente
O sistema SHALL registrar contrato vinculando uma sepultura existente quando o request informar `graveId`.

#### Scenario: Sepultura existente compativel e vinculada
- **DADO** uma sepultura ativa, nao bloqueada, `MAUSOLEUM` e `PERPETUAL`
- **E** sem contrato aberto
- **QUANDO** um contrato valido for registrado com essa sepultura
- **ENTAO** o sistema registra o contrato
- **E** vincula o contrato a sepultura informada

### Requirement: Contrato exige quadra e exatamente uma sepultura
O sistema MUST rejeitar contrato sem quadra, sem sepultura ou com selecao ambigua de sepultura.

#### Scenario: Request nao informa sepultura
- **DADO** um request de contrato sem `graveId` e sem `newGrave`
- **QUANDO** o operador tentar registrar o contrato
- **ENTAO** o sistema rejeita a operacao como violacao de regra de negocio

#### Scenario: Request informa sepultura existente e nova sepultura
- **DADO** um request de contrato com `graveId` e `newGrave`
- **QUANDO** o operador tentar registrar o contrato
- **ENTAO** o sistema rejeita a operacao como violacao de regra de negocio

#### Scenario: Quadra nao encontrada
- **DADO** um request de contrato com `blockId` inexistente
- **QUANDO** o operador tentar registrar o contrato
- **ENTAO** o sistema rejeita a operacao como recurso nao encontrado
- **E** informa "Quadra nao encontrada."

### Requirement: Contrato respeita unicidade e periodo de vigencia
O sistema MUST garantir numero unico de contrato e data final posterior a data inicial.

#### Scenario: Numero de contrato duplicado
- **DADO** um contrato ja cadastrado com determinado numero
- **QUANDO** outro contrato for registrado com o mesmo numero
- **ENTAO** o sistema rejeita a operacao
- **E** informa "Numero de contrato ja cadastrado."

#### Scenario: Data final anterior ou igual a inicial
- **DADO** um request de contrato com data final anterior ou igual a data inicial
- **QUANDO** o operador tentar registrar ou atualizar o contrato
- **ENTAO** o sistema rejeita a operacao

### Requirement: Sepultura vinculada a contrato deve ser compativel
O sistema MUST vincular contratos apenas a sepulturas ativas, nao bloqueadas, `MAUSOLEUM` e `PERPETUAL`.

#### Scenario: Sepultura incompativel
- **DADO** uma sepultura inativa, bloqueada ou que nao seja `MAUSOLEUM` e `PERPETUAL`
- **QUANDO** o operador tentar vincula-la a contrato
- **ENTAO** o sistema rejeita a operacao
- **E** informa "A sepultura selecionada nao pode ser vinculada a contratos."

#### Scenario: Sepultura ja possui contrato aberto
- **DADO** uma sepultura com contrato `ACTIVE`, `SUSPENDED` ou `OVERDUE`
- **QUANDO** o operador tentar vincula-la a outro contrato
- **ENTAO** o sistema rejeita a operacao
- **E** informa "Esta sepultura ja possui um contrato ativo."

### Requirement: Contrato usa transicoes de estado controladas
O sistema SHALL controlar as transicoes de estado de contrato por regras de dominio.

#### Scenario: Transicoes permitidas
- **QUANDO** um contrato `ACTIVE` for suspenso
- **ENTAO** o status muda para `SUSPENDED`
- **QUANDO** um contrato `SUSPENDED` for reativado
- **ENTAO** o status muda para `ACTIVE`
- **QUANDO** um contrato `OVERDUE` for expirado
- **ENTAO** o status muda para `EXPIRED`
- **QUANDO** um contrato `ACTIVE` for inativado
- **ENTAO** o status muda para `INACTIVE`

#### Scenario: Contrato encerrado nao retorna para ativo
- **DADO** um contrato `EXPIRED` ou `INACTIVE`
- **QUANDO** o operador tentar reativa-lo
- **ENTAO** o sistema rejeita a transicao

### Requirement: Vigencia expirada muda contrato para OVERDUE
O sistema SHALL atualizar automaticamente contratos `ACTIVE` vencidos para `OVERDUE`.

#### Scenario: Contrato ativo com vigencia expirada
- **DADO** um contrato `ACTIVE` com `endDate` anterior ao dia atual
- **QUANDO** a rotina automatica de vencimento executar
- **ENTAO** o status do contrato passa a ser `OVERDUE`

### Requirement: Contrato nao sofre exclusao fisica
O sistema MUST preservar registros de contrato e usar inativacao logica.

#### Scenario: Inativacao de contrato
- **DADO** um contrato `ACTIVE`
- **QUANDO** o operador inativar o contrato
- **ENTAO** o sistema altera seu status para `INACTIVE`
- **E** mantem o registro persistido
