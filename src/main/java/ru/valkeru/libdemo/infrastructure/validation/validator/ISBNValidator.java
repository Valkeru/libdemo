package ru.valkeru.libdemo.infrastructure.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import ru.valkeru.libdemo.infrastructure.validation.annotation.ValidISBN13;

public class ISBNValidator implements ConstraintValidator<ValidISBN13, String> {

    private static final int VALID_LENGTH = 13;
    private static final int[] MULTIPLIERS = new int[]{1, 3};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        String isbn = value.replace("-", "");
        if (isbn.length() != VALID_LENGTH || !StringUtils.isNumeric(isbn)) {
            return false;
        }

        int control = extractDigit(isbn, VALID_LENGTH - 1);
        int sum = 0;
        for (int i = 0; i < VALID_LENGTH - 1; i++) {
            sum += extractDigit(isbn, i) * MULTIPLIERS[i % 2];
        }

        int expected = (10 - sum % 10) % 10;

        return expected == control;
    }

    int extractDigit(String isbn, int position) {
        return isbn.charAt(position) - '0';
    }
}
