package stack.overflow.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerCommentResponseDto {
    private Long id;
    private Long answerId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String text;
    private AccountResponseDto owner;

    public AnswerCommentResponseDto(Long id, Long answerId, LocalDateTime createdDate, LocalDateTime modifiedDate, String text) {
        this.id = id;
        this.answerId = answerId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerCommentResponseDto that = (AnswerCommentResponseDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
