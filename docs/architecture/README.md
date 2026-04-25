# Architecture

- Base package: `com.java.vibecraft`
- Entry point: `VibecraftApplication` (`src/main/java/com/java/vibecraft/VibecraftApplication.java`)
- Config: `src/main/resources/application.yaml` (no more `data.sql` — deleted 2026-04-26).
- `entity` — the 10 JPA entities described above (plus `ProjectMemberId`/`ChatSessionId` composite-key classes). `User` also implements Spring Security's `UserDetails`.
- `enums` — the 4 domain enums described above.
- `repository` — `ProjectRepository`, `ProjectMemberRepository`, `UserRepository` (Spring Data JPA). Only 3 of 10 entities have one so far.
- `mapper` — `ProjectMapper`, `ProjectMemberMapper`, `UserMapper` (MapStruct). Only `Project`, `ProjectMember`, and `User` have one so far.
- `error` — `ApiError`, `GlobalExceptionHandler`, `ResourceNotFoundException`, `ForbiddenException`, `BadRequestException` — centralized exception→HTTP-response handling.
- `security` — `WebSecurityConfig` (`@Configuration`, defines the `SecurityFilterChain`/`PasswordEncoder`/`AuthenticationManager` beans), `JwtAuthFilter` (`OncePerRequestFilter`, populates the `SecurityContext` from a `Bearer` token), `AuthUtil` (JWT generate/verify + `getCurrentUserId()`), `JwtUserPrincipal` (the JWT-derived principal record).
- `controller` — 6 REST controllers: `AuthController`, `ProjectController`, `ProjectMemberController`, `FileController`, `BillingController`, `UsageController` (see [APIs](../api/README.md#apis) above).
- `service` — 8 service interfaces, one (or two, for auth) per controller.
- `service.impl` — an `@Service` implementation of each interface above (`AuthServiceImpl`, `UserServiceImpl`, `ProjectServiceImpl`, `ProjectMemberServiceImpl`, `FileServiceImpl`, `PlanServiceImpl`, `SubscriptionServiceImpl`, `UsageServiceImpl`). `ProjectServiceImpl`, `ProjectMemberServiceImpl`, and `AuthServiceImpl` are fully implemented; `UserServiceImpl` implements `UserDetailsService.loadUserByUsername` for real but its own `UserService.getProfile()` is still a stub; `FileServiceImpl`/`PlanServiceImpl`/`SubscriptionServiceImpl`/`UsageServiceImpl` are still entirely stubs.
- `dto` — request/response records, one subpackage per domain (`auth`, `project`, `member`, `subscription`). File DTOs (`FileNode`, `FileContentResponse`) live under `dto.project`, not a separate `dto.file` package.
