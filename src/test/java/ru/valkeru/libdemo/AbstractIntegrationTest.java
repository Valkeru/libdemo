package ru.valkeru.libdemo;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.valkeru.libdemo.utility.FileUtil;
import ru.valkeru.libdemo.utility.JwtUtility;
import ru.valkeru.libdemo.utility.RedisUtility;

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

    protected final ResultActions matchExpectations(ResultActions result, ResultMatcher... expectations) throws Exception {
        if (expectations != null) {
            for (ResultMatcher matcher : expectations) {
                result.andExpect(matcher);
            }
        }

        return result;
    }

    private ResultActions performAuthenticated(MockHttpServletRequestBuilder builder, String token) throws Exception {
        return mockMvc.perform(
            builder
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
        );
    }
}
