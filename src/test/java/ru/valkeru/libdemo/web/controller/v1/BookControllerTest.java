package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.constants.CommonConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class BookControllerTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Books - empty list")
    void testListBooksEmptyList() throws Exception {
        String expected = readResourceAsString("json/empty_list.json");

        assertOk(
            performNotAuthenticated(
                get("/v1/books")
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
            "classpath:sql/04.create_book.sql"
        }
    )
    @DisplayName("Books list - OK")
    void testListBooksOk() throws Exception {
        String expected = readResourceAsString("json/book/response/list_ok.json");

        assertOk(
            performNotAuthenticated(
                get("/v1/books")
                    .accept(MediaType.APPLICATION_JSON)
            ),
            expected
        );
    }

    @Test
    @DisplayName("Get book - not found")
    void testGetBookNotFound() throws Exception {
        assertNotFound(
            performNotAuthenticated(
                get("/v1/books/{id}", CommonConstants.UUID_START_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            ),
            readResourceAsString("json/book/response/service/not_found.json")
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
    @DisplayName("Get book - OK")
    void testGetBookOk() throws Exception {
        String expected = readResourceAsString("json/book/response/get_ok.json");

        assertOk(
            performNotAuthenticated(
                get("/v1/books/{id}", "989b056f-31ef-4682-8f64-a21743939aab")
                    .accept(MediaType.APPLICATION_JSON)
            ),
            expected
        );
    }
}
