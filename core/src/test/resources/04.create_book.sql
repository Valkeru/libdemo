INSERT INTO library.book (id, "name", isbn, created_at, updated_at)
VALUES (1, 'test_d29827772a', '978-5-17-049678-5', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO library.book_author (author_id, book_id) VALUES (1, 1)
ON CONFLICT DO NOTHING;
