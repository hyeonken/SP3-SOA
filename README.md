# Operum Advisor - Sistema de Recomendação de Investimentos

##  Grupo 3ESPF
- **Rony Nagai** | RM551549
- **Tomaz Versolato** | RM551417
- **Pedro Valotto** | RM551445

---

## Visão Geral

**Operum Advisor** é um sistema completo de assessoria financeira que utiliza **inteligência artificial** para analisar o perfil de risco dos clientes e recomendar carteiras de investimento personalizadas. O sistema implementa **SOLID**, **Strategy Pattern**, **autenticação JWT**, **criptografia de dados sensíveis** e total conformidade com a **LGPD**.

### Funcionalidades Principais
- **Cadastro de Clientes** com validação LGPD
- **Análise de Perfil de Risco** (Conservador, Moderado, Arrojado)
- **Recomendação Automática** de carteiras usando Strategy Pattern
- **Autenticação JWT** com tokens seguros
- **Criptografia AES-256** para dados sensíveis
- **API RESTful** documentada com Swagger/OpenAPI
- **Testes Automatizados** (49 testes, 80% de cobertura)

---

## Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **MySQL 8.0** - Banco de dados relacional
- **Flyway** - Migrações de banco de dados
- **JWT (jjwt 0.12.6)** - Autenticação stateless
- **JaCoCo** - Cobertura de código

### Ferramentas de Desenvolvimento
- **Maven Wrapper** - Gerenciamento de dependências
- **SpringDoc OpenAPI 3** - Documentação automática
- **JUnit 5** - Framework de testes
- **Mockito** - Mocks para testes
- **H2/MySQL** - Banco de testes e produção

---

## Arquitetura e Design Patterns

### Design Patterns Implementados
1. **Strategy Pattern** 
   - `CarteiraSelectionStrategy` (interface)
   - `ConservadoraStrategy`, `ModeradaStrategy`, `AgressivaStrategy`
   - Seleção dinâmica de estratégia baseada no perfil de risco

2. **Repository Pattern** 
   - Abstração da camada de dados
   - Interfaces JPA Repository para cada entidade

3. **DTO Pattern** 
   - Separação entre entidades de domínio e objetos de transferência
   - Request/Response DTOs para cada endpoint

4. **Exception Handler** 
   - `GlobalExceptionHandler` centralizado
   - Tratamento de exceções customizado

### Princípios SOLID
- **Single Responsibility** - Cada classe tem uma única responsabilidade
- **Open/Closed** - Extensível via Strategy Pattern
- **Liskov Substitution** - Implementações de estratégia são intercambiáveis
- **Interface Segregation** - Interfaces específicas por funcionalidade
- **Dependency Inversion** - Depende de abstrações, não implementações

---

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior ([Download JDK](https://adoptium.net/))
- **MySQL 8.0** ou superior ([Download MySQL](https://dev.mysql.com/downloads/mysql/))
- **Git** ([Download Git](https://git-scm.com/downloads))
- **Postman** ou **Insomnia** (opcional, para testar a API)

### Verificar Instalações
```bash
java -version     # Deve mostrar Java 17+
mysql --version   # Deve mostrar MySQL 8.0+
git --version     # Qualquer versão recente
```

---

## Instalação e Configuração

### 1. Clonar o Repositório
```bash
git clone https://github.com/hyeonken/SP3-SOA.git
cd SP3-SOA
```

### 2. Configurar o Banco de Dados MySQL

#### Opção A: Via MySQL Workbench
1. Abra o MySQL Workbench
2. Conecte ao servidor local
3. Execute o comando:
```sql
CREATE DATABASE operum CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Opção B: Via Terminal
```bash
mysql -u root -p
CREATE DATABASE operum CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

### 3. Configurar Credenciais (se necessário)

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/operum?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=fiap  # Altere para sua senha do MySQL

# Chave de Criptografia (NÃO compartilhe em produção!)
app.encryption.secret=operum-secret-key

# Credenciais de Autenticação (usuário fixo para testes)
app.security.username=operum
app.security.password=operum123

# JWT Secret (troque em produção!)
jwt.secret=operumJwtSecretKey2025VerySecureAndLongKeyForProductionUseWithoutSpecialCharacters
jwt.expiration=86400000  # 24 horas
```

### 4. Compilar o Projeto
```bash
# Windows
.\mvnw clean install

# Linux/Mac
./mvnw clean install
```

---

## 🚀 Como Executar

### Método 1: Via Maven Wrapper (Recomendado)
```bash
# Windows
.\mvnw spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Método 2: Via JAR Compilado
```bash
# Compilar
.\mvnw clean package -DskipTests

# Executar
java -jar target/operum-advisor-0.0.1-SNAPSHOT.jar
```

### Método 3: Via IDE (Eclipse/IntelliJ)
1. Importe o projeto como **Maven Project**
2. Aguarde o download das dependências
3. Execute a classe `OperumAdvisorApplication.java`

### Verificar se Está Funcionando

Acesse no navegador:
- **API Health**: http://localhost:8080/actuator/health (se configurado)
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs

Você deve ver:
```
Aplicação rodando na porta 8080
Flyway executou 2 migrações com sucesso
Banco de dados populado com carteiras modelo
```

---

## Guia de Uso da API

### Fluxo Completo de Uso

```mermaid
sequenceDiagram
    participant U as Usuário
    participant API as Operum API
    participant DB as MySQL
    
    U->>API: 1. POST /auth/login (operum/operum123)
    API->>U: Token JWT válido por 24h
    
    U->>API: 2. POST /clientes (com JWT)
    API->>DB: Salva cliente criptografado
    API->>U: Cliente criado (ID: 1)
    
    U->>API: 3. POST /diagnosticos (clienteId=1)
    API->>DB: Calcula perfil de risco
    API->>U: Perfil: MODERADO
    
    U->>API: 4. POST /recomendacoes (clienteId=1)
    API->>DB: Busca carteira via Strategy Pattern
    API->>U: Carteira Moderada recomendada
    
    U->>API: 5. GET /recomendacoes/1
    API->>U: Histórico completo
```

---

## Autenticação JWT

### 1. Obter Token de Acesso

**Endpoint:** `POST /auth/login`

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "operum",
    "password": "operum123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "operum",
  "expiresIn": 86400000
}
```

### 2. Usar o Token nas Requisições

Adicione o header `Authorization` em todas as requisições:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo com cURL:**
```bash
curl -X GET http://localhost:8080/clientes \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Exemplo no Postman:**
1. Aba **Authorization**
2. Type: **Bearer Token**
3. Cole o token no campo **Token**

---

## Endpoints da API

### Autenticação

#### POST /auth/login
Autentica usuário e retorna token JWT válido por 24 horas.

**Request:**
```json
{
  "username": "operum",
  "password": "operum123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvcGVydW0iLCJleHAiOjE3Mjk1MzY4MDB9...",
  "type": "Bearer",
  "username": "operum",
  "expiresIn": 86400000
}
```

**Erros:**
- `401 Unauthorized` - Credenciais inválidas
- `400 Bad Request` - Campos obrigatórios ausentes

---

### 👤 Clientes

#### POST /clientes
Cadastra novo cliente com validação LGPD.

**Request:**
```json
{
  "nome": "Maria Silva",
  "cpf": "12345678909",
  "email": "maria.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria tranquila",
  "perfilRisco": "CONSERVADOR"
}
```

**Response (201):**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "cpf": "123.456.789-09",
  "email": "maria.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria tranquila",
  "perfilRisco": "CONSERVADOR",
  "dataCadastro": "2025-10-20T10:30:00"
}
```

**Validações:**
- CPF válido e único
- Email válido e único
- Consentimento LGPD obrigatório
- Perfil de risco: `CONSERVADOR`, `MODERADO` ou `ARROJADO`

---

#### GET /clientes
Lista todos os clientes cadastrados.

**Response (200):**
```json
[
  {
    "id": 1,
    "nome": "Maria Silva",
    "cpf": "123.456.789-09",
    "email": "maria.silva@email.com",
    "perfilRisco": "CONSERVADOR",
    "dataCadastro": "2025-10-20T10:30:00"
  }
]
```

---

#### GET /clientes/{id}
Busca cliente específico com dados descriptografados.

**Response (200):**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "cpf": "123.456.789-09",
  "email": "maria.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria tranquila",
  "perfilRisco": "CONSERVADOR",
  "dataCadastro": "2025-10-20T10:30:00"
}
```

**Erros:**
- `404 Not Found` - Cliente não encontrado

---

#### PUT /clientes/{id}
Atualiza dados do cliente.

**Request:**
```json
{
  "nome": "Maria Silva Santos",
  "cpf": "12345678909",
  "email": "maria.santos@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Comprar casa própria",
  "perfilRisco": "MODERADO"
}
```

**Response (200):** Cliente atualizado

---

#### DELETE /clientes/{id}
Remove cliente do sistema.

**Response (204):** No Content

---

### Diagnósticos

#### POST /diagnosticos
Gera diagnóstico automático baseado em score de risco.

**Request:**
```json
{
  "clienteId": 1,
  "scoreRisco": 45,
  "objetivosComplementares": "Investir em imóveis"
}
```

**Response (200):**
```json
{
  "id": 1,
  "cliente": {
    "id": 1,
    "nome": "Maria Silva"
  },
  "perfilRisco": "MODERADO",
  "scoreRisco": 45,
  "objetivos": "Aposentadoria tranquila; Investir em imóveis",
  "recomendacao": "Perfil moderado: diversifique entre renda fixa e variável",
  "dataGeracao": "2025-10-20T11:00:00"
}
```

**Cálculo de Perfil:**
- `scoreRisco ≤ 30` → **CONSERVADOR**
- `31 ≤ scoreRisco ≤ 60` → **MODERADO**
- `scoreRisco > 60` → **ARROJADO**

**Lógica Especial:**
- Se o perfil do cliente for maior que o calculado, prevalece o perfil do cliente

---

#### GET /diagnosticos/{clienteId}
Busca diagnóstico do cliente.

**Response (200):**
```json
{
  "id": 1,
  "perfilRisco": "MODERADO",
  "scoreRisco": 45,
  "recomendacao": "Perfil moderado: diversifique entre renda fixa e variável"
}
```

---

### Recomendações

#### POST /recomendacoes
Gera recomendação de carteira usando **Strategy Pattern**.

**Request:**
```json
{
  "clienteId": 1,
  "valorDisponivel": 50000.00,
  "objetivo": "Crescimento de médio prazo"
}
```

**Response (201):**
```json
{
  "id": 1,
  "cliente": {
    "id": 1,
    "nome": "Maria Silva"
  },
  "carteira": {
    "id": 2,
    "nome": "Carteira Moderada",
    "descricao": "Equilibrio entre crescimento e protecao",
    "retornoEsperado": 9.20,
    "riscoEstimado": 5.60
  },
  "valorInvestido": 50000.00,
  "objetivo": "Crescimento de médio prazo",
  "justificativa": "Estratégia ModeradaStrategy aplicada para perfil MODERADO",
  "dataRecomendacao": "2025-10-20T11:30:00"
}
```

**Estratégias Aplicadas:**
- **CONSERVADOR** → `ConservadoraStrategy` (6.5% retorno, 2.1% risco)
- **MODERADO** → `ModeradaStrategy` (9.2% retorno, 5.6% risco)
- **ARROJADO** → `AgressivaStrategy` (13.8% retorno, 9.4% risco)

---

#### GET /recomendacoes/{clienteId}
Lista histórico de recomendações do cliente.

**Response (200):**
```json
[
  {
    "id": 1,
    "carteira": {
      "nome": "Carteira Moderada",
      "retornoEsperado": 9.20
    },
    "valorInvestido": 50000.00,
    "dataRecomendacao": "2025-10-20T11:30:00"
  }
]
```

---

## Documentação Swagger

### Acessar a Documentação Interativa

1. **Inicie a aplicação**
2. **Abra o navegador** em: http://localhost:8080/swagger-ui.html

### Funcionalidades do Swagger
- **Testar endpoints** diretamente na interface
- **Ver schemas** de Request/Response
- **Autenticação JWT** integrada
- **Exemplos de uso** para cada endpoint
- **Códigos de resposta** documentados

---

## Tutorial Completo: Como Usar o Swagger UI

### Passo 1: Iniciar a Aplicação

```bash
# No terminal, execute:
.\mvnw spring-boot:run

# Aguarde até ver a mensagem:
# "Started OperumAdvisorApplication in X seconds"
```

### Passo 2: Acessar o Swagger UI

1. **Abra seu navegador** (Chrome, Firefox, Edge)
2. **Digite na barra de endereços:** `http://localhost:8080/swagger-ui.html`
3. **Pressione Enter**

Você verá a interface do Swagger com todos os endpoints organizados por tags:
```
auth-controller - Endpoints de autenticação
cliente-controller - Gestão de clientes
diagnostico-controller - Análise de perfil
recomendacao-controller - Recomendações de carteira
```

---

### Passo 3: Autenticar no Sistema (IMPORTANTE!)

#### 3.1. Expandir o Endpoint de Login
1. Localize a seção **`auth-controller`** (primeira da lista)
2. Clique em **`POST /auth/login`** para expandir
3. Você verá a descrição: *"Autentica o usuário com username e password..."*

#### 3.2. Testar o Endpoint de Login
1. Clique no botão **`Try it out`** (canto direito)
2. O campo de Request Body ficará editável
3. **Apague** o conteúdo existente
4. **Cole** o seguinte JSON:

```json
{
  "username": "operum",
  "password": "operum123"
}
```

#### 3.3. Executar a Requisição
1. Clique no botão azul **`Execute`** (abaixo do campo)
2. Aguarde 1-2 segundos
3. Role a página para baixo

#### 3.4. Copiar o Token JWT
Na seção **Responses**, você verá:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvcGVydW0iLCJleHAiOjE3Mjk2MjM0MDB9.Xe8k...",
  "type": "Bearer",
  "username": "operum",
  "expiresIn": 86400000
}
```

1. **Selecione** TODO o valor do campo `"token"` (sem as aspas)
2. **Copie** (Ctrl+C / Cmd+C)
   - O token é uma string longa como: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

#### 3.5. Autorizar o Swagger
1. **Role a página até o topo**
2. Localize o botão **`Authorize`** 🔒 (canto superior direito, verde)
3. **Clique** em **`Authorize`**
4. Uma janela modal será aberta

#### 3.6. Configurar o Bearer Token
Na janela "Available authorizations":

1. Localize o campo **`Value`** (abaixo de "bearerAuth (http, Bearer)")
2. **Digite**: `Bearer ` (com um espaço depois)
3. **Cole** o token copiado
4. O resultado final deve ser: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

```
┌─────────────────────────────────────────────┐
│ bearerAuth (http, Bearer)                   │
│                                             │
│ Value: Bearer eyJhbGciOiJIUzI1NiIsInR5cC... │
│        ↑                                    │
│        └─ Importante: "Bearer " + espaço    │
└─────────────────────────────────────────────┘
```

5. Clique em **`Authorize`** (botão na janela modal)
6. Clique em **`Close`** para fechar a janela

**Sucesso!** O cadeado 🔒 agora está **fechado**, indicando que você está autenticado.

---

### Passo 4: Cadastrar um Cliente

#### 4.1. Expandir o Endpoint
1. Localize a seção **`cliente-controller`**
2. Clique em **`POST /clientes`** para expandir
3. Clique em **`Try it out`**

#### 4.2. Preencher os Dados do Cliente
No campo **Request body**, substitua pelo JSON:

```json
{
  "nome": "João Silva",
  "cpf": "12345678909",
  "email": "joao.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria confortável",
  "perfilRisco": "MODERADO"
}
```

**Dicas:**
- CPF deve ter 11 dígitos (sem pontos ou traços)
- Email deve ser válido e único
- `consentimentoLgpd` deve ser `true` (obrigatório)
- `perfilRisco` aceita: `CONSERVADOR`, `MODERADO` ou `ARROJADO`

#### 4.3. Executar
1. Clique em **`Execute`**
2. Aguarde a resposta
3. Verifique o **Response code: 201** (Created)

#### 4.4. Conferir a Resposta
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-09",
  "email": "joao.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria confortável",
  "perfilRisco": "MODERADO",
  "dataCadastro": "2025-10-20T14:30:45"
}
```

**Anote o `"id": 1`** - Você vai precisar desse número nos próximos passos!

---

### Passo 5: Listar Todos os Clientes

#### 5.1. Testar o GET
1. Ainda em **`cliente-controller`**
2. Clique em **`GET /clientes`**
3. Clique em **`Try it out`**
4. Clique em **`Execute`**

#### 5.2. Ver Resultado
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "123.456.789-09",
    "email": "joao.silva@email.com",
    "perfilRisco": "MODERADO",
    "dataCadastro": "2025-10-20T14:30:45"
  }
]
```

---

### Passo 6: Gerar Diagnóstico de Perfil

#### 6.1. Expandir o Endpoint
1. Localize **`diagnostico-controller`**
2. Clique em **`POST /diagnosticos`**
3. Clique em **`Try it out`**

#### 6.2. Preencher o Score de Risco
```json
{
  "clienteId": 1,
  "scoreRisco": 55,
  "objetivosComplementares": "Investir em imóveis"
}
```

**📊 Como funciona o Score:**
- `scoreRisco ≤ 30` → Perfil **CONSERVADOR**
- `31 ≤ scoreRisco ≤ 60` → Perfil **MODERADO**
- `scoreRisco > 60` → Perfil **ARROJADO**

#### 6.3. Executar e Ver Resultado
```json
{
  "id": 1,
  "cliente": {
    "id": 1,
    "nome": "João Silva"
  },
  "perfilRisco": "MODERADO",
  "scoreRisco": 55,
  "objetivos": "Aposentadoria confortável; Investir em imóveis",
  "recomendacao": "Perfil moderado: diversifique entre renda fixa e variável",
  "dataGeracao": "2025-10-20T14:35:00"
}
```

---

### Passo 7: Solicitar Recomendação de Carteira

#### 7.1. Expandir o Endpoint
1. Localize **`recomendacao-controller`**
2. Clique em **`POST /recomendacoes`**
3. Clique em **`Try it out`**

#### 7.2. Informar Valor e Objetivo
```json
{
  "clienteId": 1,
  "valorDisponivel": 50000.00,
  "objetivo": "Crescimento de patrimônio em 5 anos"
}
```

#### 7.3. Ver a Carteira Recomendada
```json
{
  "id": 1,
  "cliente": {
    "id": 1,
    "nome": "João Silva"
  },
  "carteira": {
    "id": 2,
    "nome": "Carteira Moderada",
    "descricao": "Equilibrio entre crescimento e protecao",
    "retornoEsperado": 9.20,
    "riscoEstimado": 5.60
  },
  "valorInvestido": 50000.00,
  "objetivo": "Crescimento de patrimônio em 5 anos",
  "justificativa": "Estratégia ModeradaStrategy aplicada para perfil MODERADO",
  "dataRecomendacao": "2025-10-20T14:40:00"
}
```

** Strategy Pattern em ação!**
- Sistema automaticamente selecionou `ModeradaStrategy`
- Baseado no perfil MODERADO do cliente
- Retorno esperado: 9.20% ao ano
- Risco estimado: 5.60

---

### Passo 8: Consultar Histórico de Recomendações

#### 8.1. Ver Todas as Recomendações do Cliente
1. Em **`recomendacao-controller`**
2. Clique em **`GET /recomendacoes/{clienteId}`**
3. Clique em **`Try it out`**
4. No campo **`clienteId`**, digite: `1`
5. Clique em **`Execute`**

#### 8.2. Resultado
```json
[
  {
    "id": 1,
    "carteira": {
      "nome": "Carteira Moderada",
      "retornoEsperado": 9.20
    },
    "valorInvestido": 50000.00,
    "dataRecomendacao": "2025-10-20T14:40:00"
  }
]
```

---

### Passo 9: Testar Diferentes Perfis

#### 9.1. Cliente Conservador
Repita os passos 4-7 com estes dados:

**Cliente:**
```json
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria.santos@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Segurança financeira",
  "perfilRisco": "CONSERVADOR"
}
```

**Diagnóstico:**
```json
{
  "clienteId": 2,
  "scoreRisco": 20,
  "objetivosComplementares": "Reserva de emergência"
}
```

**Resultado Esperado:** Carteira Conservadora (6.5% retorno, 2.1% risco)

#### 9.2. Cliente Arrojado
**Cliente:**
```json
{
  "nome": "Carlos Mendes",
  "cpf": "11122233344",
  "email": "carlos.mendes@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Maximizar ganhos",
  "perfilRisco": "ARROJADO"
}
```

**Diagnóstico:**
```json
{
  "clienteId": 3,
  "scoreRisco": 85,
  "objetivosComplementares": "Aceito alta volatilidade"
}
```

**Resultado Esperado:** Carteira Arrojada (13.8% retorno, 9.4% risco)

---

### Passo 10: Entender os Códigos de Resposta

O Swagger mostra todos os possíveis retornos de cada endpoint:

#### Respostas de Sucesso ✅
- **200 OK** - Requisição bem-sucedida (GET, PUT)
- **201 Created** - Recurso criado (POST)
- **204 No Content** - Sucesso sem conteúdo (DELETE)

#### Respostas de Erro ❌
- **400 Bad Request** - Dados inválidos
  ```json
  {
    "timestamp": "2025-10-20T14:45:00",
    "status": 400,
    "error": "Bad Request",
    "message": "cpf: CPF inválido",
    "path": "/clientes"
  }
  ```

- **401 Unauthorized** - Token inválido/expirado
  ```json
  {
    "timestamp": "2025-10-20T14:45:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Credenciais inválidas",
    "path": "/auth/login"
  }
  ```

- **403 Forbidden** - Sem autorização
  ```
  Você esqueceu de adicionar o token JWT!
  Volte ao Passo 3 e configure o Bearer Token.
  ```

- **404 Not Found** - Recurso não existe
  ```json
  {
    "timestamp": "2025-10-20T14:45:00",
    "status": 404,
    "error": "Not Found",
    "message": "Cliente não encontrado",
    "path": "/clientes/999"
  }
  ```

---

### Passo 11: Explorar os Schemas

#### 11.1. Ver Estrutura de Dados
1. Role até o final da página do Swagger
2. Localize a seção **`Schemas`**
3. Expanda qualquer modelo (ex: `ClienteRequest`)

Você verá todos os campos, tipos e validações:
```
ClienteRequest {
  nome* (string, minLength: 3, maxLength: 100)
  cpf* (string, pattern: ^\d{11}$)
  email* (string, format: email)
  consentimentoLgpd* (boolean)
  objetivos (string, maxLength: 500)
  perfilRisco* (string, enum: [CONSERVADOR, MODERADO, ARROJADO])
}
```

**Legenda:**
- `*` = Campo obrigatório
- `minLength/maxLength` = Tamanho do texto
- `pattern` = Expressão regular (ex: CPF só aceita 11 dígitos)
- `enum` = Valores permitidos

---

### Passo 12: Testar Validações

#### 12.1. Teste 1: CPF Inválido
No **`POST /clientes`**, tente:
```json
{
  "nome": "Teste",
  "cpf": "123",
  "email": "teste@email.com",
  "consentimentoLgpd": true,
  "perfilRisco": "MODERADO"
}
```

**Resultado:** 400 Bad Request - "CPF inválido"

#### 12.2. Teste 2: Email Duplicado
Tente cadastrar o mesmo email duas vezes.

**Resultado:** 422 Unprocessable Entity - "Email já cadastrado"

#### 12.3. Teste 3: Sem Consentimento LGPD
```json
{
  "nome": "Teste",
  "cpf": "12345678909",
  "email": "teste@email.com",
  "consentimentoLgpd": false,
  "perfilRisco": "MODERADO"
}
```

**Resultado:** 400 Bad Request - "Consentimento LGPD é obrigatório"

---

### Passo 13: Atualizar um Cliente

#### 13.1. Usar o PUT
1. Localize **`PUT /clientes/{id}`**
2. Clique em **`Try it out`**
3. No campo **`id`**, digite: `1`
4. No Request body:

```json
{
  "nome": "João Silva Santos",
  "cpf": "12345678909",
  "email": "joao.silva@email.com",
  "consentimentoLgpd": true,
  "objetivos": "Aposentadoria e comprar casa",
  "perfilRisco": "ARROJADO"
}
```

**Mudança:** Perfil alterado de MODERADO para ARROJADO

5. Clique em **`Execute`**

#### 13.2. Verificar Atualização
Use **`GET /clientes/1`** para confirmar a mudança.

---

### Passo 14: Deletar um Cliente

#### 14.1. Usar o DELETE
1. Localize **`DELETE /clientes/{id}`**
2. Clique em **`Try it out`**
3. No campo **`id`**, digite: `1`
4. Clique em **`Execute`**

**Resultado:** 204 No Content (sem corpo de resposta)

#### 14.2. Confirmar Exclusão
Use **`GET /clientes/1`** - deve retornar **404 Not Found**

---

### Dicas Avançadas

#### Dica 1: Exportar para cURL
1. Depois de executar qualquer endpoint
2. Localize a seção **`curl`** na resposta
3. Copie o comando completo
4. Cole no terminal para executar fora do Swagger

Exemplo:
```bash
curl -X 'POST' \
  'http://localhost:8080/clientes' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGc...' \
  -H 'Content-Type: application/json' \
  -d '{"nome":"João Silva","cpf":"12345678909",...}'
```

#### Dica 2: Token Expirado?
Se receber erro **401** ou **403**:
1. Volte ao **`POST /auth/login`**
2. Gere um novo token (válido por 24h)
3. Atualize o Bearer Token no botão **`Authorize`**

#### Dica 3: Limpar Dados de Teste
Para recomeçar do zero:
```bash
# No terminal MySQL
mysql -u root -p
DROP DATABASE operum;
CREATE DATABASE operum;
EXIT;

# Reinicie a aplicação
.\mvnw spring-boot:run
```

#### Dica 4: Ver Request/Response Completo
Na resposta do Swagger, expanda:
- **Request URL** - Mostra a URL exata chamada
- **Server response** - Mostra headers e body completo
- **Response headers** - Mostra Content-Type, Security headers, etc.

---

### Fluxo Completo Resumido

```
1. Login → Copiar Token
2. Authorize → Colar Token com "Bearer "
3. POST /clientes → Criar cliente (guardar ID)
4. POST /diagnosticos → Analisar perfil (usar ID do cliente)
5. POST /recomendacoes → Gerar carteira (usar ID do cliente)
6. GET /recomendacoes/{clienteId} → Ver histórico
```

---

### Troubleshooting no Swagger

| Problema | Solução |
|----------|---------|
| **"Failed to fetch"** | Aplicação não está rodando. Execute `.\mvnw spring-boot:run` |
| **403 Forbidden** | Token não configurado ou expirado. Refaça o Passo 3 |
| **Botão Execute não funciona** | Dê refresh na página (F5) |
| **Schemas não aparecem** | Aguarde 30 segundos após iniciar a aplicação |
| **Token expira rápido** | Token dura 24h. Verifique a data/hora do seu PC |

---

### Interface do Swagger Explicada

```
┌─────────────────────────────────────────────────────────────┐
│  Operum Advisor API                            [Authorize 🔒]│ ← Botão de autenticação
├─────────────────────────────────────────────────────────────┤
│  Servers: http://localhost:8080                              │
├─────────────────────────────────────────────────────────────┤
│  📁 auth-controller                                          │
│     ├─ POST /auth/login        [Autentica usuário]          │ ← Clique para expandir
│                                                              │
│  📁 cliente-controller                                       │
│     ├─ GET    /clientes        [Lista todos]                │
│     ├─ POST   /clientes        [Cadastra novo]              │
│     ├─ GET    /clientes/{id}   [Busca específico]           │
│     ├─ PUT    /clientes/{id}   [Atualiza dados]             │
│     └─ DELETE /clientes/{id}   [Remove cliente]             │
│                                                              │
│  📁 diagnostico-controller                                   │
│     ├─ POST /diagnosticos      [Gera diagnóstico]           │
│     └─ GET  /diagnosticos/{clienteId}                        │
│                                                              │
│  📁 recomendacao-controller                                  │
│     ├─ POST /recomendacoes     [Gera recomendação]          │
│     └─ GET  /recomendacoes/{clienteId}                       │
└─────────────────────────────────────────────────────────────┘
```

---

## Testes Automatizados

### Executar Todos os Testes
```bash
# Windows
.\mvnw test

# Linux/Mac
./mvnw test
```

### Tipos de Testes Implementados

#### 1. Testes Unitários (41 testes)
- **ClienteServiceImplTest** (15 testes)
  - Cadastro, busca, atualização e exclusão
  - Validações de CPF e email
  - Tratamento de exceções

- **DiagnosticoServiceImplTest** (10 testes)
  - Cálculo automático de perfil de risco
  - Combinação de objetivos
  - Lógica de score

- **RecomendacaoServiceImplTest** (9 testes)
  - Strategy Pattern com dispatch dinâmico
  - Validação de carteiras
  - Persistência correta

- **CarteiraSelectionStrategyTest** (6 testes)
  - Todas as 3 estratégias
  - Polimorfismo validado

- **DemoApplicationTests** (1 teste)
  - Context loading

#### 2. Testes de Integração (8 testes)
- **AuthControllerIntegrationTest** (8 testes)
  - Login com credenciais válidas → 200 + JWT
  - Credenciais inválidas → 401
  - Usuário inexistente → 401
  - Acesso sem token → 403
  - Token inválido → 403
  - Fluxo completo de autenticação
  - Endpoint público acessível
  - Validação de campos obrigatórios

### Resultado dos Testes
```
49 testes executados
0 falhas
0 erros
Tempo de execução: ~22 segundos
```

---

## Segurança e LGPD

### Implementações de Segurança

#### 1. Autenticação JWT
- Tokens assinados com HMAC-SHA256
- Expiração configurável (24 horas)
- Stateless (não usa sessão)
- Validação automática em cada requisição

#### 2. Criptografia de Dados
- **AES-256-GCM** para dados sensíveis
- Campos criptografados:
  - Nome do cliente
  - Email
  - Justificativas de recomendação
- Descriptografia transparente via JPA

#### 3. Validações LGPD
- **Consentimento obrigatório** no cadastro
- **Direito ao esquecimento** (DELETE /clientes)
- **Transparência** nos dados coletados
- **Minimização** de dados
- **Finalidade específica** para cada campo

#### 4. Boas Práticas
- Senhas com BCrypt (12 rounds)
- Exception handler centralizado
- Validações de entrada em todos os endpoints
- Logs auditáveis
- Headers de segurança HTTP

---

## Diagramas
### Arquitetura em Camadas
```mermaid
flowchart TD
    A[Cliente HTTP / Postman] --> B[Controllers REST]
    B --> C[Services]
    C --> D[Repositories]
    D --> E[(MySQL 8)]
    C --> F[EncryptionService]
    B --> G[GlobalExceptionHandler]
```

### Diagrama ER Simplificado
```mermaid
erDiagram
    CLIENTE ||--o| DIAGNOSTICO : possui
    CLIENTE ||--o{ RECOMENDACAO : recebe
    RECOMENDACAO }o--|| CARTEIRA : sugere
    CARTEIRA ||--o{ ITEM_CARTEIRA : contem
    ITEM_CARTEIRA }o--|| ATIVO : referencia
```

### Casos de Uso Principais
```mermaid
flowchart LR
    user(Investidor) --> UC1[Cadastrar Cliente]
    user --> UC2[Gerar Diagnostico]
    user --> UC3[Gerar Recomendacao]
    UC3 --> UC4[Consultar Recomendacoes]
```

---

## 🗄️ Migrações do Banco de Dados

### V1__create_schema.sql
Cria toda a estrutura de tabelas:
- `cliente` - Dados do investidor
- `diagnostico` - Análise de perfil
- `recomendacao` - Sugestões personalizadas
- `carteira` - Carteiras modelo
- `ativo` - Ativos financeiros
- `item_carteira` - Composição de carteiras

### V2__seed_data.sql
Popula dados iniciais:
- **5 Ativos** de teste (CDB, Tesouro IPCA, FII, Ações, ETF)
- **3 Carteiras** modelo (Conservadora, Moderada, Arrojada)
- **Composições** de cada carteira com percentuais

---

## Exemplos de Uso Completo

### Cenário 1: Investidor Conservador

```bash
# 1. Fazer login e obter token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "operum",
    "password": "operum123"
  }'

# Resposta: { "token": "eyJhbGc...", ... }

# 2. Cadastrar cliente conservador
curl -X POST http://localhost:8080/clientes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Santos",
    "cpf": "98765432100",
    "email": "joao@email.com",
    "consentimentoLgpd": true,
    "objetivos": "Preservar capital",
    "perfilRisco": "CONSERVADOR"
  }'

# Resposta: { "id": 1, ... }

# 3. Gerar diagnóstico (score baixo)
curl -X POST http://localhost:8080/diagnosticos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "scoreRisco": 25,
    "objetivosComplementares": "Reserva de emergência"
  }'

# Resposta: { "perfilRisco": "CONSERVADOR", "scoreRisco": 25, ... }

# 4. Solicitar recomendação
curl -X POST http://localhost:8080/recomendacoes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "valorDisponivel": 30000.00,
    "objetivo": "Segurança financeira"
  }'

# Resposta: { "carteira": { "nome": "Carteira Conservadora", "retornoEsperado": 6.5 }, ... }
```

### Cenário 2: Investidor Arrojado

```bash
# 1. Login (mesmo processo)

# 2. Cadastrar cliente arrojado
curl -X POST http://localhost:8080/clientes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carla Mendes",
    "cpf": "11122233344",
    "email": "carla@email.com",
    "consentimentoLgpd": true,
    "objetivos": "Maximizar retorno",
    "perfilRisco": "ARROJADO"
  }'

# 3. Gerar diagnóstico (score alto)
curl -X POST http://localhost:8080/diagnosticos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 2,
    "scoreRisco": 85,
    "objetivosComplementares": "Crescimento agressivo"
  }'

# Resposta: { "perfilRisco": "ARROJADO", "scoreRisco": 85, ... }

# 4. Solicitar recomendação
curl -X POST http://localhost:8080/recomendacoes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 2,
    "valorDisponivel": 100000.00,
    "objetivo": "Crescimento agressivo"
  }'

# Resposta: { "carteira": { "nome": "Carteira Arrojada", "retornoEsperado": 13.8 }, ... }
```

---

## Troubleshooting

### Problema: Erro de conexão com MySQL
**Solução:**
```bash
# Verifique se o MySQL está rodando
sudo service mysql status  # Linux
# ou
net start MySQL80  # Windows

# Teste a conexão manual
mysql -u root -p -e "SELECT 1;"
```

### Problema: "Table doesn't exist"
**Solução:**
```bash
# Flyway não executou. Force a recriação:
mysql -u root -p
DROP DATABASE operum;
CREATE DATABASE operum;
EXIT;

# Reinicie a aplicação
.\mvnw spring-boot:run
```

### Problema: Token JWT inválido (403)
**Solução:**
1. Verifique se o token não expirou (24h)
2. Certifique-se de usar `Bearer ` antes do token
3. Gere um novo token via `/auth/login`

### Problema: Erro de criptografia
**Solução:**
```properties
# Verifique se a chave está configurada em application.properties
app.encryption.secret=operum-secret-key
```

### Problema: Testes falhando
**Solução:**
```bash
# Limpe o cache e recompile
.\mvnw clean test -U
```

---

## Licença

Este projeto é um protótipo educacional desenvolvido para a disciplina de **Arquitetura de Software** da **FIAP**.

**Uso:** Apenas para fins acadêmicos e demonstração de conceitos.

---

## Referências Técnicas

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Strategy Pattern - Gang of Four](https://refactoring.guru/design-patterns/strategy)
- [LGPD - Lei Geral de Proteção de Dados](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm)
- [REST API Best Practices](https://restfulapi.net/)
- [JaCoCo Code Coverage](https://www.jacoco.org/jacoco/trunk/doc/)

---
