ALTER TABLE library.book
    ALTER COLUMN isbn TYPE CHAR(17) USING (isbn::CHAR(17));
