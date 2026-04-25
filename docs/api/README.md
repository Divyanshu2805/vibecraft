# APIs

6 REST controllers exist. `AuthController`'s `signup`/`login`, all 5 `ProjectController` endpoints, and all 4 `ProjectMemberController` endpoints now have real logic behind them (see [Project Status](../project-status.md#project-status)); every other endpoint still resolves to a stub service method (returns `null`, an empty list, or does nothing). Every endpoint except `/api/auth/**` requires a `Bearer` JWT (`WebSecurityConfig` — see [Practices / Conventions](../practices/conventions.md#practices--conventions)); no controller hardcodes `userId` any more. 7 of the 9 real `Project`/`ProjectMember` methods are additionally `@PreAuthorize`-gated by role (see the Project Status "Authorization" row) — the two that aren't (`GET /api/projects`, `POST /api/projects`) don't need to be, since they're inherently self-scoped. Every request body below is validated (`@Valid` + Bean Validation constraints on the DTO) — see [Request Validation](#request-validation) below the tables for the full constraint list per field.

## AuthController (`/api/auth`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| POST | `/api/auth/signup` | `SignupRequest` (`username` *(@Email)*, `name`, `password`, `@Valid`) | `AuthResponse` (`token`, `user`) | **Real** — 400 `BadRequestException` if `username` is already taken; hashes `password` via `PasswordEncoder` (BCrypt), saves the `User`, maps via `UserMapper`, returns a real JWT (`AuthUtil.generateAccessToken`) usable immediately, no separate login required |
| POST | `/api/auth/login` | `LoginRequest` (`username` *(@Email)*, `password`, `@Valid`) | `AuthResponse` | **Real** — delegates to Spring Security's `AuthenticationManager` (which calls `UserServiceImpl.loadUserByUsername` + the same `PasswordEncoder` to verify the password); returns a real JWT (`AuthUtil.generateAccessToken`) |
| GET | `/api/auth/me` | — | `UserProfileResponse` (`id`, `username`, `name`) | Stub — `UserServiceImpl.getProfile()` still returns `null`, even though the caller's identity is now available via `AuthUtil.getCurrentUserId()` |

## ProjectController (`/api/projects`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects` | — | `List<ProjectSummaryResponse>` (`id`, `projectName`, `createdAt`, `updatedAt`) | **Real** — `ProjectRepository.findAllAccessibleByUser` (any project the caller is a member of, any role) + `ProjectMapper`. Not `@PreAuthorize`-gated (it's inherently self-scoped — always the caller's own list) |
| GET | `/api/projects/{id}` | — | `ProjectResponse` | **Real** — `@PreAuthorize("@security.canViewProject(#id)")` (any role), then member-scoped lookup (404 `ResourceNotFoundException` if missing/not a member — reachable only for a soft-deleted project, see Project Status "Known gaps") via `ProjectServiceImpl.getAccessibleProjectById`, maps via `ProjectMapper` |
| POST | `/api/projects` | `ProjectRequest` (`name`, `@Valid`) | `ProjectResponse` (201) | **Real** — looks up the caller via `UserRepository` (404 `ResourceNotFoundException` if missing), saves the `Project`, then creates a `ProjectMember` row for the caller with `projectRole = OWNER`, maps via `ProjectMapper`. Not `@PreAuthorize`-gated — creating a project doesn't need a pre-existing permission |
| PATCH | `/api/projects/{id}` | `ProjectRequest` (`@Valid`) | `ProjectResponse` | **Real** — `@PreAuthorize("@security.canEditProject(#id)")` (`EDITOR`/`OWNER` only, not `VIEWER`), then member-scoped lookup via `getAccessibleProjectById`, updates `name`, saves, maps via `ProjectMapper` |
| DELETE | `/api/projects/{id}` | — | 204 No Content | **Real** — `@PreAuthorize("@security.canDeleteProject(#id)")` (`OWNER` only), then member-scoped lookup via `getAccessibleProjectById`, sets `deletedAt` |

## ProjectMemberController (`/api/projects/{projectId}/members`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/members` | — | `List<MemberResponse>` | **Real** — `@PreAuthorize("@security.canViewMembers(#projectId)")` (any role), then all `ProjectMember` rows for the project (`ProjectMemberRepository.findByIdProjectId`), including the owner's own row (`projectRole = OWNER`) — there's no separate synthetic owner entry any more, the owner is just a regular row |
| POST | `/api/projects/{projectId}/members` | `InviteMemberRequest` (`username` *(@Email)*, `role`, `@Valid`) | `MemberResponse` (201) | **Real** — `@PreAuthorize("@security.canManageMembers(#projectId)")` (`OWNER` only); 403 `ForbiddenException` for inviting yourself or an already-existing member; 404 `ResourceNotFoundException` if no user has that `username`; saves a new `ProjectMember`. Not restricted: an `OWNER` can invite someone in *as* `OWNER` too (see Project Status "Known gaps") |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | `UpdateMemberRoleRequest` (`role`, `@Valid`) | `MemberResponse` | **Real** — `@PreAuthorize("@security.canManageMembers(#projectId)")` (`OWNER` only); 404 `ResourceNotFoundException` if no `ProjectMember` matches `(projectId, memberId)`, otherwise updates `projectRole`, saves |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | — | 204 No Content | **Real** — `@PreAuthorize("@security.canManageMembers(#projectId)")` (`OWNER` only); 404 `ResourceNotFoundException` if the member doesn't exist, otherwise deletes |

## FileController (`/api/projects/{projectId}/files`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/files` | — | `FileNode` (`path`, `modifiedAt`, `size`, `type`) | Stub. Returns a single `FileNode`, not a tree/list — despite the "get file tree" method name, `FileService.getFileTree` returns just one `FileNode`. |
| GET | `/api/projects/{projectId}/files/content?path=` | — | `FileContentResponse` (`path`, `content`) | Stub |

`FileService` also declares `saveFile(projectId, filePath, fileContent)`, but there's no controller endpoint for it yet. DTOs live under `dto.project` (`FileNode`, `FileContentResponse`), not a separate `dto.file` package.

## BillingController (no `@RequestMapping` prefix — full paths on each method)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/plans` | — | `List<PlanResponse>` | Stub — active plans |
| GET | `/api/me/subscription` | — | `SubscriptionResponse` | Stub |
| POST | `/api/payments/checkout` | `CheckoutRequest` (`planId`, `@Valid`) | `CheckoutResponse` (`checkoutUrl`) | Stub |
| POST | `/api/payments/portal` | — | `PortalResponse` (`portalUrl`) | Stub |

Earlier version of this controller also had a `/webhooks/payment` Stripe webhook handler and a `PaymentProcessor` dependency that didn't exist anywhere in the repo (and referenced unimported Stripe SDK types) — it didn't compile. That handler has since been removed from the controller entirely; no Stripe SDK dependency exists in `pom.xml`, and webhook handling isn't implemented anywhere currently.

## UsageController (`/api/usage`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/usage/today` | — | `UsageTodayResponse` (`tokensUsed`, `tokensLimit`, `previewsRunning`, `previewsLimit`) | Stub |
| GET | `/api/usage/limits` | — | `PlanLimitsResponse` (`planName`, `maxTokensPerDay`, `maxProjects`, `unlimitedAi`) | Stub |

## Request Validation

All 6 request DTOs (every DTO actually used as a `@RequestBody`) carry Bean Validation constraints; response DTOs never do. Every constraint has an explicit `message`, matching the convention used in the payflux repo.

| DTO | Field | Constraints |
|---|---|---|
| `SignupRequest` | `username` | `@NotBlank`, `@Email` *(field is called `username`, but still validated as email-shaped — see note below)* |
| | `name` | `@NotBlank`, `@Size(min = 1, max = 30)` |
| | `password` | `@NotBlank`, `@Size(min = 8)` |
| `LoginRequest` | `username` | `@NotBlank`, `@Email` |
| | `password` | `@NotBlank`, `@Size(min = 8)` *(no `message` — see note below)* |
| `ProjectRequest` | `name` | `@NotBlank`, `@Size(max = 255)` |
| `InviteMemberRequest` | `username` | `@NotBlank`, `@Email` |
| | `role` | `@NotNull` |
| `UpdateMemberRoleRequest` | `role` | `@NotNull` |
| `CheckoutRequest` | `planId` | `@NotNull` |

All 6 DTOs were renamed from `email` to `username` on 2026-04-26, matching the `User` entity's field rename (`email`/`passwordHash` → `username`/`password`, see [Differences from v3](../schema/README.md#differences-from-v3)). The `@Email` constraint on `username`/`SignupRequest.username`/`InviteMemberRequest.username` was kept as-is through the rename, so these fields are still validated as email-shaped despite the field name — the `@Email` messages were updated to say "must be a valid username address" (grammatically odd, kept verbatim since it's what's actually in the code) rather than being dropped.

`LoginRequest.password` gained a `@Size(min = 8)` on 2026-04-26, reversing an earlier, explicitly-documented decision to leave it unconstrained (login shouldn't reject a password based on shape, only presence — that's what `SignupRequest.password`'s `@Size(min = 8)` is for). Flagged, not reverted, since it's unclear whether this was deliberate; also flagged: unlike every other constraint in the codebase, this one has no `message = "..."`, so a failing login now falls into the generic per-field `MethodArgumentNotValidException` → `errors` list with whatever default message Bean Validation supplies, instead of a codebase-authored one.
