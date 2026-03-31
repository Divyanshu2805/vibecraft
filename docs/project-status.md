# Project Status

_Last updated: 2026-03-30._

| Area | Status |
|---|---|
| Domain entities | 13 of 13 implemented — persistence layer only (one, `Preview`, is missing its JPA annotations — see [Known gaps](schema/README.md#known-gaps--deviations-from-v1-design)) |
| Repositories | Not started |
| Services / business logic | Not started |
| REST APIs | Not started |
| Database | No datasource configured, no migrations — schema would be Hibernate-auto-generated once a datasource is added |
| Tests | Only the generated `contextLoads` smoke test |

**Next up:** wire a PostgreSQL datasource, finish `Preview`'s JPA annotations, then repository/service layers.
