package ru.valkeru.libdemo.web.controller.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.web.api.service.AuthorServiceApi;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AuthorServiceControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    @DisplayName("Добавить автора - некорректный запрос")
    void testCreateAuthorBadRequest() throws Exception {
        performAsLibrarian(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
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
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    @DisplayName("Добавить автора - успешно")
    void testCreateAuthorOk() throws Exception {
        performAsLibrarian(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
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
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.firstName").value("test_42046185e0"))
                .andExpect(jsonPath("$.middleName").exists())
                .andExpect(jsonPath("$.middleName").value("test_a1bf02cb32"))
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.lastName").value("test_2cd02a475c"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    @DisplayName("Добавить автора - конфликт данных")
    void testCreateAuthorConflict() throws Exception {
        performAsLibrarian(post(AuthorServiceApi.AUTHOR_SERVICE_URL)
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
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    @DisplayName("Обновить данные об авторе - 404")
    void testUpdateAuthorNotFound() throws Exception {
        performAsLibrarian(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), UUID.randomUUID())
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
    @Sql(
            value = {
                    "classpath:01.create_author.sql"
            }
    )
    @DisplayName("Обновить данные об авторе - 400")
    void testUpdateAuthorBadRequest() throws Exception {
        performAsLibrarian(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), "84c1599c-21e6-47f3-a03b-12f6071da20b")
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
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    @DisplayName("Обновить данные об авторе - успешно")
    void testUpdateAuthorOk() throws Exception {
//        mockMvc.perform(
//                        get("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), 1)
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());

        performAsLibrarian(patch("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), "84c1599c-21e6-47f3-a03b-12f6071da20b")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                        {
                                          "firstName": "test_977de1b89b",
                                          "middleName": "test_f17ae88fac",
                                          "lastName": "test_1d44bb55e9"
                                        }
                                        """
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("84c1599c-21e6-47f3-a03b-12f6071da20b"))
                .andExpect(jsonPath("$.firstName").value("test_977de1b89b"))
                .andExpect(jsonPath("$.middleName").value("test_f17ae88fac"))
                .andExpect(jsonPath("$.lastName").value("test_1d44bb55e9"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    @DisplayName("Удалить данные об авторе - 404")
    void deleteAuthorNotFound() throws Exception {
        performAsLibrarian(delete("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), UUID.randomUUID()))
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
    @DisplayName("Удалить данные об авторе - успешно")
    void deleteAuthorOk() throws Exception {
        performAsLibrarian(delete("%s/{id}".formatted(AuthorServiceApi.AUTHOR_SERVICE_URL), "84c1599c-21e6-47f3-a03b-12f6071da20b"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
