package ru.valkeru.libdemo;

import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.utility.FileUtil;
import ru.valkeru.libdemo.utility.JwtUtility;
import ru.valkeru.libdemo.utility.RedisUtility;

import java.util.Arrays;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(ApplicationTestConfiguration.class)
@ActiveProfiles(resolver = ProfileResolver.class)
@Sql(
    value = {
        "classpath:sql/delete/00.truncate.sql"
    }
)
public abstract class AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private RedisUtility redisUtility;

    @AfterEach
    void afterEach() {
        redisUtility.clearCaches();
    }

    protected final String readResourceAsString(String path) {
        return FileUtil.readResourceAsString(path);
    }

    protected final ResultActions performNotAuthenticated(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(
            builder
                .accept(MediaType.APPLICATION_JSON)
        );
    }

    protected final ResultActions performAsManager(MockHttpServletRequestBuilder builder) throws Exception {
        return performAuthenticated(builder, jwtUtility.managerToken());
    }

    protected final ResultActions performAsLibrarian(MockHttpServletRequestBuilder builder) throws Exception {
        return performAuthenticated(builder, jwtUtility.librarianToken());
    }

    protected final ResultActions performAsAdmin(MockHttpServletRequestBuilder builder) throws Exception {
        return performAuthenticated(builder, jwtUtility.adminToken());
    }

    protected final ResultActions performAsUser(MockHttpServletRequestBuilder builder) throws Exception {
        return performAuthenticated(builder, jwtUtility.userToken());
    }

    protected final ResultActions assertOk(ResultActions actions, String expectedBody) throws Exception {
        return assertJson(actions, status().isOk(), expectedBody);
    }

    protected final ResultActions assertCreated(ResultActions actions) throws Exception {
        return actions
            .andExpectAll(
                status().isCreated(),
                header().exists(HttpHeaders.LOCATION),
                header().exists(ApiConfig.REQUEST_ID_HEADER_NAME)
            );
    }

    protected final ResultActions assertNoContent(ResultActions actions) throws Exception {
        return actions
            .andExpectAll(
                status().isNoContent(),
                header().exists(ApiConfig.REQUEST_ID_HEADER_NAME)
            );
    }

    protected final ResultActions assertNotAuthorized(ResultActions actions) throws Exception {
        return assertResult(actions, status().isUnauthorized());
    }

    protected final ResultActions assertForbidden(ResultActions actions) throws Exception {
        return assertJson(actions, status().isForbidden(), readResourceAsString("json/access_denied.json"))
            .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }

    protected final ResultActions assertValidationFailed(ResultActions actions, String expectedBody,
                                                         Option... bodyComparisonOptions) throws Exception {
        return assertJson(actions, status().isBadRequest(), expectedBody, bodyComparisonOptions);
    }

    protected final ResultActions assertNotFound(ResultActions actions, String expectedBody) throws Exception {
        return assertJson(actions, status().isNotFound(), expectedBody);
    }

    protected final ResultActions assertConflict(ResultActions actions, String expectedBody) throws Exception {
        return assertResult(actions, status().isConflict());
    }

    private ResultActions performAuthenticated(MockHttpServletRequestBuilder builder, String token) throws Exception {
        return mockMvc.perform(
            builder
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions assertJson(ResultActions actions, ResultMatcher statusMatcher,
                                     String expectedBody, Option... options) throws Exception {
        int optionsCount = options.length;

        return assertResult(actions, statusMatcher)
            .andExpectAll(
                content().contentType(MediaType.APPLICATION_JSON),
                (optionsCount == 0
                    ? json()
                    : ( // at least one option passed
                    optionsCount == 1
                        ? json().when(options[0]) // single option
                        : json().when(options[0], Arrays.copyOfRange(options, 1, optionsCount)) // multiple options
                )
                ).isEqualTo(expectedBody)
            );
    }

    private ResultActions assertResult(ResultActions actions, ResultMatcher statusMatcher) throws Exception {
        return actions.andExpectAll(
            statusMatcher,
            header().exists(ApiConfig.REQUEST_ID_HEADER_NAME)
        );
    }
}
