package stack.overflow.model.dto.request;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record AnswerCommentRequestDto(
        @NotBlank String text,
        @NotNull @Positive Long answerId) {
}
