package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.valkeru.libdemo.constants.TestConstants.SERIES_ID;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class SeriesControllerTest extends AbstractIntegrationTest {

    @Test
    void testGetSeriesNotFound() throws Exception {
        performNotAuthenticated(
                get("/v1/series")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.size()").value(0))
            .andDo(print());

        performNotAuthenticated(
                get("/v1/series/{id}", "caa60384-eccf-4c68-973a-5a69421c56ed")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/02.create_cycle.sql",
            "classpath:sql/03.create_series.sql"
        }
    )
    void testGetSeriesOk() throws Exception {
        performNotAuthenticated(
                get("/v1/series")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.size()").value(2))
            .andDo(print());

        performNotAuthenticated(
                get("/v1/series/{id}", SERIES_ID)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test_42db2cab8e"))
            .andDo(print());
    }
}
