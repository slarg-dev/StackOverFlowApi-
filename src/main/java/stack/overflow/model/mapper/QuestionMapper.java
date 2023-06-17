package stack.overflow.model.mapper;

import stack.overflow.model.dto.request.QuestionRequestDto;
import stack.overflow.model.entity.Question;

public final class QuestionMapper {

    private QuestionMapper() {
    }

    public static Question toEntity(QuestionRequestDto dto) {
        Question question = new Question();
        question.setTitle(dto.title());
        question.setDescription(dto.description());
        return question;
    }
}
