package stack.overflow.controller;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import stack.overflow.IntegrationTestContext;
import stack.overflow.model.dto.request.JwtRequestDto;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TokenRestControllerTest extends IntegrationTestContext {

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/TokenRestControllerTest/getTokenSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/TokenRestControllerTest/getTokenSuccessTest/after.sql")
    @Test
    public void getTokenSuccessTest() throws Exception {
        JwtRequestDto dto = new JwtRequestDto("user1", "password");
        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.notNullValue()));
    }

    @Test
    public void getTokenValidationTest() throws Exception {
        JwtRequestDto dto = new JwtRequestDto("", "test");
        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is("username - must not be blank ")));

        dto = new JwtRequestDto("test", "  ");
        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is("password - must not be blank ")));

        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is(Matchers.notNullValue())));
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/TokenRestControllerTest/isTokenExpiredSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/TokenRestControllerTest/isTokenExpiredSuccessTest/after.sql")
    @Test
    public void isTokenExpiredSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/token/is-expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", token.substring(7)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Is.is(false)));
    }

    @Test
    public void isTokenExpiredValidationTest() throws Exception {
        mockMvc.perform(get("/api/v1/token/is-expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is("isTokenExpired.token - must not be blank ")));

        mockMvc.perform(get("/api/v1/token/is-expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", "  "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is("isTokenExpired.token - must not be blank ")));

        mockMvc.perform(get("/api/v1/token/is-expired")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Is.is("Required request parameter 'token' for method parameter type String is not present")));

        mockMvc.perform(get("/api/v1/token/is-expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", "test"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", Is.is("JWT strings must contain exactly 2 period characters. Found: 0")));
    }
}
