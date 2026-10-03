package ru.valkeru.libdemo.web.controller.service;

import net.javacrumbs.jsonunit.core.Option;
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
import ru.valkeru.libdemo.constants.CommonConstants;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.valkeru.libdemo.constants.TestConstants.BOOK_ID;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class BookServiceControllerTest extends AbstractIntegrationTest {

    @ParameterizedTest
    @MethodSource("badRequestPaths")
    @DisplayName("Create a book - bad request")
    void testCreateBookBadRequest(String requestPath, String responsePath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(responsePath);

        assertValidationFailed(
            performAsManager(
                post("/service/book")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            ),
            expected,
            Option.IGNORING_ARRAY_ORDER
        );
    }

    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql",
            "classpath:sql/04.create_book.sql"
        }
    )
    @ParameterizedTest
    @MethodSource("notFoundPaths")
    @DisplayName("Create a book - resource not found")
    void testCreateBookNotFound(String requestPath, String expectedPath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(expectedPath);

        assertNotFound(
            performAsManager(
                post("/service/book")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            ),
            expected
        );
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql",
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    @DisplayName("Create a book - success")
    void testCreateBookOk() throws Exception {
        String request = readResourceAsString("json/book/request/create_request_valid.json");

        MvcResult mvcResult = assertCreated(
            performAsManager(
                post("/service/book")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
        )
            .andReturn();

        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        String response = readResourceAsString("json/book/response/service/book_created.json");
        assertOk(performNotAuthenticated(get(location)), response);
    }

    @Test
    @DisplayName("Create a book, unauthorized - 401")
    void testCreateBookUnauthorized() throws Exception {
        String request = readResourceAsString("json/book/request/create_request_valid.json");

        assertNotAuthorized(performNotAuthenticated(
            post("/service/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ));

    }

    @Test
    @DisplayName("Create a book, lack of permissions - 403")
    void testCreateBookForbidden() throws Exception {
        String request = readResourceAsString("json/book/request/create_request_valid.json");

        assertForbidden(performAsLibrarian(
            post("/service/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ));

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
    @DisplayName("Update a book - 400")
    void testUpdateBookBadRequest(String requestPath, String responsePath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(responsePath);

        assertValidationFailed(
            performAsManager(
                patch("/service/book/{id}", BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            ),
            expected,
            Option.IGNORING_ARRAY_ORDER
        );
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
    @MethodSource("notFoundPaths")
    @DisplayName("Update a book - 404")
    void testUpdateBookNotFound(String requestPath, String expectedPath) throws Exception {
        String request = readResourceAsString(requestPath);
        String expected = readResourceAsString(expectedPath);

        assertNotFound(
            performAsManager(
                patch("/service/book/{id}", BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
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
            "classpath:sql/04.create_book.sql"
        }
    )
    @DisplayName("Update a book - success")
    void testUpdateBookOk() throws Exception {
        String request = readResourceAsString("json/book/request/update_valid_request.json");

        assertNoContent(
            performAsManager(
                patch("/service/book/{id}", BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
        );
    }

    @Test
    @DisplayName("Update a book - 401")
    void testUpdateBookUnauthorized() throws Exception {
        String request = readResourceAsString("json/book/request/update_valid_request.json");

        assertNotAuthorized(
            performNotAuthenticated(
                patch("/service/book/{id}", BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
        );
    }

    @Test
    @DisplayName("Update a book - 403")
    void testUpdateBookForbidden() throws Exception {
        String request = readResourceAsString("json/book/request/update_valid_request.json");

        assertForbidden(
            performAsLibrarian(
                patch("/service/book/{id}", BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
        );
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
    @DisplayName("Delete a book - 404")
    void testDeleteBookNotFound() throws Exception {
        String expected = readResourceAsString("json/book/response/service/not_found.json");

        assertNotFound(
            performAsManager(
                delete("/service/book/{id}", CommonConstants.UUID_START_VALUE)
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
            "classpath:sql/04.create_book.sql"
        }
    )
    @DisplayName("Delete a book - OK")
    void testDeleteBookOk() throws Exception {
        assertNoContent(
            performAsManager(
                delete("/service/book/{id}", BOOK_ID)
            )
        );
    }

    @Test
    @DisplayName("Delete a book - 401")
    void testDeleteBookUnauthorized() throws Exception {
        assertNotAuthorized(
            performNotAuthenticated(
                delete("/service/book/{id}", BOOK_ID)
            )
        );
    }

    @Test
    @DisplayName("Delete a book - 403")
    void testDeleteBookForbidden() throws Exception {
        assertForbidden(
            performAsLibrarian(
                delete("/service/book/{id}", BOOK_ID)
            )
        );
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
    private static Stream<Arguments> notFoundPaths() {
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
