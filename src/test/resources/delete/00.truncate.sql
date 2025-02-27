TRUNCATE library.author CASCADE;
TRUNCATE library.book CASCADE;
TRUNCATE library.series CASCADE;
TRUNCATE library.cycle CASCADE;

ALTER SEQUENCE library.author_id_seq RESTART WITH 1;
ALTER SEQUENCE library.book_id_seq RESTART WITH 1;
ALTER SEQUENCE library.series_id_seq RESTART WITH 1;
ALTER SEQUENCE library.cycle_id_seq RESTART WITH 1;
