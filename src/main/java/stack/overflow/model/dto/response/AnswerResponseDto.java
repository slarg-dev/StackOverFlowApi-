package stack.overflow.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerResponseDto {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String text;
    private AccountResponseDto owner;
    private Long questionId;
    private Boolean isAnswerAccepted;

    public AnswerResponseDto(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, Boolean isAnswerAccepted, String text, Long questionId) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.text = text;
        this.isAnswerAccepted = isAnswerAccepted;
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerResponseDto that = (AnswerResponseDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
