package ru.valkeru.libdemo.domain.utility;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class LibraryCardNumberGenerator {

    @Value("${app.policy.library-card-number-prefix}")
    private String libraryCardNumberPrefix;

    @Value("${app.policy.library-code}")
    private String libraryCode;

    @Value("${app.policy.library-card-number-batch-size}")
    private int sequenceIncrementValue;

    private final AtomicLong currentValue = new AtomicLong();
    private final AtomicLong maxValue = new AtomicLong();

    private final JdbcClient jdbcClient;

    @PostConstruct
    void check() {
        if (sequenceIncrementValue < 0) {
            throw new IllegalArgumentException("Batch size must be greater then 0");
        }
    }

    public String generateLibraryCardNumber() {
        long sequentialValue = getNextSequenceValue();

        String payload = String.format("%s%s%08d", libraryCardNumberPrefix, libraryCode, sequentialValue);
        int control = calculateLuhnMod10Check(payload);

        return payload + control;
    }

    private long getNextSequenceValue() {
        while (true) {
            long current = currentValue.get();
            long max = maxValue.get();

            if (current < max) {
                if (currentValue.compareAndSet(current, current + 1)) {
                    return current;
                }

                // Other thread had modified current value, go next iteration
                continue;
            }

            synchronized (this) {
                if (currentValue.get() >= maxValue.get()) {
                    long next = getNextCurrentValueFromDb();

                    currentValue.set(next);
                    maxValue.set(next + sequenceIncrementValue);
                }
            }
        }
    }

    private long getNextCurrentValueFromDb() {
        return jdbcClient.sql("""
            SELECT nextval('library.library_card_number_seq')
        """)
            .query(long.class)
            .single();
    }

    /**
     * Calculate a check digit for payload
     * @param payload String containing payload only and not containing check number
     */
    private int calculateLuhnMod10Check(String payload) {
        int sum = 0;
        boolean even = true;

        for (int i = payload.length() - 1; i >= 0; i--) {
            int digit = payload.charAt(i) - '0';

            if ( even ) {
                digit <<= 1;
            }
            if ( digit > 9 ) {
                digit -= 9;
            }
            sum += digit;
            even = !even;
        }

        return ( 10 - ( sum % 10 ) ) % 10;
    }
}
