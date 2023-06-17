package stack.overflow.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.dto.response.AccountResponseDto;
import stack.overflow.model.repository.dto.AccountResponseDtoRepository;
import stack.overflow.service.dto.AccountResponseDtoService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccountResponseDtoServiceImpl implements AccountResponseDtoService {

    private final AccountResponseDtoRepository accountResponseDtoRepository;

    @Override
    public AccountResponseDto getOwnerByQuestionId(Long questionId) {
        return accountResponseDtoRepository.getOwnerByQuestionId(questionId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Question`s owner with question id#%d not found", questionId)));
    }

    @Override
    public AccountResponseDto getOwnerByAnswerCommentId(Long answerCommentId) {
        return accountResponseDtoRepository.getOwnerByAnswerCommentId(answerCommentId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer comment`s owner with id#%d not found", answerCommentId))
        );
    }

    @Override
    public Map<Long, AccountResponseDto> getOwnersByQuestionIds(List<Long> questionIds) {
        return accountResponseDtoRepository.getOwnersByQuestionIds(questionIds);
    }

    @Override
    public Map<Long, AccountResponseDto> getOwnersByAnswerCommentIds(List<Long> answerCommentIds) {
        return accountResponseDtoRepository.getOwnersByAnswerCommentIds(answerCommentIds);
    }

    @Override
    public AccountResponseDto getOwnerByAnswerId(Long answerId) {
        return accountResponseDtoRepository.getOwnerByAnswerId(answerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer`s owner with question id#%d not found", answerId)));
    }
}
