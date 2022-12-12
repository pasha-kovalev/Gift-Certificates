INSERT INTO gift_certificate (name, description, price, create_date, last_update_date, duration)
VALUES ('c1', 'd1', 100.01, '2022-04-04 22:11:33', '2022-04-04 22:11:33', 1),
       ('c2', 'd2', 200.02, '2022-04-04 22:11:33', '2022-04-04 22:11:33', 2),
       ('c3', 'd3', 300.03, '2022-04-04 22:11:33', '2022-04-04 22:11:33', 3);

INSERT INTO tag (name)
VALUES ('mjc'),
       ('epam'),
       ('minsk'),
       ('aboba');

INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 3);

INSERT INTO users (name, password, role)
VALUES ('admin', '$2a$10$UCwjA.ReCMzf0HHrDKYExu.VD.z0.JHWjBPSNkdcJcqqDl0LTnEaa', 'ADMIN'),
       ('user2', '$2a$10$FVNrlpPVKp96RPPRaI/rGO5ZGCCw1TyvhJgDBgZYKm4FiEkH0tQha', 'USER'),
       ('user3', '$2a$10$FVNrlpPVKp96RPPRaI/rGO5ZGCCw1TyvhJgDBgZYKm4FiEkH0tQha', 'USER'),
       ('user4', '$2a$10$FVNrlpPVKp96RPPRaI/rGO5ZGCCw1TyvhJgDBgZYKm4FiEkH0tQha', 'USER');

INSERT INTO "order" (user_id, create_date, total)
VALUES ('1', '2022-04-04 22:11:33', '56.56'),
       ('3', '2022-04-04 22:11:33', '46.57'),
       ('4', '2022-04-04 22:11:33', '26.00'),
       ('2', '2022-04-04 22:11:33', '16.56');

INSERT INTO gift_certificate_orders (order_id, gift_certificate_id, quantity, price, duration)
VALUES (1, 1, 2, 10.0, 30),
       (1, 2, 1, 10.0, 30),
       (2, 2, 1, 10.0, 30),
       (3, 2, 1, 10.0, 30);