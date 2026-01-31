package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.valkeru.libdemo.ApplicationTestConfiguration;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ApplicationTestConfiguration.class)
@AutoConfigureMockMvc
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateBookBadRequestAuthorNotSet() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_f81355e59a",
                                          "isbn": "978-5-17-049678-5"
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
    void testCreateBookBadRequestInvalidIsbn() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_8ab9dd865a",
                                          "isbn": "978-5-17-049678",
                                          "authors": [
                                            {
                                              "id": 1
                                            }
                                          ]
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
    void testCreateBookAuthorNotFound() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_f81355e59a",
                                          "isbn": "978-5-17-049678-5",
                                          "authors": [
                                            {
                                              "id": 1
                                            }
                                          ]
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
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    void testCreateBookSeriesNotFound() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_dc0ecee69f",
                                          "isbn": "978-5-17-049678-5",
                                          "authors": [
                                            {
                                              "id": "84c1599c-21e6-47f3-a03b-12f6071da20b"
                                            }
                                          ],
                                          "series": {
                                            "id": "91be1c7e-4b02-4d27-966a-815f9bf20369"
                                          }
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
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql"
            }
    )
    void testCreateBookCycleNotFound() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_dc0ecee69f",
                                          "isbn": "978-5-17-049678-5",
                                          "authors": [
                                            {
                                              "id": "84c1599c-21e6-47f3-a03b-12f6071da20b"
                                            }
                                          ],
                                          "cycle": {
                                            "id": "1b3ab36d-6bb6-4081-9ec3-8d845ce55e5d"
                                          }
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
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql"
            }
    )
    void testCreateBookOk() throws Exception {
        mockMvc.perform(
                        post("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_8ae3cd18ca",
                                          "isbn": "978-5-17-049678-5",
                                          "authors": [
                                            {
                                              "id": "84c1599c-21e6-47f3-a03b-12f6071da20b"
                                            }
                                          ],
                                          "series": {
                                            "id": "697792d6-8d57-4d6f-9ea2-c91b01159612"
                                          },
                                          "cycle": {
                                            "id": "7cc6be9b-7649-4955-bff9-8cbf7c4c429a"
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("test_8ae3cd18ca"))
                .andExpect(jsonPath("$.isbn").value("978-5-17-049678-5"))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].id").value("84c1599c-21e6-47f3-a03b-12f6071da20b"))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").exists())
                .andExpect(jsonPath("$.series.id").value("697792d6-8d57-4d6f-9ea2-c91b01159612"))
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").exists())
                .andExpect(jsonPath("$.cycle.id").value("7cc6be9b-7649-4955-bff9-8cbf7c4c429a"))
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testListBooksEmptyList() throws Exception {
        mockMvc.perform(
                        get("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(0))
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
    void testListBooksOk() throws Exception {
        mockMvc.perform(
                        get("/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("test_d29827772a"))
                .andExpect(jsonPath("$[0].authors.size()").value(1))
                .andExpect(jsonPath("$[0].authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$[0].series").isNotEmpty())
                .andExpect(jsonPath("$[0].series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$[0].cycle").isNotEmpty())
                .andExpect(jsonPath("$[0].cycle.name").value("test_9411799dad"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
            }
    )
    void testGetBookNotFound() throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
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
    void testGetBookOk() throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_d29827772a"))
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
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
    void testUpdateBookBadRequestAuthorsNotSet() throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_d29827772a"))
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());

        mockMvc.perform(
                        patch("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_99827d8120",
                                          "isbn": "978-5-17-049678-5",
                                          "series": {
                                            "id": 2
                                          },
                                          "cycle": {
                                            "id": 2
                                          }
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
                    "classpath:01.create_author.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
                    "classpath:04.create_book.sql"
            }
    )
    void testUpdateBookBadRequestInvalidIsbn() throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_d29827772a"))
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());

        mockMvc.perform(
                        patch("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_99827d8120",
                                          "isbn": "978-5-17-049678-",
                                          "authors": [
                                            {
                                              "id": 2
                                            },
                                            {
                                              "id": 3
                                            }
                                          ],
                                          "series": {
                                            "id": 2
                                          },
                                          "cycle": {
                                            "id": 2
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @ParameterizedTest
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:01.create_author.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
                    "classpath:04.create_book.sql"
            }
    )
    @ValueSource(strings = {
            """
                    {
                      "name": "test_99827d8120",
                      "isbn": "978-5-17-049678-5",
                      "authors": [{"id": 5},{"id": 6}],
                      "series": {"id": 2},
                      "cycle": {"id": 2}
                    }
                    """,
            """
                    {
                      "name": "test_99827d8120",
                      "isbn": "978-5-17-049678-5",
                      "authors": [{"id": 2},{"id": 3}],
                      "series": {"id": 5},
                      "cycle": {"id": 2}
                    }
                    """,
            """
                    {
                      "name": "test_99827d8120",
                      "isbn": "978-5-17-049678-5",
                      "authors": [{"id": 2},{"id": 3}],
                      "series": {"id": 2},
                      "cycle": {"id": 5}
                    }
                    """

    })
    void testUpdateBookNotFound(String value) throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_d29827772a"))
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());

        mockMvc.perform(
                        patch("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(value)
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
    void testUpdateBookOk() throws Exception {
        mockMvc.perform(
                        get("/v1/books/{id}", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_d29827772a"))
                .andExpect(jsonPath("$.authors.size()").value(1))
                .andExpect(jsonPath("$.authors[0].fullName").value("test_9b844b884b test_90321cca80 test_6012cf646d"))
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_42db2cab8e"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());

        mockMvc.perform(
                        patch("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_99827d8120",
                                          "isbn": "978-5-17-049678-5",
                                          "authors": [
                                            {
                                              "id": 2
                                            },
                                            {
                                              "id": 3
                                            }
                                          ],
                                          "series": {
                                            "id": 2
                                          },
                                          "cycle": {
                                            "id": 2
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_99827d8120"))
                .andExpect(jsonPath("$.authors.size()").value(2))
                .andExpect(jsonPath("$.authors[*].fullName", containsInAnyOrder(
                                        "test_b562cf1fae test_5f170fac03 test_a713816ac7",
                                        "test_f7ba5093a9 test_006df0bfd6 test_01529219d0"
                                )
                        )
                )
                .andExpect(jsonPath("$.series").isNotEmpty())
                .andExpect(jsonPath("$.series.name").value("test_e2506e0e1a"))
                .andExpect(jsonPath("$.cycle").isNotEmpty())
                .andExpect(jsonPath("$.cycle.name").value("test_ba77861515"))
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
    void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(
                        delete("/v1/books/{id}", 10)
                                .accept(MediaType.APPLICATION_JSON)
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
    void testDeleteBookOk() throws Exception {
        mockMvc.perform(
                        delete("/v1/books/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
