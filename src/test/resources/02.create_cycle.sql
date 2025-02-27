INSERT INTO library.cycle (id, "name", created_at, updated_at)
VALUES (1, 'test_9411799dad', now(), now()),
       (2, 'test_ba77861515', now(), now())
ON CONFLICT DO NOTHING;
