# Foco nos Estudos — API (Spring Boot)

API REST para organizar estudos: usuários, matérias e tarefas, com resumo geral e semanal.
Java 17+, Spring Boot 3.3, Spring Security (JWT), Spring Data JPA, H2 em arquivo.

## Executar
```bash
mvn spring-boot:run          # sobe em http://localhost:8080 ; banco em ./data/foco
mvn test                     # testes unitários + integração (H2 em memória)
```
Variáveis opcionais: `JWT_SECRET` (>= 32 caracteres), `CORS_ALLOWED_ORIGINS` (separados por vírgula).

## Endpoints
Públicos: `POST /auth/cadastro`, `POST /auth/login` → `{token, tipo, usuarioId, nome}`.
Demais exigem `Authorization: Bearer <token>`:

| Método | Rota | Obs. |
|---|---|---|
| POST/GET | `/materias` | |
| GET/PUT/DELETE | `/materias/{id}` | DELETE → 409 se houver tarefas |
| POST/GET | `/tarefas` | GET aceita `?materiaId=&status=` |
| GET/PUT/DELETE | `/tarefas/{id}` | |
| PATCH | `/tarefas/{id}/status` | corpo `{"status":"CONCLUIDA"}` |
| GET | `/resumo` | totais por status + atrasadas |
| GET | `/resumo/semanal` | tarefas com prazo na semana (seg–dom) |

Erros: `{status, mensagem, detalhes[], timestamp}` — 400 validação/regra, 401 sem/ruim token ou login, 404, 409.
