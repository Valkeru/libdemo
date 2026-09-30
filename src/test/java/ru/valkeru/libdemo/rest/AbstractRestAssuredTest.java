package ru.valkeru.libdemo.rest;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.valkeru.libdemo.ApplicationTestConfiguration;
import ru.valkeru.libdemo.ProfileResolver;
import ru.valkeru.libdemo.utility.JwtUtility;
import ru.valkeru.libdemo.utility.RedisUtility;

import static io.restassured.RestAssured.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ApplicationTestConfiguration.class)
@ActiveProfiles(resolver = ProfileResolver.class)
@Sql(
    value = {
        "classpath:sql/delete/00.truncate.sql"
    }
)
public abstract class AbstractRestAssuredTest {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private RedisUtility redisUtility;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String apiPath;

    protected String uri;

    @PostConstruct
    void init() {
        uri = "http://localhost:" + port + apiPath;
    }

    @AfterEach
    void afterEach() {
        redisUtility.clearCaches();
    }

    protected final ValidatableResponse postAsUser(String url) {
        return postAsUser(url, null);
    }

    protected final ValidatableResponse postAsUser(String url, String body) {
        RequestSpecification specification = RestAssured.given()
            .header(new Header(HttpHeaders.AUTHORIZATION, jwtUtility.userToken()));

        if (StringUtils.isNotBlank(body)) {
            specification.body(body);
        }

        return specification
            .post(url)
            .then()
            .assertThat();
    }
}
