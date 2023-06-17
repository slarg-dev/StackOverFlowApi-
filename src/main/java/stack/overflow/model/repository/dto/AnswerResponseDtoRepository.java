package stack.overflow.model.repository.dto;

import stack.overflow.model.dto.response.AnswerResponseDto;
import stack.overflow.model.repository.pagination.PaginationRepository;

import java.util.Optional;

public interface AnswerResponseDtoRepository extends PaginationRepository<AnswerResponseDto> {
    Optional<AnswerResponseDto> getByAnswerId(Long answerId);
}
