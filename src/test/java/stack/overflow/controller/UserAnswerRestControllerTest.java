package stack.overflow.controller;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import stack.overflow.IntegrationTestContext;
import stack.overflow.model.dto.request.AnswerRequestDto;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAnswerRestControllerTest extends IntegrationTestContext {
    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/createAnswerSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/createAnswerSuccessTest/after.sql")
    @Test
    public void createAnswerSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        AnswerRequestDto dto = new AnswerRequestDto("text", 1L);
        mockMvc.perform(post("/api/v1/user/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(a.id) = 1
                                FROM Answer a
                                JOIN a.owner ow
                                WHERE a.text = 'text'
                                AND a.question.id = 1
                                AND a.createdDate IS NOT NULL
                                AND a.modifiedDate IS NOT NULL
                                and a.isAnswerAccepted IS NOT NULL
                                AND ow.id = 1
                                """, Boolean.class)
                .getSingleResult());
    }
    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/deleteAnswerSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/deleteAnswerSuccessTest/after.sql")
    @Test
    public void deleteAnswerSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(delete("/api/v1/user/answers/{answerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(a.id) = 0
                                FROM Answer a
                                WHERE a.id = 1
                                """, Boolean.class)
                .getSingleResult());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(a.id) = 1
                                FROM Account a
                                WHERE a.id = 1
                                """, Boolean.class)
                .getSingleResult());
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/getByAnswerIdSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/getByAnswerIdSuccessTest/after.sql")
    @Test
    public void getByAnswerIdSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/answers/{answerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", Is.is(1)))
                .andExpect(jsonPath("$.data.createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.text", Is.is("text")))
                .andExpect(jsonPath("$.data.questionId", Is.is(1)))
                .andExpect(jsonPath("$.data.owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.owner.username", Is.is("user1")));
    }
    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/getPageSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerRestControllerTest/getPageSuccessTest/after.sql")
    @Test
    public void getPageSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/answers/page/{answerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("size", "3"))
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(2)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("text4")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[0].questionId", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("text5")))
                .andExpect(jsonPath("$.data.dtos[1].questionId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user1")));

        mockMvc.perform(get("/api/v1/user/answers/page/{answerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("size", "2")
                        .param("sortType", "ID_DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(2)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("text5")))
                .andExpect(jsonPath("$.data.dtos[0].questionId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("text4")))
                .andExpect(jsonPath("$.data.dtos[1].questionId", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user2")));

        mockMvc.perform(get("/api/v1/user/answers/page/{answerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(5)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("text1")))
                .andExpect(jsonPath("$.data.dtos[0].questionId", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user5")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("text2")))
                .andExpect(jsonPath("$.data.dtos[1].questionId", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user4")))

                .andExpect(jsonPath("$.data.dtos[2].id", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[2].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].text", Is.is("text3")))
                .andExpect(jsonPath("$.data.dtos[2].questionId", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[2].owner.id", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[2].owner.username", Is.is("user3")))

                .andExpect(jsonPath("$.data.dtos[3].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[3].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].text", Is.is("text4")))
                .andExpect(jsonPath("$.data.dtos[3].questionId", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[3].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[3].owner.username", Is.is("user2")))

                .andExpect(jsonPath("$.data.dtos[4].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[4].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].text", Is.is("text5")))
                .andExpect(jsonPath("$.data.dtos[4].questionId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[4].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[4].owner.username", Is.is("user1")));
    }

}
