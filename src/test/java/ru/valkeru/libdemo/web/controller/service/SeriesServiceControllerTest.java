package ru.valkeru.libdemo.web.controller.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeriesServiceControllerTest extends AbstractIntegrationTest {

    @Test
    void testCreateSeriesBadRequest() throws Exception {
        performAsLibrarian(
                        post(SeriesServiceController.SERIES_SERVICE_URL)
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
    void testCreateSeriesCycleNotFound() throws Exception {
        performAsLibrarian(
                        post(SeriesServiceController.SERIES_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_58c6ac3d30",
                                          "cycle": {
                                            "id": "42860c68-21fe-438e-95ae-4555c35f5150"
                                          }
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testCreateSeriesNoCycleOk() throws Exception {
        performAsLibrarian(
                        post(SeriesServiceController.SERIES_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "test_23e17f5cca"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("test_23e17f5cca"))
                .andExpect(jsonPath("$.cycle").isEmpty())
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql"
            }
    )
    void testCreateSeriesOk() throws Exception {
        performAsLibrarian(
                        post(SeriesServiceController.SERIES_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "test_23e17f5cca",
                                            "cycle": {
                                              "id": "7cc6be9b-7649-4955-bff9-8cbf7c4c429a"
                                            }
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test_23e17f5cca"))
                .andExpect(jsonPath("$.cycle.id").value("7cc6be9b-7649-4955-bff9-8cbf7c4c429a"))
                .andExpect(jsonPath("$.cycle.name").value("test_9411799dad"))
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql",
            }
    )
    void testUpdateSeriesBadRequest() throws Exception {
        performAsLibrarian(
                        patch("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
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
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql",
            }
    )
    void testUpdateSeriesCycleNotFound() throws Exception {
        performNotAuthenticated(
                        get("/v1/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        performAsLibrarian(
                        patch("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_5fed320cf3",
                                          "cycle": {
                                            "id": "42860c68-21fe-438e-95ae-4555c35f5150"
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql",
            }
    )
    void testUpdateSeriesOk() throws Exception {
        performNotAuthenticated(
                        get("/v1/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        performAsLibrarian(
                        patch("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_4233baa1c1",
                                          "cycle": {
                                            "id": "7febe13e-19c2-4c12-82cc-b354546d360e"
                                          }
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value("697792d6-8d57-4d6f-9ea2-c91b01159612"))
                .andExpect(jsonPath("$.name").value("test_4233baa1c1"))
                .andExpect(jsonPath("$.cycle").exists())
                .andExpect(jsonPath("$.cycle.id").value("7febe13e-19c2-4c12-82cc-b354546d360e"))
                .andExpect(jsonPath("$.cycle.name").value("test_ba77861515"))
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql",
            }
    )
    void testUpdateSeriesNoCycleOk() throws Exception {
        performNotAuthenticated(
                        get("/v1/series")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cycle").exists())
                .andDo(print());

        performAsLibrarian(
                        patch("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "test_b7b7e7577d"
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("697792d6-8d57-4d6f-9ea2-c91b01159612"))
                .andExpect(jsonPath("$.name").value("test_b7b7e7577d"))
                .andExpect(jsonPath("$.cycle").isEmpty())
                .andDo(print());
    }

    @Test
    void testDeleteSeriesNotFound() throws Exception {
        performAsLibrarian(
                        delete("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/01.create_author.sql",
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql",
                    "classpath:sql/04.create_book.sql",
                    "classpath:sql/05.book_to_series.sql"
            }
    )
    void testDeleteSeriesConflict() throws Exception {
        performAsLibrarian(
                        delete("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
            value = {
                    "classpath:sql/02.create_cycle.sql",
                    "classpath:sql/03.create_series.sql"
            }
    )
    void testDeleteSeriesOk() throws Exception {
        performAsLibrarian(
                        delete("%s/{id}".formatted(SeriesServiceController.SERIES_SERVICE_URL), "697792d6-8d57-4d6f-9ea2-c91b01159612")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
