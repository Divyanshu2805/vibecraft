# APIs

6 REST controllers exist, all delegating to a `@Service`-annotated implementation, so the app now starts and every endpoint is reachable — but **every implementation is currently a stub** (returns `null`, an empty list, or does nothing), so nothing below returns real data yet. Every endpoint also hardcodes `Long userId = 1L` rather than reading an authenticated principal, since there's no security/auth wiring yet. Every request body below is validated (`@Valid` + Bean Validation constraints on the DTO) — see [Request Validation](#request-validation) below the tables for the full constraint list per field.

## AuthController (`/api/auth`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| POST | `/api/auth/signup` | `SignupRequest` (`username` *(@Email)*, `name`, `password`, `@Valid`) | `AuthResponse` (`token`, `user`) | |
| POST | `/api/auth/login` | `LoginRequest` (`username` *(@Email)*, `password`, `@Valid`) | `AuthResponse` | |
| GET | `/api/auth/me` | — | `UserProfileResponse` (`id`, `username`, `name`) | `userId` hardcoded to `1L` |

## ProjectController (`/api/projects`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects` | — | `List<ProjectSummaryResponse>` | The caller's projects |
| GET | `/api/projects/{id}` | — | `ProjectSummaryResponse` | |
| POST | `/api/projects` | `ProjectRequest` (`name`, `@Valid`) | `ProjectResponse` (201) | |
| PATCH | `/api/projects/{id}` | `ProjectRequest` (`@Valid`) | `ProjectResponse` | |
| DELETE | `/api/projects/{id}` | — | 204 No Content | Soft delete (`ProjectService.softDelete`) |

## ProjectMemberController (`/api/projects/{projectId}/members`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/members` | — | `List<MemberResponse>` | |
| POST | `/api/projects/{projectId}/members` | `InviteMemberRequest` (`username` *(@Email)*, `role`, `@Valid`) | `MemberResponse` (201) | |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | `UpdateMemberRoleRequest` (`role`, `@Valid`) | `MemberResponse` | |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | — | 204 No Content | |

## FileController (`/api/projects/{projectId}/files`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/files` | — | `FileTreeResponse` (`List<FileNode>`) | |
| GET | `/api/projects/{projectId}/files/content?path=` | — | `FileContentResponse` (`path`, `content`) | |

`FileService` (renamed from `ProjectFileService`) also declares `saveFile(projectId, filePath, fileContent, userId)`, but there's no controller endpoint for it yet.

## BillingController (no `@RequestMapping` prefix — full paths on each method)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/plans` | — | `List<PlanResponse>` | Active plans |
| GET | `/api/me/subscription` | — | `SubscriptionResponse` | |
| POST | `/api/payments/checkout` | `CheckoutRequest` (`planId`, `@Valid`) | `CheckoutResponse` (`checkoutUrl`) | |
| POST | `/api/payments/portal` | — | `PortalResponse` (`portalUrl`) | |

Earlier version of this controller also had a `/webhooks/payment` Stripe webhook handler and a `PaymentProcessor` dependency that didn't exist anywhere in the repo (and referenced unimported Stripe SDK types) — it didn't compile. That handler has since been removed from the controller entirely; no Stripe SDK dependency exists in `pom.xml`, and webhook handling isn't implemented anywhere currently.

## UsageController (`/api/usage`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/usage/today` | — | `UsageTodayResponse` (`tokensUsed`, `tokensLimit`, `previewsRunning`, `previewsLimit`) | |
| GET | `/api/usage/limits` | — | `PlanLimitsResponse` (`planName`, `maxTokensPerDay`, `maxProjects`, `unlimitedAi`) | |

## Request Validation

All 6 request DTOs (every DTO actually used as a `@RequestBody`) carry Bean Validation constraints; response DTOs never do. Every constraint has an explicit `message`, matching the convention used in the payflux repo.

| DTO | Field | Constraints |
|---|---|---|
| `SignupRequest` | `username` | `@NotBlank`, `@Email` |
| | `name` | `@NotBlank`, `@Size(min = 1, max = 30)` |
| | `password` | `@NotBlank`, `@Size(min = 4)` |
| `LoginRequest` | `username` | `@NotBlank`, `@Email` |
| | `password` | `@NotBlank`, `@Size(min = 4, max = 50)` |
| `ProjectRequest` | `name` | `@NotBlank`, `@Size(max = 255)` |
| `InviteMemberRequest` | `username` | `@NotBlank`, `@Email` |
| | `role` | `@NotNull` |
| `UpdateMemberRoleRequest` | `role` | `@NotNull` |
| `CheckoutRequest` | `planId` | `@NotNull` |

**Fixed while adding this:** `SignupRequest.name`/`password` and `LoginRequest.password` previously had only `@Size`, which doesn't reject `null` (only empty/too-short strings) — added the missing `@NotBlank`. `CheckoutRequest` had no validation at all before. Separately, `AuthController.signup`/`login` and `BillingController`'s checkout endpoint were missing `@Valid` on their `@RequestBody` parameter — meaning even correctly-annotated DTOs would never have actually been validated on those 3 endpoints. All three now have it (`ProjectController`/`ProjectMemberController` already did).
