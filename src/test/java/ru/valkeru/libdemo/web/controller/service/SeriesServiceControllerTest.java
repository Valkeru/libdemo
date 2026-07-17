package ru.valkeru.libdemo.web.controller.service;

import org.junit.jupiter.api.Assertions;
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

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.valkeru.libdemo.constants.TestConstants.SERIES_ID;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class SeriesServiceControllerTest extends AbstractIntegrationTest {

    @ParameterizedTest
    @MethodSource("validationFailedArguments")
    void testCreateSeriesBadRequest(String contentPath, String expectedResultPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        performAsAdmin(post(SeriesServiceApi.SERIES_SERVICE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    void testCreateSeriesCycleNotFound() throws Exception {
        String content = readResourceAsString("json/series/request/add_cycle_not_found.json");
        String expected = readResourceAsString("json/series/response/cycle_not_found.json");

        performAsAdmin(post(SeriesServiceApi.SERIES_SERVICE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        )
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
        }
    )
    @ParameterizedTest
    @MethodSource("validArguments")
    void testCreateSeriesOk(String contentPath, String expectedPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedPath);

        MvcResult result = performAsAdmin(
            post(SeriesServiceApi.SERIES_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        performNotAuthenticated(get(location))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
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
    void testUpdateSeriesBadRequest(String contentPath, String expectedResultPath) throws Exception {
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        performAsAdmin(
            patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/cycle/truncate.sql",
            "classpath:sql/cycle/insert.sql",
            "classpath:sql/series/insert.sql",
        }
    )
    void testUpdateSeriesCycleNotFound() throws Exception {
        String content = readResourceAsString("json/series/request/add_cycle_not_found.json");
        String expected = readResourceAsString("json/series/response/cycle_not_found.json");

        performAsAdmin(patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        )
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
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
    void testUpdateSeriesOk(String contentPath, String expectedResultPath) throws Exception {
        String initial = readResourceAsString("json/series/request/series.json");
        String content = readResourceAsString(contentPath);
        String expected = readResourceAsString(expectedResultPath);

        performNotAuthenticated(get("/v1/series/{id}", SERIES_ID)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(initial));

        performAsAdmin(
            patch("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isNoContent());

        performNotAuthenticated(get("%s/{id}".formatted(SeriesController.SERIES_V1_PATH), SERIES_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    void testDeleteSeriesNotFound() throws Exception {
        String expected = readResourceAsString("json/series/response/not_found.json");

        performAsAdmin(
            delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), TestConstants.START_UUID_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql",
            "classpath:sql/05.book_to_series.sql"
        }
    )
    void testDeleteSeriesConflict() throws Exception {
        String expected = readResourceAsString("json/conflict.json");

        performAsAdmin(delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isConflict())
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    void testDeleteSeriesOk() throws Exception {
        performAsAdmin(
            delete("%s/{id}".formatted(SeriesServiceApi.SERIES_SERVICE_PATH), SERIES_ID)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent());
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
