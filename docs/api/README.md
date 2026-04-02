# APIs

6 REST controllers exist, all delegating to service **interfaces with no implementation** — so none of these are actually callable end-to-end yet (Spring can't wire an interface with zero `@Service` beans). Every endpoint below also hardcodes `Long userId = 1L` rather than reading an authenticated principal, since there's no security/auth wiring yet.

## AuthController (`/api/auth`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| POST | `/api/auth/signup` | `SignupRequest` (`username` *(@Email)*, `name`, `password`) | `AuthResponse` (`token`, `user`) | |
| POST | `/api/auth/login` | `LoginRequest` (`username` *(@Email)*, `password`) | `AuthResponse` | |
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
| POST | `/api/projects/{projectId}/members` | `InviteMemberRequest` (`username` *(@Email)*, `role`) | `MemberResponse` (201) | |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | `UpdateMemberRoleRequest` (`role`) | `MemberResponse` | |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | — | 204 No Content | |

## FileController (`/api/projects/{projectId}/files`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/projects/{projectId}/files` | — | `FileTreeResponse` (`List<FileNode>`) | |
| GET | `/api/projects/{projectId}/files/content?path=` | — | `FileContentResponse` (`path`, `content`) | |

`ProjectFileService` also declares `saveFile(projectId, filePath, fileContent, userId)`, but there's no controller endpoint for it yet.

## BillingController (no `@RequestMapping` prefix — full paths on each method)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/plans` | — | `List<PlanResponse>` | Active plans |
| GET | `/api/me/subscription` | — | `SubscriptionResponse` | |
| POST | `/api/payments/checkout` | `CheckoutRequest` (`planId`) | `CheckoutResponse` (`checkoutUrl`) | |
| POST | `/api/payments/portal` | — | `PortalResponse` (`portalUrl`) | |

Earlier version of this controller also had a `/webhooks/payment` Stripe webhook handler and a `PaymentProcessor` dependency that didn't exist anywhere in the repo (and referenced unimported Stripe SDK types) — it didn't compile. That handler has since been removed from the controller entirely; no Stripe SDK dependency exists in `pom.xml`, and webhook handling isn't implemented anywhere currently.

## UsageController (`/api/usage`)

| Method | Path | Request | Response | Notes |
|---|---|---|---|---|
| GET | `/api/usage/today` | — | `UsageTodayResponse` (`tokensUsed`, `tokensLimit`, `previewsRunning`, `previewsLimit`) | |
| GET | `/api/usage/limits` | — | `PlanLimitsResponse` (`planName`, `maxTokensPerDay`, `maxProjects`, `unlimitedAi`) | |
