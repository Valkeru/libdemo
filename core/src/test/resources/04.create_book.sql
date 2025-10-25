INSERT INTO library.book (id, "name", isbn, created_at, updated_at)
VALUES (1, 'test_d29827772a', '978-5-17-049678-5', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO library.book_author (author_id, book_id) VALUES ('84c1599c-21e6-47f3-a03b-12f6071da20b', 1)
ON CONFLICT DO NOTHING;
