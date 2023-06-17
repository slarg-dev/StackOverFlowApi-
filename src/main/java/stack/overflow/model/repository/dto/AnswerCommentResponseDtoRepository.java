package stack.overflow.model.repository.dto;

import stack.overflow.model.dto.response.AnswerCommentResponseDto;
import stack.overflow.model.repository.pagination.PaginationRepository;


import java.util.Optional;

public interface AnswerCommentResponseDtoRepository extends PaginationRepository<AnswerCommentResponseDto> {
    Optional<AnswerCommentResponseDto> getByAnswerCommentId(Long answerCommentId);
}
