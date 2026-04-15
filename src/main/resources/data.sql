INSERT INTO users (id, email, password_hash, name, avatar_url, created_at, updated_at, deleted_at)
VALUES (1, 'divyanshu@gmail.com', NULL, 'Divyanshu', NULL, now(), now(), NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, email, password_hash, name, avatar_url, created_at, updated_at, deleted_at)
VALUES (2, 'anuj@gmail.com', NULL, 'Anuj', NULL, now(), now(), NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, email, password_hash, name, avatar_url, created_at, updated_at, deleted_at)
VALUES (3, 'shivam@gmail.com', NULL, 'Shivam', NULL, now(), now(), NULL)
    ON CONFLICT (id) DO NOTHING;
