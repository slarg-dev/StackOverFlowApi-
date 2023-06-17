package stack.overflow.model.dto.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record QuestionRequestDto(
        @NotBlank String title,
        @NotBlank String description,
        List<Long> tagIds) {
}
