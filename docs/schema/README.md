# Entities / Models

No JPA entities are implemented in code yet. Below is the **v1 data model design** (design/planning stage — the target schema, not yet implemented). Update this section (and bump the version) whenever the schema changes.

## ER Diagram

```mermaid
erDiagram
    USER ||--o{ SUBSCRIPTION : "has active"
    USER ||--o{ USAGE_LOG : performs
    USER ||--o{ PROJECT_OWNERSHIP : owns
    USER ||--o{ PROJECT_MEMBER : "is member of"
    USER ||--o{ PROJECT_FILE : triggers

    PROJECT ||--o{ PROJECT_OWNERSHIP : "has owner"
    PROJECT ||--o{ PROJECT_MEMBER : "has members"
    PROJECT ||--o{ PROJECT_FILE : contains
    PROJECT ||--o| PREVIEW : "has one active"
    PROJECT ||--o{ CHAT_SESSION : "has conversations"

    PLAN ||--o{ SUBSCRIPTION : follows
    CHAT_SESSION ||--o{ CHAT_MESSAGE : contains

    USER {
        bigint id PK
        string email UK
        string password_hash
        string name
        string avatar_url
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    PROJECT {
        bigint id PK
        string name
        bigint owner_id FK
        bool is_public
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    SUBSCRIPTION {
        bigint id PK
        bigint user_id FK
        bigint plan_id FK
        string stripe_subscription_id UK
        string status
        timestamp current_period_start
        timestamp current_period_end
        bool cancel_at_period_end
        timestamp created_at
        timestamp updated_at
    }

    PLAN {
        bigint id PK
        string name
        string stripe_price_id
        int max_projects
        int max_tokens_per_day
        int max_previews
        bool unlimited_ai
        jsonb features
        bool active
    }

    USAGE_LOG {
        bigint id PK
        bigint user_id FK
        bigint project_id FK
        string action
        int tokens_used
        int duration_ms
        jsonb metadata
        timestamp created_at
    }

    PROJECT_OWNERSHIP {
        bigint project_id PK_FK
        bigint user_id PK_FK
    }

    PROJECT_MEMBER {
        bigint project_id PK_FK
        bigint user_id PK_FK
        string role "EDITOR, VIEWER"
        bigint invited_by FK
        timestamp invited_at
    }

    PROJECT_FILE {
        bigint id PK
        bigint project_id FK
        string path UK
        string minio_object_key
        bigint created_by FK
        bigint updated_by FK
        timestamp created_at
        timestamp updated_at
    }

    PREVIEW {
        bigint id PK
        bigint project_id UK_FK
        string namespace
        string pod_name
        string preview_url
        string status
        timestamp started_at
        timestamp terminated_at
        timestamp created_at
    }

    CHAT_SESSION {
        bigint project_id PK_FK
        bigint user_id PK_FK
        string title
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    CHAT_MESSAGE {
        bigint id PK
        bigint project_id FK
        bigint user_id FK
        varchar role
        text content
        jsonb tool_calls
        varchar tool_call_id
        int tokens_used
        timestamp created_at
    }
```

## Entity summary

| Entity | Purpose |
|---|---|
| `USER` | Account holder — auth, profile. |
| `PROJECT` | A user-owned workspace/app being built; can be public or private. |
| `PROJECT_OWNERSHIP` | Join table recording project ownership (alongside `PROJECT.owner_id`, which denormalizes the current owner directly on the project row). |
| `PROJECT_MEMBER` | Join table for collaborators on a project, with `EDITOR`/`VIEWER` roles and invite tracking. |
| `PROJECT_FILE` | A file belonging to a project; content stored in MinIO object storage (`minio_object_key`), with created/updated-by audit fields. |
| `PREVIEW` | A live preview deployment of a project (pod-based, with a public `preview_url` and lifecycle status/timestamps). |
| `CHAT_SESSION` | An AI chat/build conversation scoped to a project and the user who started it. |
| `CHAT_MESSAGE` | An individual message within a chat session, including AI tool-call data and token usage. |
| `SUBSCRIPTION` | A user's billing subscription (Stripe-backed) to a `PLAN`. |
| `PLAN` | A billing tier defining quotas (max projects, daily token budget, max previews) and feature flags. |
| `USAGE_LOG` | Per-user, per-project audit/usage record (action performed, tokens used, duration, metadata). |

## Notes / not yet drawn as explicit relationships

- `PROJECT.owner_id` is a direct FK to `USER.id`, denormalized alongside the `PROJECT_OWNERSHIP` join table.
- `PROJECT_FILE.created_by` / `updated_by` and `PROJECT_MEMBER.invited_by` are FKs to `USER.id` (audit trail), summarized above under the `triggers` relationship rather than drawn as separate lines.
- `CHAT_SESSION.user_id` and `CHAT_MESSAGE.user_id` are FKs to `USER.id`, not drawn as separate lines in this v1 diagram (only the `PROJECT` → `CHAT_SESSION` relationship is shown).

## Revision history

- **v1** (2026-03-30): Initial schema design covering users, projects, collaboration (ownership/membership), file storage, live previews, AI chat, and billing (plans/subscriptions/usage).
