package ru.valkeru.libdemo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ISBNUtil {

    public static final int VALID_LENGTH = 13;
    public static final int VALID_LENGTH_FORMATTED = 17;
    private static final int[] MULTIPLIERS = new int[]{1, 3};

    /**
     * Calculates control for ISBN-13
     * @param isbn ISBN or ISBN significant part
     * @return control number
     */
    public int calculateISBN13Control(String isbn) {
        int isbnLength = isbn.length();

        if (isbnLength > VALID_LENGTH) {
            throw new IllegalArgumentException("ISBN string is too long");
        }

        int significantPartLength = VALID_LENGTH - 1;
        if (isbnLength < significantPartLength) {
            throw new IllegalArgumentException("ISBN string size must be %d to %d".formatted(significantPartLength, VALID_LENGTH));
        }

        int sum = 0;
        for (int i = 0; i < significantPartLength; i++) {
            sum += extractDigit(isbn, i) * MULTIPLIERS[i % 2];
        }

        return (10 - sum % 10) % 10;
    }

    public int extractDigit(String isbn, int position) {
        return isbn.charAt(position) - '0';
    }
}
