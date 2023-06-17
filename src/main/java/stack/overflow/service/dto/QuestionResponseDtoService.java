package stack.overflow.service.dto;

import stack.overflow.model.dto.response.QuestionResponseDto;
import stack.overflow.service.pagination.PaginationService;

public interface QuestionResponseDtoService extends PaginationService<QuestionResponseDto> {

    QuestionResponseDto getByQuestionId(Long questionId);
}
