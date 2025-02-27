INSERT INTO library.author (id, first_name, middle_name, last_name, created_at, updated_at)
VALUES (1, 'test_9b844b884b', 'test_90321cca80', 'test_6012cf646d', NOW(), NOW()),
       (2, 'test_b562cf1fae', 'test_5f170fac03', 'test_a713816ac7', now(), now()),
       (3, 'test_f7ba5093a9', 'test_006df0bfd6', 'test_01529219d0', now(), now())
ON CONFLICT DO NOTHING;
