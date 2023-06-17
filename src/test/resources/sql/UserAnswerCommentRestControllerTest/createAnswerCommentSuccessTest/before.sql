TRUNCATE TABLE accounts CASCADE;
TRUNCATE TABLE permissions CASCADE;
TRUNCATE TABLE answer_comments CASCADE;
TRUNCATE TABLE answers CASCADE;
TRUNCATE TABLE questions CASCADE;
TRUNCATE TABLE tags CASCADE;


INSERT INTO permissions(id, name, created_date, modified_date)
VALUES (1, 'ADMIN', current_timestamp, current_timestamp),
       (2, 'MODERATOR', current_timestamp, current_timestamp),
       (3, 'USER', current_timestamp, current_timestamp);

INSERT INTO accounts(id, username, password, enabled, created_date, modified_date)
VALUES (1, 'user1', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp);

INSERT INTO accounts_permissions(account_id, permission_id)
VALUES (1, 3);

INSERT INTO tags(id, name, created_date, modified_date)
VALUES (1, 'tag1', current_timestamp, current_timestamp),
       (2, 'tag2', current_timestamp, current_timestamp),
       (3, 'tag3', current_timestamp, current_timestamp),
       (4, 'tag4', current_timestamp, current_timestamp);

INSERT INTO questions(id, title, description, account_id, created_date, modified_date)
VALUES (1, 'title1', 'description1', 1, current_timestamp, current_timestamp);

INSERT INTO questions_tags(question_id, tag_id)
VALUES (1, 1),
       (1, 2);
INSERT INTO answers(id, created_date, modified_date, text, accepted, account_id, question_id)
VALUES (1, current_timestamp, current_timestamp, 'Answer1', true, 1, 1);