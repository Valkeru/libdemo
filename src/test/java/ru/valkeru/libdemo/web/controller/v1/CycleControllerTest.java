package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.web.api.service.CycleServiceApi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.valkeru.libdemo.constants.TestConstants.CYCLE_ID;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class CycleControllerTest extends AbstractIntegrationTest {


    @Test
    void testCreateCycleBadRequest() throws Exception {
        performAsAdmin(post(CycleServiceApi.CYCLE_SERVICE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                          {
                            "title": null
                          }
                        """
            ))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCycleOk() throws Exception {
        performNotAuthenticated(
            get("/v1/cycle")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()").value(0));

        MvcResult result = performAsAdmin(
            post(CycleServiceApi.CYCLE_SERVICE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                            "title": "test_a480bc0a5a"
                          }
                        """
                )
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        performAsAdmin(
            get(location).accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test_a480bc0a5a"));
    }

    @Test
    void testGetCycleNotFound() throws Exception {
        performNotAuthenticated(
            get("/v1/cycle/{cycleId}", "42860c68-21fe-438e-95ae-4555c35f5150")
        )
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testGetCyclesOk() throws Exception {
        performNotAuthenticated(
            get("/v1/cycle/{cycleId}", CYCLE_ID)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(CYCLE_ID))
            .andExpect(jsonPath("$.title").value("test_9411799dad"));
    }

    @Test
    void testUpdateCycleNotFound() throws Exception {
        performAsAdmin(patch("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), "42860c68-21fe-438e-95ae-4555c35f5150")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "title": "test"
                        }
                        """
            )
        )
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql"
        }
    )
    void testUpdateCycleOk() throws Exception {
        performAsAdmin(
            patch("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), CYCLE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "title": "test_778a3b8b2f"
                        }
                        """
                )
        )
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCycleNotFound() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), CYCLE_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    void testDeleteCycleSeriesConflict() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), CYCLE_ID))
            .andExpect(status().isConflict());
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
    void testDeleteCycleBookConflict() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), CYCLE_ID))
            .andExpect(status().isConflict());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql"
        }
    )
    void testDeleteCycleOk() throws Exception {
        performAsAdmin(delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_PATH), CYCLE_ID))
            .andExpect(status().isNoContent());
    }
}
