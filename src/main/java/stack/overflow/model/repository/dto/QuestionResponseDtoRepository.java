package stack.overflow.model.repository.dto;

import stack.overflow.model.dto.response.QuestionResponseDto;
import stack.overflow.model.repository.pagination.PaginationRepository;

import java.util.Optional;

public interface QuestionResponseDtoRepository extends PaginationRepository<QuestionResponseDto> {

    Optional<QuestionResponseDto> getByQuestionId(Long questionId);
}
