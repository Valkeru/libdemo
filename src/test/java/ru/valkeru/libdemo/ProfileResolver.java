package ru.valkeru.libdemo;

import org.jspecify.annotations.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;
import ru.valkeru.libdemo.constants.ApplicationProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ProfileResolver implements ActiveProfilesResolver {

    @Override
    public String @NonNull [] resolve(@NonNull Class<?> testClass) {
        String[] profiles = Optional.ofNullable(System.getenv("SPRING_PROFILES_ACTIVE"))
            .map(s -> s.split(","))
            .orElse(new String[0]);

        Set<String> effectiveProfiles = new HashSet<>(Arrays.asList(profiles));
        effectiveProfiles.add(ApplicationProfiles.PROFILE_TEST);

        return effectiveProfiles.toArray(String[]::new);
    }
}
