TRUNCATE TABLE accounts CASCADE;
TRUNCATE TABLE permissions CASCADE;
TRUNCATE TABLE questions CASCADE;
TRUNCATE TABLE answers CASCADE;

INSERT INTO permissions(id, name, created_date, modified_date)
VALUES (1, 'ADMIN', current_timestamp, current_timestamp),
       (2, 'MODERATOR', current_timestamp, current_timestamp),
       (3, 'USER', current_timestamp, current_timestamp);

INSERT INTO accounts(id, username, password, enabled, created_date, modified_date)
VALUES (1, 'user1', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp);

INSERT INTO accounts_permissions(account_id, permission_id)
VALUES (1, 3);

INSERT INTO questions(id, title, description, account_id, created_date, modified_date)
VALUES (1, 'title1', 'description1', 1, current_timestamp, current_timestamp);

INSERT INTO answers(id, question_id, account_id, text, created_date, modified_date, accepted)
VALUES (1, 1, 1, 'text', current_timestamp, current_timestamp, true);