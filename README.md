FocoEstudos — Backend

API REST do FocoEstudos, plataforma para estudantes organizarem matérias, tarefas e prazos acadêmicos.

Frontend (repositório separado): <URL_DO_REPOSITORIO_FRONTEND>

Status: em desenvolvimento. Itens marcados como (planejado) ainda não foram implementados.

Tecnologias
Java 21
Spring Boot 3.5.x (Web, Data JPA, Validation, Security)
MySQL 8.0.16 ou superior
Maven (wrapper incluído)
Lombok
JWT (jjwt) e BCrypt (planejado)
Flyway (planejado)
JUnit e Mockito
Swagger/OpenAPI (planejado)
Arquitetura
src/main/java/com/br/eduardo/focoestudos/
├── FocoEstudosApplication.java
├── config/            CORS, segurança e Clock
├── security/          JwtService, JwtAuthenticationFilter
├── controller/        Entrada HTTP; sem regra de negócio
├── service/           Regras de negócio e transações
├── database/
│   ├── model/         Entidades JPA
│   └── repository/    Interfaces Spring Data JPA
├── dto/
│   ├── request/       Dados recebidos pela API
│   └── response/      Dados devolvidos pela API
├── enums/             StatusTarefa, PrioridadeTarefa
├── exception/         Exceções de domínio
└── handler/           GlobalExceptionHandler (@RestControllerAdvice)

Regras: injeção de dependência por construtor; entidades nunca são expostas pela API; validação de entrada com Bean Validation e regras de negócio nos services.

Pré-requisitos
JDK 21
MySQL 8.0.16 ou superior
Configuração
1. Banco de dados

Execute o script src/main/resources/db/migration/V1__criar_tabelas.sql para criar o banco focoestudos e as tabelas usuarios, materias e tarefas.

Quando o Flyway estiver ativo, ele aplica esse script sozinho: não o execute manualmente no mesmo banco.

Crie um usuário próprio para a aplicação, sem usar o root.

2. Variáveis de ambiente
Variável	Descrição
DB_URL	URL JDBC. Padrão: jdbc:mysql://localhost:3306/focoestudos
DB_USER	Usuário do banco
DB_PASSWORD	Senha do banco
JWT_SECRET	Segredo de assinatura do token (planejado)

Nunca envie credenciais ao repositório.

3. application.properties
properties
spring.application.name=focoestudos
server.servlet.context-path=/api

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/focoestudos}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
Executando

Linux e macOS:

bash
./mvnw spring-boot:run

Windows:

bash
mvnw.cmd spring-boot:run

A API fica disponível em http://localhost:8080/api.

Testes
bash
./mvnw test

Os services são testados com Mockito. Cobrem as regras de atraso, resumo semanal, dono do recurso, exclusão bloqueada por tarefas e e-mail duplicado.

Endpoints (planejado)

Todas as rotas, exceto /auth/**, exigem Authorization: Bearer <token>.

Método	Rota	Descrição
POST	/auth/cadastro	Cria conta. 409 se o e-mail já existe
POST	/auth/login	Retorna {token}. 401 se as credenciais são inválidas
GET	/materias	Lista matérias com totalTarefas e tarefasPendentes
POST	/materias	Cria matéria. 409 se o nome já existe para o usuário
PUT	/materias/{id}	Atualiza matéria
DELETE	/materias/{id}	Exclui matéria. 409 se houver tarefas
GET	/tarefas	Lista com filtros: status, prioridade, materiaId, de, ate, atrasadas
GET	/tarefas/hoje	Tarefas de hoje e atrasadas
GET	/tarefas/proximos-prazos?dias=7	Próximos prazos
GET	/tarefas/{id}	Consulta tarefa
POST	/tarefas	Cria tarefa
PUT	/tarefas/{id}	Atualiza tarefa
PATCH	/tarefas/{id}/status	Atualiza apenas o status
DELETE	/tarefas/{id}	Exclui tarefa
GET	/resumo	Contagem por status e atrasadas
GET	/resumo/semana	Tarefas planejadas e concluídas na semana

Exemplo de corpo para POST /tarefas:

json
{
  "titulo": "Entregar lista de exercícios",
  "descricao": null,
  "dataPrazo": "2026-11-05T18:00:00",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "materiaId": 1
}
Regras de negócio
Cada usuário acessa apenas os próprios dados. Recurso de outro usuário retorna 404.
Nome de matéria é único por usuário.
Matéria com tarefas não pode ser excluída.
Tarefa atrasada: prazo anterior ao momento atual e status diferente de CONCLUIDA.
Semana: de segunda a domingo. planejadas são as tarefas com prazo na semana; concluidas são as planejadas com status CONCLUIDA.
tarefasPendentes de uma matéria conta apenas o status PENDENTE.
Formato de erro
json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "campos": { "titulo": "Informe o título." },
  "timestamp": "2026-10-29T10:00:00"
}

campos aparece apenas em erros de validação. Códigos usados: 400, 401, 404, 409 e 500.

Roadmap
 Estrutura de pacotes e script do banco
 Setup do Spring Boot, MySQL e Flyway
 Cadastro, login e JWT
 CRUD de matérias
 CRUD de tarefas e filtros
 Endpoints de resumo
 CORS, testes automatizados e OpenAPI
 Deploy
