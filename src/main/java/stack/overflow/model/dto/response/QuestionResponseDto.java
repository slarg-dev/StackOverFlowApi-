package stack.overflow.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionResponseDto {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String description;
    private AccountResponseDto owner;
    private List<TagResponseDto> tags = new ArrayList<>();

    public QuestionResponseDto(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String title, String description) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionResponseDto that = (QuestionResponseDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
