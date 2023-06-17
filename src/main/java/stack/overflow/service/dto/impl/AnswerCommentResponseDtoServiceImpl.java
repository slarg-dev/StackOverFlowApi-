package stack.overflow.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.dto.response.AccountResponseDto;
import stack.overflow.model.dto.response.AnswerCommentResponseDto;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.service.dto.AccountResponseDtoService;
import stack.overflow.service.dto.AnswerCommentResponseDtoService;
import stack.overflow.model.repository.dto.AnswerCommentResponseDtoRepository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnswerCommentResponseDtoServiceImpl implements AnswerCommentResponseDtoService {
    private final AnswerCommentResponseDtoRepository answerCommentResponseDtoRepository;
    private final AccountResponseDtoService accountResponseDtoService;

    @Override
    public AnswerCommentResponseDto getByAnswerCommentId(Long answerCommentId) {
        AnswerCommentResponseDto resultDto = answerCommentResponseDtoRepository.getByAnswerCommentId(answerCommentId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer comment with id#%d not found", answerCommentId)));
        resultDto.setOwner(accountResponseDtoService.getOwnerByAnswerCommentId(answerCommentId));
        return resultDto;
    }

    @Override
    public Page<AnswerCommentResponseDto> getPage(PaginationParameters paginationParameters) {
        List<AnswerCommentResponseDto> resultDtos = answerCommentResponseDtoRepository.getDtos(paginationParameters);
        Long resultCount = answerCommentResponseDtoRepository.getCount();
        addOwners(resultDtos);
        return new Page<>(resultDtos, resultCount);
    }

    private void addOwners(List<AnswerCommentResponseDto> answerCommentResponseDtos) {
        List<Long> answerCommentIds = answerCommentResponseDtos.stream().map(AnswerCommentResponseDto::getId).toList();
        Map<Long, AccountResponseDto> ownerMap = accountResponseDtoService.getOwnersByAnswerCommentIds(answerCommentIds);
        for (AnswerCommentResponseDto dto : answerCommentResponseDtos) {
            AccountResponseDto owner = ownerMap.get(dto.getId());
            if (owner != null) {
                dto.setOwner(owner);
            }
        }
    }
}
