package stack.overflow.service.dto;

import stack.overflow.model.dto.response.AnswerCommentResponseDto;
import stack.overflow.service.pagination.PaginationService;

public interface AnswerCommentResponseDtoService extends PaginationService<AnswerCommentResponseDto> {
    AnswerCommentResponseDto getByAnswerCommentId(Long answerCommentId);
}
