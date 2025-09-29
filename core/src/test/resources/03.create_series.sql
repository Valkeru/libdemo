INSERT INTO library.series (id, "name", cycle_id, created_at, updated_at)
VALUES (1, 'test_42db2cab8e', 1, now(), now()),
       (2, 'test_e2506e0e1a', 2, now(), now())
ON CONFLICT DO NOTHING;
