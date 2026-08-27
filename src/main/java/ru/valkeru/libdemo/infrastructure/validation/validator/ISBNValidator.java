package ru.valkeru.libdemo.infrastructure.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import ru.valkeru.libdemo.infrastructure.validation.annotation.ValidISBN13;
import ru.valkeru.libdemo.util.ISBNUtil;

import static ru.valkeru.libdemo.util.ISBNUtil.VALID_LENGTH;
import static ru.valkeru.libdemo.util.ISBNUtil.VALID_LENGTH_FORMATTED;

public class ISBNValidator implements ConstraintValidator<ValidISBN13, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value) || value.length() != VALID_LENGTH_FORMATTED) {
            return true;
        }

        // Remove hyphens and spaces
        String isbn = StringUtils.replaceChars(value, "- ", "");
        if (isbn.length() != VALID_LENGTH || !StringUtils.isNumeric(isbn)) {
            return false;
        }

        int control = ISBNUtil.extractDigit(isbn, VALID_LENGTH - 1);
        int expected = ISBNUtil.calculateISBN13Control(isbn.substring(0, VALID_LENGTH));

        return expected == control;
    }
}
