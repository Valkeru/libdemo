package ru.valkeru.libdemo.infrastructure.initializer;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class AuthorNameGenerator {

    private static final RandomUtils randomUtils = RandomUtils.insecure();

    private static final int middleNamesCountMin = 1;
    private static final int middleNamesCountMax = 5;

    public String generateFirstName() {
        return FIRST_NAMES.get(randomUtils.randomInt(0, FIRST_NAMES.size()));
    }

    public String generateLastName() {
        return LAST_NAMES.get(randomUtils.randomInt(0, LAST_NAMES.size()));
    }

    /**
     * Generates middle name excluding first and last names
     * @param firstName Author's first name
     * @param lastName Author's last name
     */
    public String generateMiddleName(@NonNull String firstName, @NonNull String lastName) {
        int middleNamesCountMaxExclusive = middleNamesCountMax + 1;

        int middleNamesCount = randomUtils.randomInt(0, 5) < 3 // Condition for author has no middle name
            ? 0
            : randomUtils.randomInt(middleNamesCountMin, middleNamesCountMaxExclusive);

        Set<String> middleNameParts = new HashSet<>();
        while (middleNameParts.size() < middleNamesCount) {
            String part = MIDDLE_NAMES.get(randomUtils.randomInt(0, MIDDLE_NAMES.size()));
            if (part.equals(firstName) || part.equals(lastName)) {
                continue;
            }

            middleNameParts.add(part);
        }

        return StringUtils.join(middleNameParts, StringUtils.SPACE);
    }

    private static final List<String> FIRST_NAMES = List.of(
        "James",
        "John",
        "Robert",
        "Michael",
        "William",
        "David",
        "Richard",
        "Joseph",
        "Thomas",
        "Charles",
        "Christopher",
        "Daniel",
        "Matthew",
        "Anthony",
        "Donald",
        "Mark",
        "Paul",
        "Steven",
        "Andrew",
        "Kenneth",
        "George",
        "Joshua",
        "Kevin",
        "Brian",
        "Edward",
        "Ronald",
        "Timothy",
        "Jason",
        "Jeffrey",
        "Ryan",
        "Jacob",
        "Gary",
        "Nicholas",
        "Eric",
        "Jonathan",
        "Stephen",
        "Larry",
        "Justin",
        "Scott",
        "Brandon",
        "Benjamin",
        "Samuel",
        "Gregory",
        "Frank",
        "Alexander",
        "Raymond",
        "Patrick",
        "Jack",
        "Dennis",
        "Jerry"
    );

    private static final List<String> LAST_NAMES = List.of(
        "Smith",
        "Johnson",
        "Williams",
        "Brown",
        "Jones",
        "Garcia",
        "Miller",
        "Davis",
        "Rodriguez",
        "Martinez",
        "Hernandez",
        "Lopez",
        "Gonzalez",
        "Wilson",
        "Anderson",
        "Thomas",
        "Taylor",
        "Moore",
        "Jackson",
        "Martin",
        "Lee",
        "Perez",
        "Thompson",
        "White",
        "Harris",
        "Sanchez",
        "Clark",
        "Ramirez",
        "Lewis",
        "Robinson",
        "Walker",
        "Young",
        "Allen",
        "King",
        "Wright",
        "Scott",
        "Torres",
        "Nguyen",
        "Hill",
        "Flores",
        "Green",
        "Adams",
        "Nelson",
        "Baker",
        "Hall",
        "Rivera",
        "Campbell",
        "Mitchell",
        "Carter",
        "Roberts"
    );

    private static final List<String> MIDDLE_NAMES = List.of(
        "James",
        "John",
        "Robert",
        "Michael",
        "William",
        "Alexander",
        "Thomas",
        "Edward",
        "Joseph",
        "Charles",
        "Henry",
        "Arthur",
        "George",
        "Samuel",
        "Daniel",
        "Benjamin",
        "Matthew",
        "Andrew",
        "Christopher",
        "Nicholas"
    );
}
