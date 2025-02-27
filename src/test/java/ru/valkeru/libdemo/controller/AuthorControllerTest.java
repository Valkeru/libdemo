package ru.valkeru.libdemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.valkeru.libdemo.ApplicationTestConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(ApplicationTestConfiguration.class)
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateAuthorBadRequest() throws Exception {
        mockMvc.perform(
                        post("/author")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "firstName": "test_18287525cd",
                                              "middleName": "test_1812e70ce1"
                                            }
                                        """
                                )
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateAuthorOk() throws Exception {
        mockMvc.perform(
                        post("/author")
                                .accept(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("$.id").isNumber())
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
    void testCreateAuthorConflict() throws Exception {
        mockMvc.perform(
                        post("/author")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "test_9b844b884b",
                                          "middleName": "test_90321cca80",
                                          "lastName": "test_6012cf646d"
                                        }
                                        """
                                )
                )
                .andExpect(status().isConflict());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testGetAuthorNotFound() throws Exception {
        mockMvc.perform(get("/author/{authorId}", Integer.MAX_VALUE))
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
    void testAuthorsListOk() throws Exception {
        mockMvc.perform(
                get("/author")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    void testGetAuthorOk() throws Exception {
        mockMvc.perform(
                        get("/author/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("test_9b844b884b"))
                .andExpect(jsonPath("$.middleName").value("test_90321cca80"))
                .andExpect(jsonPath("$.lastName").value("test_6012cf646d"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testUpdateAuthorNotFound() throws Exception {
        mockMvc.perform(
                        patch("/author/{authorId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "test_9b844b884b",
                                          "middleName": "test_90321cca80",
                                          "lastName": "test_6b15281eae"
                                        }
                                        """
                                )
                )
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @Sql(
            value = {
                    "classpath:01.create_author.sql"
            }
    )
    void testUpdateAuthorBadRequest() throws Exception {
        mockMvc.perform(
                        get("/author/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(
                        patch("/author/{authorId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": null,
                                          "middleName": null,
                                          "lastName": null
                                        }
                                        """
                                )
                )
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
    void testUpdateAuthorOk() throws Exception {
        mockMvc.perform(
                        get("/author/{authorId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(
                        patch("/author/{authorId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "test_977de1b89b",
                                          "middleName": "test_f17ae88fac",
                                          "lastName": "test_1d44bb55e9"
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
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
    void deleteAuthorNotFound() throws Exception {
        mockMvc.perform(
                        delete("/author/{authorId}", 1)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
                    "classpath:04.create_book.sql"
            }
    )
    void deleteAuthorConflict() throws Exception {
        mockMvc.perform(
                        delete("/author/{authorId}", 1)
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    void deleteAuthorOk() throws Exception {
        mockMvc.perform(
                        delete("/author/{authorId}", 1)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
