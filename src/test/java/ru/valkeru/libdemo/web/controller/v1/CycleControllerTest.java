package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.ApplicationTestConfiguration;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.utility.JwtUtility;
import ru.valkeru.libdemo.web.api.service.CycleServiceApi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.valkeru.libdemo.constants.TestConstants.CYCLE_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(ApplicationTestConfiguration.class)
@Sql(value = {"classpath:sql/delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"classpath:sql/delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CycleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JwtUtility jwtUtility;

    @Test
    @Sql(
        value = {
            "classpath:sql/delete/00.truncate.sql"
        }
    )
    void testCreateCycleBadRequest() throws Exception {
        mockMvc.perform(
                post(CycleServiceApi.CYCLE_SERVICE_URL)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                          {
                            "title": null
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
            "classpath:sql/delete/00.truncate.sql"
        }
    )
    void testCreateCycleOk() throws Exception {
        mockMvc.perform(
                get("/v1/cycle")
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()").value(0))
            .andDo(print());

        MvcResult result = mockMvc.perform(
                post(CycleServiceApi.CYCLE_SERVICE_URL)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
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

        mockMvc.perform(get(location)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test_a480bc0a5a"));
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/delete/00.truncate.sql"
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
            "classpath:sql/delete/00.truncate.sql",
            "classpath:sql/02.create_cycle.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testGetCyclesOk() throws Exception {
        mockMvc.perform(
                get("/v1/cycle/{cycleId}", CYCLE_ID)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(CYCLE_ID))
            .andExpect(jsonPath("$.title").value("test_9411799dad"))
            .andDo(print());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/delete/00.truncate.sql"
        }
    )
    void testUpdateCycleNotFound() throws Exception {
        mockMvc.perform(
                patch("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), "42860c68-21fe-438e-95ae-4555c35f5150")
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "title": "test"
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
            "classpath:sql/delete/00.truncate.sql",
            "classpath:sql/02.create_cycle.sql"
        }
    )
    void testUpdateCycleOk() throws Exception {
        mockMvc.perform(
                patch("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), CYCLE_ID)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                         "title": "test_778a3b8b2f"
                        }
                        """
                    )
            )
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @Sql(value = {"classpath:sql/delete/00.truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteCycleNotFound() throws Exception {
        mockMvc.perform(
                delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), CYCLE_ID)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
            )
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @Sql(
        value = {"classpath:sql/02.create_cycle.sql", "classpath:sql/03.create_series.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testDeleteCycleSeriesConflict() throws Exception {
        mockMvc.perform(
                delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), CYCLE_ID)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
            )
            .andExpect(status().isConflict())
            .andDo(print());
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
        mockMvc.perform(
                delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), CYCLE_ID)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
            )
            .andExpect(status().isConflict())
            .andDo(print());
    }

    @Test
    @Sql(
        value = {
            "classpath:sql/delete/00.truncate.sql",
            "classpath:sql/02.create_cycle.sql"
        }
    )
    void testDeleteCycleOk() throws Exception {
        mockMvc.perform(
                delete("%s/{id}".formatted(CycleServiceApi.CYCLE_SERVICE_URL), CYCLE_ID)
                    .header(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, jwtUtility.librarianToken())
            )
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}
