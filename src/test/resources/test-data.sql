INSERT INTO mpa (mpa_id, name) VALUES
(1, 'G'),
(2, 'R');


INSERT INTO genres (genre_id, name) VALUES
(1, 'Action'),
(2, 'Drama'),
(3, 'Comedy');


INSERT INTO users (email, login, user_name, birthday) VALUES
('testUser@example.com', 'testUser', 'Test User', '2000-01-01'),
('testFriend@example.com', 'testFriend', 'Test Friend', '1990-01-01'),
('commonFriend@example.com', 'commonFriend', 'Common Friend', '1985-01-01');


INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('Test Film 1', 'Description for Test Film 1', '2024-01-01', 120, 1);

INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 1),
(1, 2);

INSERT INTO friends (user_id, friend_id) VALUES
(1, 3),
(2, 3);

INSERT INTO likes (film_id, user_id) VALUES
(1, 1);

