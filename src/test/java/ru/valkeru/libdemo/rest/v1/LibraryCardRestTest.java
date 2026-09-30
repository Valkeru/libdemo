package ru.valkeru.libdemo.rest.v1;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.rest.AbstractRestAssuredTest;
import ru.valkeru.libdemo.security.Role;

import static org.hamcrest.Matchers.notNullValue;

@Sql(
    value = {
        "classpath:sql/card/00-truncate.sql"
    }
)
@Sql(
    value = {
        "classpath:sql/users/user/reset_role_to_user.sql"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class LibraryCardRestTest extends AbstractRestAssuredTest {

    @Autowired
    private JWTService jwtService;

    @Test
    @Sql(
        value = {
            "classpath:sql/users/user/reset_role_to_user.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void testCreateLibraryCard() {
        ExtractableResponse<Response> extractable = postAsUser(uri + "/v1/library-card")
            .statusCode(HttpStatus.CREATED.value())
            .header(ApiConfig.ACCESS_TOKEN_HEADER_NAME, notNullValue())
            .header(ApiConfig.REFRESH_TOKEN_HEADER_NAME, notNullValue())
            .extract();

        String accessToken = extractable.header(ApiConfig.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = extractable.header(ApiConfig.REFRESH_TOKEN_HEADER_NAME);

        Assertions.assertNotNull(accessToken);
        Assertions.assertNotNull(refreshToken);

        TokenPayload jwtPayload = jwtService.getPayload(accessToken);

        Assertions.assertEquals(Role.READER, jwtPayload.role());
    }
}
