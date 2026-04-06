# Project Status

_Last updated: 2026-04-07._

| Area | Status |
|---|---|
| Domain entities | 13 of 13 implemented — persistence layer only |
| Repositories | Not started — no Spring Data JPA repository exists yet |
| Services / business logic | All 8 service interfaces now have an `@Service` implementation in `service.impl` (`AuthServiceImpl`, `UserServiceImpl`, `ProjectServiceImpl`, `ProjectMemberServiceImpl`, `FileServiceImpl`, `PlanServiceImpl`, `SubscriptionServiceImpl`, `UsageServiceImpl`) — **but every method is a stub** (returns `null`, an empty list, or does nothing). The app starts now, but no endpoint does anything real yet. |
| REST APIs | 6 controllers implemented (auth, projects, project members, project files, billing, usage — see [APIs](api/README.md#apis) below); every endpoint now resolves to a real (stub) service bean, so the app boots, but responses are effectively empty/`null` until the stubs are replaced with real logic. All still hardcode `Long userId = 1L` in place of a real authenticated principal. |
| Database | PostgreSQL datasource configured (`application.yaml`), `ddl-auto: update` — schema is created/updated automatically on startup. Credentials are externalized via `${DB_USERNAME:User}`/`${DB_PASSWORD:Password}` placeholders (env var, falling back to the local dev default) rather than hardcoded, since this repo is public. No migration tool (Flyway/Liquibase) yet. |
| Tests | Only the generated `contextLoads` smoke test |

**Next up:** replace the service stubs with real logic — starting with `AuthServiceImpl`/`UserServiceImpl` (password hashing, token generation, user lookup — needs a `UserRepository`, which doesn't exist yet), since nothing else is reachable without real login. Real authentication should also replace the `userId = 1L` placeholder in every controller.
