# Project Status

_Last updated: 2026-04-26._

| Area | Status |
|---|---|
| Domain entities | 10 entity types (see [Entities / Models](schema/README.md#entities--models)) — the design was simplified since v2: project ownership moved back onto `Project.owner` (direct FK), `ProjectRole` is a plain `EDITOR`/`VIEWER` enum again (no permission-set model), `ChatMessage` stores tool calls as a `toolCalls` JSON string again (the `ChatEvent` child-entity design was removed), and `UsageLog` is a per-action audit row again (not a daily counter). |
| Repositories | 2 exist: `ProjectRepository` (`findAllAccessibleByUser`), `UserRepository` (CRUD only, no custom queries yet). The other 8 entities have no repository yet. |
| Mapper | `ProjectMapper` (MapStruct) — `Project` → `ProjectResponse`/`ProjectSummaryResponse`. No mapper for any other entity yet. |
| Error handling | `GlobalExceptionHandler` (`@RestControllerAdvice`) exists but only handles one exception type (`ResourceNotFoundException` → 404 `ApiError`). Bean Validation failures (`MethodArgumentNotValidException`) aren't caught by it yet, so they still fall through to Spring's default error response shape. |
| Services / business logic | `ProjectServiceImpl.createProject`/`getUserProjects` have real logic now (the first non-stub service methods in the project). Every other method across all 8 services is still a stub (`null`/empty/no-op). |
| REST APIs | 6 controllers implemented — see [APIs](api/README.md#apis) below. All still hardcode `Long userId = 1L` in place of a real authenticated principal. |
| Database | PostgreSQL datasource configured locally (gitignored from this repo's history — not committed, since it previously held plaintext credentials; kept as an uncommitted local file plus a local `data.sql` seed). `ddl-auto: update`. |
| Tests | Only the generated `contextLoads` smoke test |

**Next up:** replace the remaining service stubs with real logic — `AuthServiceImpl`/`UserServiceImpl` first (password hashing, token generation), since nothing else is reachable without real login. Add a `MethodArgumentNotValidException` handler to `GlobalExceptionHandler` so validation failures return the same `ApiError` shape as everything else. Real authentication should also replace the `userId = 1L` placeholder in every controller.
