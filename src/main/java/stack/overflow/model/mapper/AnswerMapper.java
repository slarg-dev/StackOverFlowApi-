package stack.overflow.model.mapper;

import stack.overflow.model.dto.request.AnswerRequestDto;
import stack.overflow.model.entity.Answer;

public class AnswerMapper {

    private AnswerMapper() {}

    public static Answer toEntity(AnswerRequestDto dto) {
        Answer answer = new Answer();
        answer.setText(dto.text());
        answer.setIsAnswerAccepted(false);
        return answer;
    }
}

