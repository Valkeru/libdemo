package ru.valkeru.libdemo.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Database {

    public static final int SEQUENCE_CACHE = 5;

    @UtilityClass
    public class Schema {
        public static final String LIBRARY = "LIBRARY";
    }

    @UtilityClass
    public class Generator {
        public static final String AUTHOR_ID = "author_id_gen";
        public static final String BOOK_ID = "book_id_gen";
        public static final String SERIES_ID = "series_id_gen";
        public static final String CYCLE_ID = "cycle_id_gen";
        public static final String USER_ID = "user_id_gen";
    }

    @UtilityClass
    public class Sequence {
        public static final String AUTHOR_ID_SEQUENCE = "author_id_seq";
        public static final String BOOK_ID_SEQUENCE = "book_id_seq";
        public static final String SERIES_ID_SEQUENCE = "series_id_seq";
        public static final String CYCLE_ID_SEQUENCE = "cycle_id_seq";
        public static final String USER_ID_SEQUENCE = "user_id_seq";
    }

    @UtilityClass
    public class Table {

        @UtilityClass
        public class User {
            public static final String TABLE_NAME = "users";
        }

        @UtilityClass
        public class Author {
            public static final String CONSTRAINT_FULL_NAME = "author_full_name_uc";
        }

        @UtilityClass
        public class Book {
            public static final String CYCLE_FK = "book_cycle_id_fk";
            public static final String SERIES_FK = "book_series_id_fk";
        }

        @UtilityClass
        public class Series {
            public static final String CYCLE_FK = "series_cycle_id_fk";
        }

        @UtilityClass
        public class BookAuthor {
            public static final String AUTHOR_FK = "author_book_author_id_fk";
            public static final String BOOK_FK = "author_book_book_id_fk";
            public static final String CONSTRAINT_BOOK_AUTHOR = "book_id_author_id_uc";
        }
    }
}
