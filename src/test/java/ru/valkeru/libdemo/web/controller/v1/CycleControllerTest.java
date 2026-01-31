package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.valkeru.libdemo.ApplicationTestConfiguration;

import java.util.UUID;

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
class CycleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testCreateCycleBadRequest() throws Exception {
        mockMvc.perform(
                        post("/v1/cycle")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                          {
                                            "name": null
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
    void testCreateCycleOk() throws Exception {
        mockMvc.perform(
                        get("/v1/cycle")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());

        mockMvc.perform(
                        post("/v1/cycle")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                          {
                                            "name": "test_a480bc0a5a"
                                          }
                                        """
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("test_a480bc0a5a"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testGetCycleNotFound() throws Exception {
        mockMvc.perform(
                        get("/v1/cycle/{cycleId}", "42860c68-21fe-438e-95ae-4555c35f5150")
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testGetCyclesOk() throws Exception {
        mockMvc.perform(
                        get("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("7cc6be9b-7649-4955-bff9-8cbf7c4c429a"))
                .andExpect(jsonPath("$.name").value("test_9411799dad"))
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql"
            }
    )
    void testUpdateCycleNotFound() throws Exception {
        mockMvc.perform(
                        patch("/v1/cycle/{cycleId}", "42860c68-21fe-438e-95ae-4555c35f5150")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test"
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
                    "classpath:02.create_cycle.sql"
            }
    )
    void testUpdateCycleOk() throws Exception {
        mockMvc.perform(
                        patch("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                         "name": "test_778a3b8b2f"
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Sql(value = {"classpath:delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteCycleNotFound() throws Exception {
        mockMvc.perform(
                        delete("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {"classpath:02.create_cycle.sql", "classpath:03.create_series.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testDeleteCycleSeriesConflict() throws Exception {
        mockMvc.perform(
                        delete("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:01.create_author.sql",
                    "classpath:02.create_cycle.sql",
                    "classpath:03.create_series.sql",
                    "classpath:04.create_book.sql"
            }
    )
    void testDeleteCycleBookConflict() throws Exception {
        mockMvc.perform(
                        delete("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Sql(
            value = {
                    "classpath:delete/00.truncate.sql",
                    "classpath:02.create_cycle.sql"
            }
    )
    void testDeleteCycleOk() throws Exception {
        mockMvc.perform(
                        delete("/v1/cycle/{cycleId}", "7cc6be9b-7649-4955-bff9-8cbf7c4c429a")
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
