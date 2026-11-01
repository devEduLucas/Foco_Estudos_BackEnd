# Foco nos Estudos — Back-end

API REST desenvolvida com **Java e Spring Boot** para uma plataforma de organização de estudos. O sistema permite gerenciar matérias, tarefas e acompanhar o progresso por meio de resumos e indicadores.

O projeto utiliza autenticação baseada em JWT, persistência de dados com Spring Data JPA e arquitetura organizada para separação de responsabilidades.

## Tecnologias

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Web** — desenvolvimento de APIs REST.
- **Spring Security** — autenticação e autorização.
- **Spring Data JPA** — persistência e gerenciamento de dados.
- **Hibernate** — mapeamento objeto-relacional (ORM).
- **MySQL** — banco de dados relacional.
- **H2 Database** — banco em memória para testes.
- **JWT (JSON Web Token)** — autenticação baseada em tokens.
- **Bean Validation** — validação de dados.
- **Lombok** — redução de código repetitivo.
- **Maven** — gerenciamento de dependências e build.

## Funcionalidades

### Autenticação e segurança
- Cadastro de usuários.
- Autenticação com e-mail e senha.
- Geração e validação de tokens JWT.
- Proteção de endpoints utilizando Spring Security.
- Controle de acesso por meio de autenticação baseada em tokens.

### Gerenciamento de matérias
- Cadastro de matérias.
- Listagem de matérias.
- Consulta individual por identificador.
- Atualização de informações.
- Exclusão de matérias.
- Validação de regras de negócio para impedir exclusões inconsistentes.

### Gerenciamento de tarefas
- Criação de tarefas.
- Listagem e consulta de tarefas.
- Atualização de informações.
- Exclusão de tarefas.
- Atualização do status de conclusão.
- Filtros por matéria e status.

### Resumos e indicadores
- Resumo geral das tarefas.
- Quantidade de tarefas por status.
- Identificação de tarefas atrasadas.
- Resumo semanal das atividades.

## Arquitetura

O projeto utiliza uma arquitetura em camadas, separando as responsabilidades da aplicação.

```text
Cliente (Front-end)
       |
       v
    API REST
       |
       v
   Controllers
       |
       v
    Services
       |
       v
   Repositories
       |
       v
   Spring Data JPA
       |
       v
      MySQL
```

Essa organização facilita a manutenção, reutilização de código e separação das regras de negócio.

## Endpoints da API

Base URL local: `http://localhost:8080/api`

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/cadastro` | Cadastro de usuário |
| POST | `/auth/login` | Autenticação e geração de JWT |

### Matérias

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/materias` | Cadastrar matéria |
| GET | `/materias` | Listar matérias |
| GET | `/materias/{id}` | Buscar matéria |
| PUT | `/materias/{id}` | Atualizar matéria |
| DELETE | `/materias/{id}` | Excluir matéria |

### Tarefas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/tarefas` | Criar tarefa |
| GET | `/tarefas` | Listar tarefas |
| GET | `/tarefas/{id}` | Buscar tarefa |
| PUT | `/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/tarefas/{id}` | Excluir tarefa |
| PATCH | `/tarefas/{id}/status` | Atualizar status |

### Resumos

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/resumo` | Resumo geral |
| GET | `/resumo/semanal` | Resumo semanal |

**Observação:** os endpoints protegidos exigem o envio do token JWT no cabeçalho:

```http
Authorization: Bearer SEU_TOKEN_JWT
```

## Como executar o projeto

### Pré-requisitos

- Java JDK 21.
- MySQL Server.
- Git.

### 1. Clonar o repositório

```bash
git clone https://github.com/devEduLucas/Foco_Estudos_BackEnd.git

cd Foco_Estudos_BackEnd
```

### 2. Configurar o banco de dados

Crie um banco de dados MySQL:

```sql
CREATE DATABASE foco_estudos;
```

### 3. Configurar as variáveis de ambiente

A aplicação utiliza variáveis de ambiente para configurar a conexão com o banco de dados e a autenticação.

No PowerShell, configure:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/foco_estudos"
$env:DB_USER="root"
$env:DB_PASSWORD="SUA_SENHA_MYSQL"
$env:JWT_SECRET="SUA_CHAVE_SECRETA_COM_NO_MINIMO_32_CARACTERES"
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173"
```

**Importante:** substitua os valores de exemplo pelas suas próprias configurações. Não compartilhe credenciais ou chaves secretas.

### 4. Executar a aplicação

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

### 5. Executar os testes

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

Os testes utilizam o banco H2 em memória, dispensando uma conexão com o MySQL durante sua execução.

## Tratamento de erros

A API utiliza respostas padronizadas para situações como:

- `400 Bad Request` — dados inválidos ou violações de regras de negócio.
- `401 Unauthorized` — autenticação ausente ou inválida.
- `404 Not Found` — recurso não encontrado.
- `409 Conflict` — conflito com o estado atual do recurso.

## Integração

Este back-end foi desenvolvido para trabalhar em conjunto com a aplicação front-end:

**Repositório Front-end:**  
https://github.com/devEduLucas/Foco_Estudos_FrontEnd

## Objetivos técnicos

- Desenvolvimento de APIs RESTful.
- Aplicação de arquitetura em camadas.
- Implementação de autenticação e autorização com JWT.
- Persistência de dados com Spring Data JPA.
- Modelagem de entidades e relacionamentos.
- Validação de dados e regras de negócio.
- Tratamento de exceções.
- Testes automatizados.
- Integração entre front-end e back-end.

---

Desenvolvido por **Eduardo Lucas**.

[GitHub](https://github.com/devEduLucas)
