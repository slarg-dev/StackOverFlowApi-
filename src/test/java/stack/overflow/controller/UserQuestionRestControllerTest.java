package stack.overflow.controller;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import stack.overflow.IntegrationTestContext;
import stack.overflow.model.dto.request.QuestionRequestDto;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserQuestionRestControllerTest extends IntegrationTestContext {

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/createQuestionSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/createQuestionSuccessTest/after.sql")
    @Test
    public void createQuestionSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        QuestionRequestDto dto = new QuestionRequestDto("title", "description", List.of(1L, 2L, 3L));
        mockMvc.perform(post("/api/v1/user/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(q.id) = 3
                                FROM Question q
                                JOIN q.owner ow
                                JOIN q.tags t
                                WHERE q.title = 'title'
                                AND q.description = 'description'
                                AND q.createdDate IS NOT NULL
                                AND q.modifiedDate IS NOT NULL
                                AND ow.id = 1
                                AND t.id IN (1, 2, 3)
                                """, Boolean.class)
                .getSingleResult());
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/deleteQuestionSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/deleteQuestionSuccessTest/after.sql")
    @Test
    public void deleteQuestionSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(delete("/api/v1/user/questions/{questionId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(q.id) = 0
                                FROM Question q
                                WHERE q.id = 1
                                """, Boolean.class)
                .getSingleResult());
        Assertions.assertTrue(entityManager.createQuery(
                        """
                                SELECT COUNT(t.id) = 4
                                FROM Tag t
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

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/getByQuestionIdSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/getByQuestionIdSuccessTest/after.sql")
    @Test
    public void getByQuestionIdSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/questions/{questionId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", Is.is(1)))
                .andExpect(jsonPath("$.data.createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.title", Is.is("title1")))
                .andExpect(jsonPath("$.data.description", Is.is("description1")))
                .andExpect(jsonPath("$.data.owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.owner.username", Is.is("user1")))
                .andExpect(jsonPath("$.data.tags.length()", Is.is(3)))
                .andExpect(jsonPath("$.data.tags[*].id", hasItems(1, 2, 3)))
                .andExpect(jsonPath("$.data.tags[*].name", hasItems("tag1", "tag2", "tag3")));
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/getPageSuccessTest/before.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, value = "/sql/UserQuestionRestControllerTest/getPageSuccessTest/after.sql")
    @Test
    public void getPageSuccessTest() throws Exception {
        String token = testUtil.getToken("user1", "password");
        mockMvc.perform(get("/api/v1/user/questions/page/{pageNumber}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(2)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].title", Is.is("title4")))
                .andExpect(jsonPath("$.data.dtos[0].description", Is.is("description4")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[0].tags.length()", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[0].tags[*].id", hasItems(1, 2, 3, 4, 5)))
                .andExpect(jsonPath("$.data.dtos[0].tags[*].name", hasItems("tag1", "tag2", "tag3", "tag4", "tag5")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].title", Is.is("title5")))
                .andExpect(jsonPath("$.data.dtos[1].description", Is.is("description5")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[1].tags.length()", Is.is(0)));

        mockMvc.perform(get("/api/v1/user/questions/page/{pageNumber}", 1)
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
                .andExpect(jsonPath("$.data.dtos[0].title", Is.is("title5")))
                .andExpect(jsonPath("$.data.dtos[0].description", Is.is("description5")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[0].tags.length()", Is.is(0)))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].title", Is.is("title4")))
                .andExpect(jsonPath("$.data.dtos[1].description", Is.is("description4")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[1].tags.length()", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[1].tags[*].id", hasItems(1, 2, 3, 4, 5)))
                .andExpect(jsonPath("$.data.dtos[1].tags[*].name", hasItems("tag1", "tag2", "tag3", "tag4", "tag5")));

        mockMvc.perform(get("/api/v1/user/questions/page/{pageNumber}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos.length()", Is.is(5)))

                .andExpect(jsonPath("$.data.dtos[0].id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[0].title", Is.is("title1")))
                .andExpect(jsonPath("$.data.dtos[0].description", Is.is("description1")))
                .andExpect(jsonPath("$.data.dtos[0].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[0].owner.username", Is.is("user1")))
                .andExpect(jsonPath("$.data.dtos[0].tags.length()", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[0].tags[*].id", hasItems(1, 2, 3)))
                .andExpect(jsonPath("$.data.dtos[0].tags[*].name", hasItems("tag1", "tag2", "tag3")))

                .andExpect(jsonPath("$.data.dtos[1].id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[1].title", Is.is("title2")))
                .andExpect(jsonPath("$.data.dtos[1].description", Is.is("description2")))
                .andExpect(jsonPath("$.data.dtos[1].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[1].owner.username", Is.is("user1")))
                .andExpect(jsonPath("$.data.dtos[1].tags.length()", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[1].tags[*].id", hasItems(4, 5)))
                .andExpect(jsonPath("$.data.dtos[1].tags[*].name", hasItems("tag4", "tag5")))

                .andExpect(jsonPath("$.data.dtos[2].id", Is.is(3)))
                .andExpect(jsonPath("$.data.dtos[2].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[2].title", Is.is("title3")))
                .andExpect(jsonPath("$.data.dtos[2].description", Is.is("description3")))
                .andExpect(jsonPath("$.data.dtos[2].owner.id", Is.is(1)))
                .andExpect(jsonPath("$.data.dtos[2].owner.username", Is.is("user1")))
                .andExpect(jsonPath("$.data.dtos[2].tags.length()", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[2].tags[*].id", hasItems(1, 2)))
                .andExpect(jsonPath("$.data.dtos[2].tags[*].name", hasItems("tag1", "tag2")))

                .andExpect(jsonPath("$.data.dtos[3].id", Is.is(4)))
                .andExpect(jsonPath("$.data.dtos[3].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[3].title", Is.is("title4")))
                .andExpect(jsonPath("$.data.dtos[3].description", Is.is("description4")))
                .andExpect(jsonPath("$.data.dtos[3].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[3].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[3].tags.length()", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[3].tags[*].id", hasItems(1, 2, 3, 4, 5)))
                .andExpect(jsonPath("$.data.dtos[3].tags[*].name", hasItems("tag1", "tag2", "tag3", "tag4", "tag5")))

                .andExpect(jsonPath("$.data.dtos[4].id", Is.is(5)))
                .andExpect(jsonPath("$.data.dtos[4].createdDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].modifiedDate", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.data.dtos[4].title", Is.is("title5")))
                .andExpect(jsonPath("$.data.dtos[4].description", Is.is("description5")))
                .andExpect(jsonPath("$.data.dtos[4].owner.id", Is.is(2)))
                .andExpect(jsonPath("$.data.dtos[4].owner.username", Is.is("user2")))
                .andExpect(jsonPath("$.data.dtos[4].tags.length()", Is.is(0)));
    }
}
