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
        current_timestamp),
       (2, 'user2', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp),
       (3, 'user3', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp),
       (4, 'user4', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp),
       (5, 'user5', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', true, current_timestamp,
        current_timestamp);


INSERT INTO questions(id, title, description, account_id, created_date, modified_date)
VALUES (1, 'title1', 'description1', 5, current_timestamp, current_timestamp),
       (2, 'title2', 'description2', 4, current_timestamp, current_timestamp),
       (3, 'title3', 'description3', 3, current_timestamp, current_timestamp),
       (4, 'title4', 'description4', 2, current_timestamp, current_timestamp),
       (5, 'title5', 'description5', 1, current_timestamp, current_timestamp);

INSERT INTO answers(id, question_id, account_id, text, created_date, modified_date, accepted)
VALUES (1, 5, 1, 'text1', current_timestamp, current_timestamp, true),
       (2, 4, 2, 'text2', current_timestamp, current_timestamp, true),
       (3, 3, 3, 'text3', current_timestamp, current_timestamp, true),
       (4, 2, 4, 'text4', current_timestamp, current_timestamp, true),
       (5, 1, 5, 'text5', current_timestamp, current_timestamp, true);
INSERT INTO accounts_permissions(account_id, permission_id)
VALUES (1, 3),
       (2, 3),
       (3, 3),
       (4, 3),
       (5, 3);

