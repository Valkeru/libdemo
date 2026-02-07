package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.web.api.v1.AuthorApi;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AuthorControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    @DisplayName("Получить данные об авторе по ID - успешно")
    void testGetAuthorOk() throws Exception {
        performNotAuthenticated(
                get("%s/{id}".formatted(AuthorApi.AUTHOR_V1_URL), "84c1599c-21e6-47f3-a03b-12f6071da20b")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("84c1599c-21e6-47f3-a03b-12f6071da20b"))
                .andExpect(jsonPath("$.firstName").value("test_9b844b884b"))
                .andExpect(jsonPath("$.middleName").value("test_90321cca80"))
                .andExpect(jsonPath("$.lastName").value("test_6012cf646d"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Получить данные об авторе по ID - автор не найден")
    void testGetAuthorNotFound() throws Exception {
        performNotAuthenticated(get("%s/{id}".formatted(AuthorApi.AUTHOR_V1_URL), UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    @DisplayName("Получить список авторов")
    void testAuthorsListOk() throws Exception {
        performNotAuthenticated(get(AuthorApi.AUTHOR_V1_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].id").value("84c1599c-21e6-47f3-a03b-12f6071da20b"))
                .andExpect(jsonPath("$.content[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andDo(print());
    }
}
