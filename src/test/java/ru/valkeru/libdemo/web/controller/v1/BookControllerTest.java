package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.constants.CommonConstants;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class BookControllerTest extends AbstractIntegrationTest {

    @Test
    void testListBooksEmptyList() throws Exception {
        String expected = readResourceAsString("json/empty_list.json");

        performNotAuthenticated(
            get("/v1/books")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
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
    void testListBooksOk() throws Exception {
        String expected = readResourceAsString("json/book/response/list_ok.json");

        performNotAuthenticated(
                get("/v1/books")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    void testGetBookNotFound() throws Exception {
        performNotAuthenticated(
                get("/v1/books/{id}", CommonConstants.UUID_START_VALUE)
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
    void testGetBookOk() throws Exception {
        String expected = readResourceAsString("json/book/response/get_ok.json");

        performNotAuthenticated(
                get("/v1/books/{id}", "989b056f-31ef-4682-8f64-a21743939aab")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(json().isEqualTo(expected));
    }
}
