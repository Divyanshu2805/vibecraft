# APIs

6 REST controllers exist. All 5 `ProjectController` endpoints and all 4 `ProjectMemberController` endpoints now have real logic behind them (see [Project Status](../project-status.md#project-status)); every other controller's endpoints still resolve to a stub service method (returns `null`, an empty list, or does nothing). All endpoints hardcode `Long userId = 1L` rather than reading an authenticated principal, since there's no security/auth wiring yet. Every request body below is validated (`@Valid` + Bean Validation constraints on the DTO) — see [Request Validation](#request-validation) below the tables for the full constraint list per field.

## AuthController (`/api/auth`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| POST | `/api/auth/signup` | `SignupRequest` (`email` *(@Email)*, `name`, `password`, `@Valid`) | `AuthResponse` (`token`, `user`) | Stub |
| POST | `/api/auth/login` | `LoginRequest` (`email` *(@Email)*, `password`, `@Valid`) | `AuthResponse` | Stub |
| GET | `/api/auth/me` | — | `UserProfileResponse` (`id`, `email`, `name`, `avatarUrl`) | Stub; `userId` hardcoded to `1L` |

## ProjectController (`/api/projects`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects` | — | `List<ProjectSummaryResponse>` (`id`, `projectName`, `createdAt`, `updatedAt`) | **Real** — `ProjectRepository.findAllAccessibleByUser` + `ProjectMapper` |
| GET | `/api/projects/{id}` | — | `ProjectResponse` | **Real** — owner-scoped lookup (404 `ResourceNotFoundException` if missing/inaccessible) via `ProjectServiceImpl.getAccessibleProjectById`, maps via `ProjectMapper` |
| POST | `/api/projects` | `ProjectRequest` (`name`, `@Valid`) | `ProjectResponse` (201) | **Real** — looks up the owner via `UserRepository` (404 `ResourceNotFoundException` if missing), saves via `ProjectRepository`, maps via `ProjectMapper` |
| PATCH | `/api/projects/{id}` | `ProjectRequest` (`@Valid`) | `ProjectResponse` | **Real** — owner-scoped lookup (404 `ResourceNotFoundException` if missing/inaccessible) via `getAccessibleProjectById`, updates `name`, saves, maps via `ProjectMapper` |
| DELETE | `/api/projects/{id}` | — | 204 No Content | **Real** — soft delete (`ProjectService.softDelete`): owner-scoped lookup via `getAccessibleProjectById`, then an explicit owner check (403 `ForbiddenException` — currently redundant, since the lookup itself already scopes to the owner), then sets `deletedAt` |

## ProjectMemberController (`/api/projects/{projectId}/members`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/members` | — | `List<MemberResponse>` | **Real** — owner-scoped project lookup, then the owner (synthetic `role: OWNER` entry via `ProjectMemberMapper.toProjectMemberResponseFromOwner`) prepended to the real `ProjectMember` rows (`ProjectMemberRepository.findByIdProjectId`) |
| POST | `/api/projects/{projectId}/members` | `InviteMemberRequest` (`email` *(@Email)*, `role`, `@Valid`) | `MemberResponse` (201) | **Real** — 403 `ForbiddenException` if the caller isn't the project owner, or is inviting themself, or the invitee is already a member; looks up the invitee via `UserRepository.findByEmail` (unhandled `NoSuchElementException` → 500 if no such user), saves a new `ProjectMember` |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | `UpdateMemberRoleRequest` (`role`, `@Valid`) | `MemberResponse` | **Real** — 403 `ForbiddenException` if the caller isn't the owner; looks up the `ProjectMember` by composite id (unhandled `NoSuchElementException` → 500 if missing), updates `projectRole`, saves |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | — | 204 No Content | **Real** — 403 `ForbiddenException` if the caller isn't the owner; a plain `RuntimeException` (→ 500, not caught by `GlobalExceptionHandler`) if the member doesn't exist, otherwise deletes |

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
| `SignupRequest` | `email` | `@NotBlank`, `@Email` |
| | `name` | `@NotBlank`, `@Size(min = 1, max = 30)` |
| | `password` | `@NotBlank`, `@Size(min = 8)` |
| `LoginRequest` | `email` | `@NotBlank`, `@Email` |
| | `password` | `@NotBlank` |
| `ProjectRequest` | `name` | `@NotBlank`, `@Size(max = 255)` |
| `InviteMemberRequest` | `email` | `@NotBlank`, `@Email` |
| | `role` | `@NotNull` |
| `UpdateMemberRoleRequest` | `role` | `@NotNull` |
| `CheckoutRequest` | `planId` | `@NotNull` |

All 6 DTOs use `email` (not `username`) as the identity field now, matching the `User` entity's `email` column. `LoginRequest.password` intentionally has no `@Size` — login shouldn't reject a password based on shape, only presence; `SignupRequest.password` does (`min = 8`), since that's where a length policy actually belongs.
