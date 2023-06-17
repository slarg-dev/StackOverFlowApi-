package stack.overflow.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public record AnswerRequestDto(
        @NotBlank String text,
        @NotNull @Positive Long questionId){
}

