# APIs

6 REST controllers exist. All 5 `ProjectController` endpoints and all 4 `ProjectMemberController` endpoints now have real logic behind them (see [Project Status](../project-status.md#project-status)); every other controller's endpoints still resolve to a stub service method (returns `null`, an empty list, or does nothing). All endpoints hardcode `Long userId = 1L` rather than reading an authenticated principal, since there's no security/auth wiring yet. Every request body below is validated (`@Valid` + Bean Validation constraints on the DTO) — see [Request Validation](#request-validation) below the tables for the full constraint list per field.

## AuthController (`/api/auth`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| POST | `/api/auth/signup` | `SignupRequest` (`username` *(@Email)*, `name`, `password`, `@Valid`) | `AuthResponse` (`token`, `user`) | Stub |
| POST | `/api/auth/login` | `LoginRequest` (`username` *(@Email)*, `password`, `@Valid`) | `AuthResponse` | Stub |
| GET | `/api/auth/me` | — | `UserProfileResponse` (`id`, `username`, `name`) | Stub; `userId` hardcoded to `1L` |

## ProjectController (`/api/projects`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects` | — | `List<ProjectSummaryResponse>` (`id`, `projectName`, `createdAt`, `updatedAt`) | **Real** — `ProjectRepository.findAllAccessibleByUser` (any project the caller is a member of, any role) + `ProjectMapper` |
| GET | `/api/projects/{id}` | — | `ProjectResponse` | **Real** — member-scoped lookup (404 `ResourceNotFoundException` if missing/not a member) via `ProjectServiceImpl.getAccessibleProjectById`, maps via `ProjectMapper` |
| POST | `/api/projects` | `ProjectRequest` (`name`, `@Valid`) | `ProjectResponse` (201) | **Real** — looks up the caller via `UserRepository` (404 `ResourceNotFoundException` if missing), saves the `Project`, then creates a `ProjectMember` row for the caller with `projectRole = OWNER`, maps via `ProjectMapper` |
| PATCH | `/api/projects/{id}` | `ProjectRequest` (`@Valid`) | `ProjectResponse` | **Real** — member-scoped lookup (404 `ResourceNotFoundException` if missing/not a member) via `getAccessibleProjectById`, updates `name`, saves, maps via `ProjectMapper`. ⚠ No owner check — any member (`EDITOR`/`VIEWER`/`OWNER`) can rename the project, see the Project Status "Known gaps" note |
| DELETE | `/api/projects/{id}` | — | 204 No Content | **Real** — soft delete (`ProjectService.softDelete`): member-scoped lookup via `getAccessibleProjectById`, then sets `deletedAt`. ⚠ No owner check — any member can soft-delete the project |

## ProjectMemberController (`/api/projects/{projectId}/members`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/members` | — | `List<MemberResponse>` | **Real** — member-scoped project lookup, then all `ProjectMember` rows for the project (`ProjectMemberRepository.findByIdProjectId`), including the owner's own row (`projectRole = OWNER`) — there's no separate synthetic owner entry any more, the owner is just a regular row |
| POST | `/api/projects/{projectId}/members` | `InviteMemberRequest` (`username` *(@Email)*, `role`, `@Valid`) | `MemberResponse` (201) | **Real** — member-scoped project lookup (any role); 403 `ForbiddenException` only for inviting yourself or an already-existing member; looks up the invitee via `UserRepository.findByUsername` (unhandled `NoSuchElementException` → 500 if no such user), saves a new `ProjectMember`. ⚠ No owner check — any existing member can invite new members, and can invite them in as `OWNER` (`request.role()` isn't restricted) |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | `UpdateMemberRoleRequest` (`role`, `@Valid`) | `MemberResponse` | **Real** — looks up the `ProjectMember` by composite id (unhandled `NoSuchElementException` → 500 if missing), updates `projectRole`, saves. ⚠ No project-membership or owner check at all — doesn't even call `getAccessibleProjectById` |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | — | 204 No Content | **Real** — a plain `RuntimeException` (→ 500, not caught by `GlobalExceptionHandler`) if the member doesn't exist, otherwise deletes. ⚠ Same as PATCH above — no project-membership or owner check at all |

## FileController (`/api/projects/{projectId}/files`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/files` | — | `FileNode` (`path`, `modifiedAt`, `size`, `type`) | Stub. Returns a single `FileNode`, not a tree/list — despite the "get file tree" method name, `FileService.getFileTree` returns just one `FileNode`. |
| GET | `/api/projects/{projectId}/files/content?path=` | — | `FileContentResponse` (`path`, `content`) | Stub |

`FileService` also declares `saveFile(projectId, filePath, fileContent, userId)`, but there's no controller endpoint for it yet. DTOs live under `dto.project` (`FileNode`, `FileContentResponse`), not a separate `dto.file` package.

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
