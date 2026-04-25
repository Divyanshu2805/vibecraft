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

_Last updated: 2026-04-26 (v3 entity schema: simplified ownership/roles/chat/usage designs, restored JPA annotations after an external revert; added repository, MapStruct mapper, and global exception handling layers; first real service logic in `ProjectServiceImpl`; `application.yaml`/`data.sql` are now committed; `ProjectServiceImpl` completed — `getUserProjectById`, `updateProject`, `softDelete` implemented alongside a new `findAccessibleProjectById` repository query and a `ForbiddenException`/403 handler; `ProjectMemberServiceImpl` completed — all 4 methods implemented alongside new `ProjectMemberRepository`/`ProjectMemberMapper`/`UserRepository.findByEmail`, `ProjectRole.OWNER` re-added, `data.sql` now seeds 3 users; `GlobalExceptionHandler` gained a `MethodArgumentNotValidException` → 400 handler, closing the last "Next up" item from the previous pass; `LoginRequest.password` gained an undocumented, message-less `@Size(min = 8)`; **v4 schema**: `Project.owner` FK removed in favor of `ProjectMember.projectRole == OWNER`, dropping every owner-only authorization check in the process (flagged as the top "Known gap"); `User.email`/`passwordHash` renamed back to `username`/`password`, `avatarUrl` dropped, propagated throughout; `data.sql` seeds `password = 'N/A'`; local `ddl-auto` switched to `create`; **JWT auth landed**: `spring-boot-starter-security` + JJWT added, new `security` package (`WebSecurityConfig`/`JwtAuthFilter`/`AuthUtil`/`JwtUserPrincipal`), `User implements UserDetails`, `AuthServiceImpl.signup`/`login` fully implemented (BCrypt hashing, real JWTs — though `signup`'s response bug returns the literal string `"dummy"` instead of the generated token), `UserServiceImpl` implements `UserDetailsService`; every controller/service dropped its explicit `userId` parameter in favor of `AuthUtil.getCurrentUserId()`; `ProjectRepository`'s two accessibility queries rewritten as `EXISTS` subqueries (fixing an entity-name typo introduced in that rewrite); `data.sql` deleted (real signup replaces the seed), `ddl-auto` reverted to `update`; the JWT secret briefly had no env-var fallback at all — caught and fixed (`${JWT_SECRET:...}`) before reaching `main`)_
