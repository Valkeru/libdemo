package ru.valkeru.libdemo.web.controller.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.valkeru.libdemo.AbstractIntegrationTest;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.security.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class LibraryCardTest extends AbstractIntegrationTest {

    @Autowired
    private JWTService jwtService;

    @Test
    @Sql(
        value = {
            "classpath:sql/users/user/reset_role_to_user.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void testCreateLibraryCard() throws Exception {
        MvcResult mvcResult = matchExpectations(
            performAsUser(post("/v1/library-card")),
            status().isCreated(),
            header().exists(ApiConfig.ACCESS_TOKEN_HEADER_NAME),
            header().exists(ApiConfig.REFRESH_TOKEN_HEADER_NAME)
        )
            .andReturn();

        String accessToken = mvcResult.getResponse().getHeader(ApiConfig.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = mvcResult.getResponse().getHeader(ApiConfig.REFRESH_TOKEN_HEADER_NAME);

        Assertions.assertNotNull(accessToken);
        Assertions.assertNotNull(refreshToken);

        TokenPayload jwtPayload = jwtService.getPayload(accessToken);

        Assertions.assertEquals(Role.READER, jwtPayload.role());
    }
}
