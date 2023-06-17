package stack.overflow.model.repository.dto;

import stack.overflow.model.dto.response.AccountResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountResponseDtoRepository {

    Optional<AccountResponseDto> getOwnerByQuestionId(Long questionId);
    Optional<AccountResponseDto> getOwnerByAnswerId(Long answerId);

    Map<Long, AccountResponseDto> getOwnersByQuestionIds(List<Long> questionIds);
    Optional<AccountResponseDto> getOwnerByAnswerCommentId(Long answerCommentIds);
    Map<Long, AccountResponseDto> getOwnersByAnswerCommentIds(List<Long> answerCommentIds);
}
