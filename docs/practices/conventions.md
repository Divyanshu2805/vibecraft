# Practices / Conventions

- Maven Wrapper (`mvnw`/`mvnw.cmd`) used instead of a system Maven install.
- Empty POM overrides (`name`, `description`, `url`, `license`, `developers`, `scm`) to avoid inheriting values from `spring-boot-starter-parent`.
- Lombok wired as an annotation processor in both compile and test-compile Maven executions; entities consistently use `@Getter @Setter @FieldDefaults(level = AccessLevel.PRIVATE) @AllArgsConstructor @NoArgsConstructor @Builder`.
- `@CreationTimestamp`/`@UpdateTimestamp` (Hibernate) for automatic `createdAt`/`updatedAt` timestamps, instead of manual `@PrePersist`/`@PreUpdate` methods.
- Composite primary keys (`ProjectMember`, `ChatSession`) modeled via `@EmbeddedId` + `@MapsId`, with a small dedicated `@Embeddable`, `Serializable` ID class (`ProjectMemberId`, `ChatSessionId`) per entity — each carries `@EqualsAndHashCode` over its fields, required by the JPA spec for composite keys to behave correctly in the persistence context.
- Soft delete via a plain nullable `deletedAt` timestamp column (`User`, `Project`, `ChatSession`) — no delete-filtering annotation (`@SQLDelete`/`@Where`) wired up yet, so exclusion of soft-deleted rows must currently be done manually in queries.
- Role-based access via an enum-of-permission-sets pattern (`ProjectRole` → `Set<ProjectPermission>`) rather than a flat role string, so permission checks can test capability (`hasPermission(EDIT)`) instead of role identity.
- Local dev history tracked in `DEVLOG.md` (gitignored); this file is the tracked, public-facing project doc.
- Semantic, one-line commit messages (`feat:`, `fix:`, `docs:`, `chore:`, etc.).
