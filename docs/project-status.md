# Project Status

_Last updated: 2026-04-07._

| Area | Status |
|---|---|
| Domain entities | 13 of 13 implemented — persistence layer only |
| Repositories | Not started — no Spring Data JPA repository exists yet |
| Services / business logic | 8 service interfaces defined (`AuthService`, `UserService`, `ProjectService`, `ProjectMemberService`, `ProjectFileService`, `PlanService`, `SubscriptionService`, `UsageService`) — **none have an implementation**, so the app can't actually start once a datasource exists (`No qualifying bean`) |
| REST APIs | 6 controllers implemented (auth, projects, project members, project files, billing, usage — see [APIs](api/README.md#apis) below) — all delegate to the unimplemented services above, and all hardcode `Long userId = 1L` in place of a real authenticated principal |
| Database | No datasource configured, no migrations — schema would be Hibernate-auto-generated once a datasource is added |
| Tests | Only the generated `contextLoads` smoke test |

**Next up:** wire a PostgreSQL datasource, then implement the service layer (at minimum `AuthService`/`UserService`, since nothing else is reachable without login) and real authentication in place of the `userId = 1L` placeholder.
