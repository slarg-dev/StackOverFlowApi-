package stack.overflow.model.mapper;

import stack.overflow.model.dto.request.AnswerCommentRequestDto;
import stack.overflow.model.entity.AnswerComment;

public final class AnswerCommentMapper {

    public static AnswerComment toEntity(AnswerCommentRequestDto dto) {
        AnswerComment answerComment = new AnswerComment();
        answerComment.setText(dto.text());
        return answerComment;
    }
}
