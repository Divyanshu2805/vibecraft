# Architecture

- Base package: `com.java.vibecraft`
- Entry point: `VibecraftApplication` (`src/main/java/com/java/vibecraft/VibecraftApplication.java`)
- Config: `src/main/resources/application.yaml` (no more `data.sql` — deleted 2026-04-26).
- `entity` — the 10 JPA entities described above (plus `ProjectMemberId`/`ChatSessionId` composite-key classes). `User` also implements Spring Security's `UserDetails`.
- `enums` — the 4 domain enums described above.
- `config` — `PaymentConfig` (new 2026-05-02, `@Configuration`) — sets the Stripe SDK's static `Stripe.apiKey` from `stripe.api.secret` at startup.
- `repository` — `ProjectRepository`, `ProjectMemberRepository`, `UserRepository`, and (new 2026-05-02) `PlanRepository`, `SubscriptionRepository` (Spring Data JPA). Only 5 of 10 entities have one so far.
- `mapper` — `ProjectMapper`, `ProjectMemberMapper`, `UserMapper`, and (new 2026-05-02) `SubscriptionMapper` (MapStruct). Only `Project`, `ProjectMember`, `User`, and `Subscription` have one so far.
- `error` — `ApiError`, `GlobalExceptionHandler`, `ResourceNotFoundException`, `ForbiddenException`, `BadRequestException` — centralized exception→HTTP-response handling.
- `security` — `WebSecurityConfig` (`@Configuration`, defines the `SecurityFilterChain`/`PasswordEncoder`/`AuthenticationManager` beans), `JwtAuthFilter` (`OncePerRequestFilter`, populates the `SecurityContext` from a `Bearer` token), `AuthUtil` (JWT generate/verify + `getCurrentUserId()`), `JwtUserPrincipal` (the JWT-derived principal record).
- `controller` — 6 REST controllers: `AuthController`, `ProjectController`, `ProjectMemberController`, `FileController`, `BillingController`, `UsageController` (see [APIs](../api/README.md#apis) above). `BillingController` gained a 7th method, the `/webhooks/payment` handler, on 2026-05-02.
- `service` — 9 service interfaces, one (or two, for auth) per controller, plus the new gateway-facing `PaymentProcessor` (2026-05-02).
- `service.impl` — an `@Service` implementation of each interface above (`AuthServiceImpl`, `UserServiceImpl`, `ProjectServiceImpl`, `ProjectMemberServiceImpl`, `FileServiceImpl`, `PlanServiceImpl`, `SubscriptionServiceImpl`, `UsageServiceImpl`, and now `StripePaymentProcessor`). `ProjectServiceImpl`, `ProjectMemberServiceImpl`, and `AuthServiceImpl` are fully implemented; `UserServiceImpl` implements `UserDetailsService.loadUserByUsername` for real but its own `UserService.getProfile()` is still a stub; `SubscriptionServiceImpl` and `StripePaymentProcessor` are mostly real as of 2026-05-02 (see Project Status); `FileServiceImpl`/`PlanServiceImpl`/`UsageServiceImpl` are still entirely stubs.
- `dto` — request/response records, one subpackage per domain (`auth`, `project`, `member`, `subscription`). File DTOs (`FileNode`, `FileContentResponse`) live under `dto.project`, not a separate `dto.file` package.
