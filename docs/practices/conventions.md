# Practices / Conventions

- Maven Wrapper (`mvnw`/`mvnw.cmd`) used instead of a system Maven install.
- Empty POM overrides (`name`, `description`, `url`, `license`, `developers`, `scm`) to avoid inheriting values from `spring-boot-starter-parent`.
- Lombok wired as an annotation processor in both compile and test-compile Maven executions.
- Local dev history tracked in `DEVLOG.md` (gitignored); this file is the tracked, public-facing project doc.
