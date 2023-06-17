package stack.overflow.controller;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import stack.overflow.IntegrationTestContext;
import stack.overflow.model.dto.request.AnswerCommentRequestDto;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAnswerCommentRestControllerTest extends IntegrationTestContext {

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/createAnswerCommentSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/createAnswerCommentSuccessTest/after.sql")
    @Test
    public void createAnswerCommentSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        AnswerCommentRequestDto dto = new AnswerCommentRequestDto("text", 1L);
        mockMvc.perform(post("/api/v1/user/answer-comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(ac.id) = 1
                                FROM AnswerComment ac
                                JOIN ac.owner ow
                                JOIN ac.answer ans
                                WHERE ac.createdDate IS NOT NULL
                                AND ac.modifiedDate IS NOT NULL
                                AND ac.text = 'text'
                                AND ans.id = 1
                                AND ow.id = 1
                                """, Boolean.class)
                .getSingleResult());
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/deleteAnswerCommentSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/deleteAnswerCommentSuccessTest/after.sql")
    @Test
    public void deleteQuestionSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(delete("/api/v1/user/answer-comments/{answerCommentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(ac.id) = 0
                                FROM AnswerComment ac
                                WHERE ac.id = 1
                                """, Boolean.class)
                .getSingleResult());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(ans.id) = 1
                                FROM Answer ans
                                WHERE ans.id = 1
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

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/getByAnswerCommentIdSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/getByAnswerCommentIdSuccessTest/after.sql")
    @Test
    public void getByAnswerCommentIdSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/answer-comments/{answerCommentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", Is.is(1)))
                .andExpect(jsonPath("$.data.answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.text", Is.is("Answer1")))
                .andExpect(jsonPath("$.data.owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.owner.username", Is.is("user1")));
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/getPageSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserAnswerCommentRestControllerTest/getPageSuccessTest/after.sql")
    @Test
    public void getPageSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/answer-comments/page/{pageNumber}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(2)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[0].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("AnswerComment4")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[1].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("AnswerComment5")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user1")));

        mockMvc.perform(get("/api/v1/user/answer-comments/page/{pageNumber}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("size", "2")
                        .param("sortType", "ID_DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(2)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[0].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("AnswerComment5")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[1].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("AnswerComment4")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user1")));

        mockMvc.perform(get("/api/v1/user/answer-comments/page/{pageNumber}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(5)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].text", Is.is("AnswerComment1")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].text", Is.is("AnswerComment2")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[2].id", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[2].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[2].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].text", Is.is("AnswerComment3")))
                .andExpect(jsonPath("$.data.dtos[2].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[2].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[3].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[3].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[3].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].text", Is.is("AnswerComment4")))
                .andExpect(jsonPath("$.data.dtos[3].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[3].owner.username", Is.is("user1")))

                .andExpect(jsonPath("$.data.dtos[4].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[4].answerId", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[4].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].text", Is.is("AnswerComment5")))
                .andExpect(jsonPath("$.data.dtos[4].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[4].owner.username", Is.is("user1")));
    }
}
