package stack.overflow.service.dto;

import stack.overflow.model.dto.response.AnswerResponseDto;
import stack.overflow.service.pagination.PaginationService;

public interface AnswerResponseDtoService extends PaginationService<AnswerResponseDto> {
    AnswerResponseDto getByAnswerId(Long answerId);
}
