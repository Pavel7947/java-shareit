INSERT INTO users (user_id, email, name)
values (1, '22@mail.ru', 'name');

INSERT INTO users (user_id, email, name)
values (2, '23@mail.ru', 'pasha');

INSERT INTO users (user_id, email, name)
values (3, '24@mail.ru', 'dasha');

INSERT INTO users (user_id, email, name)
values (4, '26@mail.ru', 'sasha');

INSERT INTO items (item_id, name, description, is_available, owner_id)
values(1, 'drill', 'good drill', true, 1);

INSERT INTO items (item_id, name, description, is_available, owner_id)
values(2, 'grinder', 'good grinder', true, 2);

INSERT INTO items (item_id, name, description, is_available, owner_id)
values(3, 'mallet', 'good mallet', false, 1);

INSERT INTO bookings (booking_id, start_date, end_date, item_id, booker_id, status)
values(1, '2024-12-10T9:11:56', '2024-12-11T9:11:56', 1, 3, 'APPROVED');

INSERT INTO bookings (booking_id, start_date, end_date, item_id, booker_id, status)
values(2, '2024-12-12T9:11:56', '2024-12-13T9:11:56', 1, 3, 'APPROVED');

INSERT INTO requests(request_id, description, requester_id, create_date)
values(1, 'drill request', 3,  '2024-12-12T9:11:56');

INSERT INTO requests(request_id, description, requester_id, create_date)
values(2, 'mallet request', 3, '2024-12-13T9:11:56');