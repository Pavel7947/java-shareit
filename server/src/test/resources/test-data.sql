INSERT INTO users (email, name) --id 1
values ('1@mail.ru', 'name');

INSERT INTO users (email, name) --id 2
values ('2@mail.ru', 'pasha');

INSERT INTO users (email, name) --id 3
values ('3@mail.ru', 'dasha');

INSERT INTO users (email, name) --id 4
values ('4@mail.ru', 'sasha');



INSERT INTO requests(description, requester_id, create_date) --id 1
values('drill request', 3,  NOW());

INSERT INTO requests(description, requester_id, create_date) --id 2
values('mallet request', 3, NOW() -1);

INSERT INTO requests(description, requester_id, create_date) --id 3
values('grinder request', 4, NOW() -1);

INSERT INTO requests(description, requester_id, create_date) --id 3
values('axe request', 4, NOW() -1);



INSERT INTO items (name, description, is_available, owner_id) --id 1
values('drill', 'good drill', true, 1);

INSERT INTO items (name, description, is_available, owner_id) --id 3
values('mallet', 'good mallet', false, 1);

INSERT INTO items (name, description, is_available, owner_id, request_id) --id 4
values('axe', 'good axe', true, 1, 3);

INSERT INTO items (name, description, is_available, owner_id) --id 2
values('grinder', 'good grinder', true, 2);



INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) --id 1
values( NOW() - 6, NOW() - 5, 1, 3, 'APPROVED');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) --id 2
values(NOW() + 6, NOW() + 7, 1, 3, 'WAITING');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) --id 3
values(NOW() - 1, NOW() + 1, 1, 3, 'WAITING');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) --id 4
values(NOW() + 4, NOW() + 5, 1, 3, 'APPROVED');

