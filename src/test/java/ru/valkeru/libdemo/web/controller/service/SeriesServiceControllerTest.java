package ru.valkeru.libdemo.web.controller.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.constants.TestConstants;
import ru.valkeru.libdemo.web.api.service.SeriesServiceApi;
import ru.valkeru.libdemo.web.controller.v1.SeriesController;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.valkeru.libdemo.constants.TestConstants.SERIES_ID;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class SeriesServiceControllerTest extends AbstractIntegrationTest {

    @ParameterizedTest
    @MethodSource("validationFailedArguments")
    @DisplayName("Create a series - 400")
    void testCreateSeriesBadRequest(String contentPath, String expectedResultPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        assertValidationFailed(
            performAsManager(post(SeriesServiceApi.SERIES_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            ),
            expected
        );
    }

    @Test
    @DisplayName("Create a series - 404")
    void testCreateSeriesCycleNotFound() throws Exception {
        String content = readResourceAsString("json/series/request/add_cycle_not_found.json");
        String expected = readResourceAsString("json/series/response/cycle_not_found.json");

        assertNotFound(
            performAsManager(post(SeriesServiceApi.SERIES_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            ),
            expected
        );
    }

    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
        }
    )
    @ParameterizedTest
    @MethodSource("validArguments")
    @DisplayName("Create a series - 201")
    void testCreateSeriesOk(String contentPath, String expectedPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedPath);

        MvcResult result = assertCreated(
            performAsManager(
                post(SeriesServiceApi.SERIES_SERVICE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
        )
            .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        assertOk(
            performNotAuthenticated(get(location)),
            expected
        );
    }

    @ParameterizedTest
    @MethodSource("validationFailedArguments")
    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
            "classpath:sql/series/insert.sql",
        }
    )
    @DisplayName("Update series - 400")
    void testUpdateSeriesBadRequest(String contentPath, String expectedResultPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        assertValidationFailed(
            performAsManager(
                patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            ),
            expected
        );
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
            "classpath:sql/series/insert.sql",
        }
    )
    @DisplayName("Update series, cycle not found - 404")
    void testUpdateSeriesCycleNotFound() throws Exception {
        String content = readResourceAsString("json/series/request/add_cycle_not_found.json");
        String expected = readResourceAsString("json/series/response/cycle_not_found.json");

        assertNotFound(
            performAsManager(patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            ),
            expected
        );
    }

    @ParameterizedTest
    @MethodSource("updateValidArguments")
    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
            "classpath:sql/series/insert.sql",
        }
    )
    @DisplayName("Update series - 200")
    void testUpdateSeriesOk(String contentPath, String expectedResultPath) throws Exception {
        String initial = readResourceAsString("json/series/request/series.json");
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        assertOk(
            performNotAuthenticated(get("/v1/series/{id}", SERIES_ID)
                .accept(MediaType.APPLICATION_JSON)
            ),
            initial
        );

        assertNoContent(
            performAsManager(
                patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
        );

        assertOk(
            performNotAuthenticated(get("%s/{id}".formatted(SeriesController.SERIES_V1_PATH), SERIES_ID)),
            expected
        );
    }

    @Test
    @DisplayName("Delete series - 404")
    void testDeleteSeriesNotFound() throws Exception {
        String expected = readResourceAsString("json/series/response/not_found.json");

        assertNotFound(
            performAsManager(
                delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), TestConstants.START_UUID_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            ),
            expected
        );
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql",
            "classpath:sql/05.book_to_series.sql"
        }
    )
    @DisplayName("Delete series - 409")
    void testDeleteSeriesConflict() throws Exception {
        String expected = readResourceAsString("json/conflict.json");

        assertConflict(
            performAsManager(delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                .accept(MediaType.APPLICATION_JSON)
            ),
            expected
        );
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    @DisplayName("Delete series - 200")
    void testDeleteSeriesOk() throws Exception {
        assertNoContent(
            performAsManager(
                delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                    .accept(MediaType.APPLICATION_JSON)
            )
        );
    }

    private static Stream<Arguments> validationFailedArguments() {
        return Stream.of(
            Arguments.of("json/series/request/add_title_blank.json", "json/series/response/validation_error.json"),
            Arguments.of("json/series/request/add_title_null.json", "json/series/response/validation_error.json")
        );
    }

    private static Stream<Arguments> validArguments() {
        return Stream.of(
            Arguments.of("json/series/request/add_valid_no_cycle.json", "json/series/response/created_no_cycle.json"),
            Arguments.of("json/series/request/add_valid_with_cycle.json", "json/series/response/created_with_cycle.json")
        );
    }

    private static Stream<Arguments> updateValidArguments() {
        return Stream.of(
            Arguments.of("json/series/request/update_no_cycle.json", "json/series/response/updated_no_cycle.json"),
            Arguments.of("json/series/request/update_with_cycle.json", "json/series/response/updated_with_cycle.json")
        );
    }
}
