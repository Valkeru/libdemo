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
@Import(ApplicationTestConfiguration.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:delete/00.truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "classpath:delete/00.truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class SeriesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateSeriesBadRequest() throws Exception {
        mockMvc.perform(
                        post("/series")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": null
                                        }
                                        """)
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
    void testCreateSeriesCycleNotFound() throws Exception {
        mockMvc.perform(
                        post("/series")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_58c6ac3d30",
                                          "cycle": {
                                            "id": 1
                                          }
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateSeriesNoCycleOk() throws Exception {
        mockMvc.perform(
                        post("/series")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "test_23e17f5cca"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value("test_23e17f5cca"))
                .andExpect(jsonPath("$.cycle").isEmpty())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql"
            }
    )
    void testCreateSeriesOk() throws Exception {
        mockMvc.perform(
                        post("/series")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "test_23e17f5cca",
                                            "cycle": {
                                              "id": 1
                                            }
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test_23e17f5cca"))
                .andExpect(jsonPath("$.cycle.id").isNumber())
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
            }
    )
    void testUpdateSeriesBadRequest() throws Exception {
        mockMvc.perform(
                        patch("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "name": "test_d9c8030db4"
                                                }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
            }
    )
    void testUpdateSeriesCycleNotFound() throws Exception {
        mockMvc.perform(
                        get("/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        mockMvc.perform(
                        patch("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_5fed320cf3",
                                          "cycle": {
                                            "id": 3
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
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
            }
    )
    void testUpdateSeriesOk() throws Exception {
        mockMvc.perform(
                        get("/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        mockMvc.perform(
                        patch("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_4233baa1c1",
                                          "cycle": {
                                            "id": 2
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test_4233baa1c1"))
                .andExpect(jsonPath("$.cycle").exists())
                .andExpect(jsonPath("$.cycle.id").value(2))
                .andExpect(jsonPath("$.cycle.name").value("test_ba77861515"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
            }
    )
    void testUpdateSeriesNoCycleOk() throws Exception {
        mockMvc.perform(
                        get("/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        mockMvc.perform(
                        patch("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_b7b7e7577d"
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test_b7b7e7577d"))
                .andExpect(jsonPath("$.cycle").isEmpty())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testGetSeriesNotFound() throws Exception {
        mockMvc.perform(
                        get("/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());

        mockMvc.perform(
                        get("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql"
            }
    )
    void testGetSeriesOk() throws Exception {
        mockMvc.perform(
                        get("/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());

        mockMvc.perform(
                        get("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_42db2cab8e"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testDeleteSeriesNotFound() throws Exception {
        mockMvc.perform(
                        delete("/series/{seriesId}", 1)
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
    void testDeleteSeriesConflict() throws Exception {
        mockMvc.perform(
                        delete("/series/{seriesId}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql"
            }
    )
    void testDeleteSeriesOk() throws Exception {
        mockMvc.perform(
                        delete("/series/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
