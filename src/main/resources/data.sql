INSERT INTO users (id, username, password, name, created_at, updated_at, deleted_at)
VALUES (1, 'divyanshu@gmail.com', 'N/A', 'Divyanshu', now(), now(), NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, username, password, name, created_at, updated_at, deleted_at)
VALUES (2, 'anuj@gmail.com', 'N/A', 'Anuj', now(), now(), NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, username, password, name, created_at, updated_at, deleted_at)
VALUES (3, 'shivam@gmail.com', 'N/A', 'Shivam', now(), now(), NULL)
    ON CONFLICT (id) DO NOTHING;
