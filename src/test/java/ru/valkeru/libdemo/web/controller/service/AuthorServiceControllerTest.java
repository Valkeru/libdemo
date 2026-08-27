package ru.valkeru.libdemo.web.controller.service;

import net.javacrumbs.jsonunit.core.Option;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.domain.repository.jpa.author.AuthorRepository;

import java.util.UUID;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.valkeru.libdemo.constants.TestConstants.AUTHOR_ID;
import static ru.valkeru.libdemo.constants.TestConstants.START_UUID_VALUE;

class AuthorServiceControllerTest extends AbstractIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @ParameterizedTest
    @MethodSource("getBadRequestArguments")
    @DisplayName("Add an author - invalid request")
    void testCreateAuthorBadRequest(String payloadPath, String expectedResultPath) throws Exception {
        String payload = readResourceAsString(payloadPath);
        String expected = readResourceAsString(expectedResultPath);

        performAsManager(post("/service/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().when(Option.IGNORING_ARRAY_ORDER).isEqualTo(expected));
    }

    @Test
    @DisplayName("Add an author - success")
    void testCreateAuthorOk() throws Exception {
        String payload = readResourceAsString("json/author/request/add_valid.json");
        String expected = readResourceAsString("json/author/response/created.json");

        MvcResult result = performAsManager(post("/service/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);

        performNotAuthenticated(get(location))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @DisplayName("Update author info - 404")
    void testUpdateAuthorNotFound() throws Exception {
        String payload = readResourceAsString("json/author/request/update_valid.json");
        String expected = readResourceAsString("json/author/response/not_found.json");

        performAsManager(patch("/service/author/{id}", START_UUID_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        )
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));

    }

    @ParameterizedTest
    @MethodSource("getBadRequestArguments")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(
        value = {
            "classpath:sql/01.create_author.sql"
        }
    )
    @DisplayName("Update author info - 400")
    void testUpdateAuthorBadRequest(String payloadPath, String expectedResultPath) throws Exception {
        String payload = readResourceAsString(payloadPath);
        String expected = readResourceAsString(expectedResultPath);

        performAsManager(patch("/service/author/{id}", AUTHOR_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().when(Option.IGNORING_ARRAY_ORDER).isEqualTo(expected));
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
        String payload = readResourceAsString("json/author/request/update_valid.json");
        String expected = readResourceAsString("json/author/response/updated.json");

        performNotAuthenticated(get("/v1/author/{id}", AUTHOR_ID))
            .andExpect(status().isOk());

        performAsManager(patch("/service/author/{id}", AUTHOR_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        )
            .andExpect(status().isNoContent());

        performNotAuthenticated(get("/v1/author/{id}", AUTHOR_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(json().isEqualTo(expected));
    }

    @Test
    @DisplayName("Delete author info - 404")
    void deleteAuthorNotFound() throws Exception {
        performAsManager(delete("/service/author/{id}", START_UUID_VALUE))
            .andExpect(status().isNotFound());
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
        UUID id = UUID.fromString(AUTHOR_ID);

        Assert.assertTrue(authorRepository.existsById(id));

        performAsManager(delete("/service/author/{id}", AUTHOR_ID))
            .andExpect(status().isNoContent());

        Assert.assertFalse(authorRepository.existsById(id));
    }

    private static Stream<Arguments> getBadRequestArguments() {
        return Stream.of(
            Arguments.of("json/author/request/invalid_no_fields.json", "json/author/response/validation_error.json"),
            Arguments.of("json/author/request/invalid_blank_strings.json", "json/author/response/validation_error.json"),
            Arguments.of("json/author/request/invalid_nulls.json", "json/author/response/validation_error.json")
        );
    }
}
