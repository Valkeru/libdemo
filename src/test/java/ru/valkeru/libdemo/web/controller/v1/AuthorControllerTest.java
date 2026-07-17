package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.constants.TestConstants;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
    value = {
        "classpath:sql/delete/00.truncate.sql"
    }
)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class AuthorControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Get author info by ID - success")
    void testGetAuthorOk() throws Exception {
        String expected = readResourceAsString("json/author/response/author.json");

        performNotAuthenticated(
            get("/v1/author/{id}", "84c1599c-21e6-47f3-a03b-12f6071da20b")
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @DisplayName("Get author info by ID - author not found")
    void testGetAuthorNotFound() throws Exception {
        String expected = readResourceAsString("json/author/response/not_found.json");

        performNotAuthenticated(get("/v1/author/{id}", TestConstants.START_UUID_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Get authors list")
    void testAuthorsListOk() throws Exception {
        String expected = readResourceAsString("json/author/response/list.json");

        performNotAuthenticated(get("/v1/author"))
            .andExpect(status().isOk())
            .andExpect(json().isEqualTo(expected));
    }
}
