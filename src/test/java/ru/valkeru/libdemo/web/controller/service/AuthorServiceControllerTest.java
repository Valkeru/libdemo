package ru.valkeru.libdemo.web.controller.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.web.api.service.AuthorServiceApi;
import ru.valkeru.libdemo.web.controller.v1.AuthorController;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorServiceControllerTest extends AbstractIntegrationTest {

    private static final String AUTHOR_ID = "84c1599c-21e6-47f3-a03b-12f6071da20b";

    @Test
    @DisplayName("Add an author - invalid request")
    void testCreateAuthorBadRequest() throws Exception {
        performAsAdmin(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                   "firstName": "test_18287525cd",
                   "middleName": "test_1812e70ce1"
                }
                """))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("Add an author - success")
    void testCreateAuthorOk() throws Exception {
        MvcResult result = performAsAdmin(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "firstName": "test_42046185e0",
                  "middleName": "test_a1bf02cb32",
                  "lastName": "test_2cd02a475c"
                }
                """)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists(CustomHeaders.RESOURCE_ID))
            .andReturn();

        String id = (String) result.getResponse().getHeaderValue(CustomHeaders.RESOURCE_ID);

        performNotAuthenticated(get("%s/{id}".formatted(AuthorController.AUTHOR_V1_URL), id))
            .andExpect(jsonPath("$.id").isString())
            .andExpect(jsonPath("$.firstName").exists())
            .andExpect(jsonPath("$.firstName").value("test_42046185e0"))
            .andExpect(jsonPath("$.middleName").exists())
            .andExpect(jsonPath("$.middleName").value("test_a1bf02cb32"))
            .andExpect(jsonPath("$.lastName").exists())
            .andExpect(jsonPath("$.lastName").value("test_2cd02a475c"));
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Add an author - data integrity violation")
    void testCreateAuthorConflict() throws Exception {
        performAsAdmin(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "firstName": "test_9b844b884b",
                  "middleName": "test_90321cca80",
                  "lastName": "test_6012cf646d"
                }
                """
            ))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Update author info - 404")
    void testUpdateAuthorNotFound() throws Exception {
        performAsAdmin(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "firstName": "test_9b844b884b",
                  "middleName": "test_90321cca80",
                  "lastName": "test_6b15281eae"
                }
                """
            ))
            .andExpect(status().isNotFound())
            .andDo(print());

    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Update author info - 400")
    void testUpdateAuthorBadRequest() throws Exception {
        performAsAdmin(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), AUTHOR_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "firstName": null,
                  "middleName": null,
                  "lastName": null
                }
                """
            ))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Update author info - success")
    void testUpdateAuthorOk() throws Exception {
        performNotAuthenticated(get("%s/{id}".formatted(AuthorController.AUTHOR_V1_URL), AUTHOR_ID))
            .andExpect(status().isOk())
            .andDo(print());

        performAsAdmin(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), AUTHOR_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "firstName": "test_977de1b89b",
                  "middleName": "test_f17ae88fac",
                  "lastName": "test_1d44bb55e9"
                }
                """
            ))
            .andExpect(status().isNoContent());

        performNotAuthenticated(get("%s/{id}".formatted(AuthorController.AUTHOR_V1_URL), AUTHOR_ID))
            .andExpect(jsonPath("$.id").value(AUTHOR_ID))
            .andExpect(jsonPath("$.firstName").value("test_977de1b89b"))
            .andExpect(jsonPath("$.middleName").value("test_f17ae88fac"))
            .andExpect(jsonPath("$.lastName").value("test_1d44bb55e9"))
            .andDo(print());
    }

    @Test
    @DisplayName("Delete author info - 404")
    void deleteAuthorNotFound() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Delete author info - success")
    void deleteAuthorOk() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), AUTHOR_ID))
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}
