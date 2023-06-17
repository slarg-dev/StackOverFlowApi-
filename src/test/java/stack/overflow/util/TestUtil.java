package stack.overflow.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import stack.overflow.model.dto.request.JwtRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@Component
public class TestUtil {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    public String getToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JwtRequestDto(username, password))))
                .andExpect(status().isOk())
                .andReturn();
        ObjectNode content = objectMapper.readValue(result.getResponse().getContentAsString(), ObjectNode.class);
        return "Bearer " + objectMapper.readValue(content.get("data").toString(), String.class);
    }
}
