# VibeCraft — Project Documentation

Living documentation of the project, kept current with each commit. Reference for APIs, entities, tech stack, and practices used — useful for resume/portfolio write-ups later.

## Contents

- [Overview](overview.md) - What VibeCraft is and who it is for
- [Tech Stack](tech-stack.md) - Languages, frameworks and libraries, and why each is here
- [Architecture](architecture/README.md) - How the system is put together, request by request
- [API](api/README.md) - Every endpoint, its request and response shapes, and errors
- [Schema](schema/README.md) - Entities, tables, enums and schema conventions
- [Practices](practices/README.md) - Conventions, gotchas, security guardrails, testing, definition of done
- [Project Status](project-status.md) - What is built and what is next

## Related docs

- [README.md](../README.md) — public-facing project overview and setup instructions.
- [CLAUDE.md](../CLAUDE.md) — instructions for AI coding assistants working in this repo.

---

_Last updated: 2026-04-26 (v3 entity schema: simplified ownership/roles/chat/usage designs, restored JPA annotations after an external revert; added repository, MapStruct mapper, and global exception handling layers; first real service logic in `ProjectServiceImpl`; `application.yaml`/`data.sql` are now committed; `ProjectServiceImpl` completed — `getUserProjectById`, `updateProject`, `softDelete` implemented alongside a new `findAccessibleProjectById` repository query and a `ForbiddenException`/403 handler; `ProjectMemberServiceImpl` completed — all 4 methods implemented alongside new `ProjectMemberRepository`/`ProjectMemberMapper`/`UserRepository.findByEmail`, `ProjectRole.OWNER` re-added, `data.sql` now seeds 3 users)_
