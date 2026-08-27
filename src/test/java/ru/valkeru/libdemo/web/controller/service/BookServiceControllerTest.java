package ru.valkeru.libdemo.web.controller.service;

import net.javacrumbs.jsonunit.core.Option;
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
import ru.valkeru.libdemo.constants.CommonConstants;

import java.util.UUID;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class BookServiceControllerTest extends AbstractIntegrationTest {

    private static final UUID BOOK_ID = UUID.fromString("989b056f-31ef-4682-8f64-a21743939aab");

    @ParameterizedTest
    @MethodSource("badRequestPaths")
    void testCreateBookBadRequest(String requestPath, String responsePath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(responsePath);

        performAsManager(
            post("/service/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest())
            .andExpect(json().when(Option.IGNORING_ARRAY_ORDER).isEqualTo(expected));
    }

    @Test
    void testCreateBookAuthorNotFound() throws Exception {
        String request = readResourceAsString("json/book/request/not_existed_author_id.json");
        String expected = readResourceAsString("json/author/response/not_found.json");

        performAsManager(
            post("/service/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isNotFound())
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    void testCreateBookSeriesNotFound() throws Exception {
        String request = readResourceAsString("json/book/request/not_existed_series_id.json");
        String expected = readResourceAsString("json/series/response/not_found.json");

        performAsManager(
            post("/service/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isNotFound())
            .andExpect(json().isEqualTo(expected))
            .andDo(print());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    void testCreateBookCycleNotFound() throws Exception {
        String request = readResourceAsString("json/book/request/not_existed_cycle_id.json");
        String expected = readResourceAsString("json/cycle/response/not_found.json");

        performAsManager(
            post("/service/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isNotFound())
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    void testCreateBookOk() throws Exception {
        String request = readResourceAsString("json/book/request/create_request_valid.json");

        MvcResult mvcResult = performAsManager(
            post("/service/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andReturn();

        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        String response = readResourceAsString("json/book/response/service/book_created.json");
        performNotAuthenticated(get(location))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(response));
    }

    @ParameterizedTest
    @MethodSource("badRequestPaths")
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    void testUpdateBookBadRequest(String requestPath, String responsePath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(responsePath);

        performAsManager(
            patch("/service/book/{id}", BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest())
            .andExpect(json().when(Option.IGNORING_ARRAY_ORDER).isEqualTo(expected));
    }

    @ParameterizedTest
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    @MethodSource("updateNotFoundPaths")
    void testUpdateBookNotFound(String requestPath, String expectedPath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(expectedPath);

        performAsManager(
            patch("/service/book/{id}", BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isNotFound())
            .andExpect(json().isEqualTo(expected));

    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    void testUpdateBookOk() throws Exception {
        String request = readResourceAsString("json/book/request/update_valid_request.json");

        performAsManager(
            patch("/service/book/{id}", BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isNoContent());
    }


    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    void testDeleteBookNotFound() throws Exception {
        performAsManager(
            delete("/service/book/{id}", CommonConstants.UUID_START_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    void testDeleteBookOk() throws Exception {
        performAsManager(
            delete("/service/book/{id}", BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    /**
     * Bad request files paths. Order is "request, response"
     */
    private static Stream<Arguments> badRequestPaths() {
        return Stream.of(
            Arguments.of(
                "json/book/request/create_invalid_request.json",
                "json/book/response/service/create_validation_failed.json"
            )
        );
    }

    /**
     * Not found files paths. Order is "request, response"
     */
    private static Stream<Arguments> updateNotFoundPaths() {
        return Stream.of(
            Arguments.of(
                "json/book/request/not_existed_author_id.json",
                "json/author/response/not_found.json"
            ),
            Arguments.of(
                "json/book/request/not_existed_series_id.json",
                "json/series/response/not_found.json"
            ),
            Arguments.of(
                "json/book/request/not_existed_cycle_id.json",
                "json/cycle/response/not_found.json"
            )
        );
    }
}
