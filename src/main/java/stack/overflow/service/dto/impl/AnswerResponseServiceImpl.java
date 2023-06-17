package stack.overflow.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.dto.response.AccountResponseDto;
import stack.overflow.model.dto.response.AnswerResponseDto;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.dto.AnswerResponseDtoRepository;
import stack.overflow.service.dto.AccountResponseDtoService;
import stack.overflow.service.dto.AnswerResponseDtoService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnswerResponseServiceImpl implements AnswerResponseDtoService {
    private final AnswerResponseDtoRepository answerResponseDtoRepository;
    private final AccountResponseDtoService accountResponseDtoService;

    private void addOwners(List<AnswerResponseDto> answerResponseDtos) {
        List<Long> answerIds = answerResponseDtos.stream().map(AnswerResponseDto::getId).toList();
        Map<Long, AccountResponseDto> ownerMap = accountResponseDtoService.getOwnersByQuestionIds(answerIds);
        for (AnswerResponseDto dto : answerResponseDtos) {
            AccountResponseDto owner = ownerMap.get(dto.getId());
            if (owner != null) {
                dto.setOwner(owner);
            }
        }
    }
    @Override
    public AnswerResponseDto getByAnswerId(Long answerId) {
        AnswerResponseDto resultDto = answerResponseDtoRepository.getByAnswerId(answerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer with id#%d not found", answerId)));
        resultDto.setOwner(accountResponseDtoService.getOwnerByAnswerId(answerId));
        return resultDto;
    }

    @Override
    public Page<AnswerResponseDto> getPage(PaginationParameters paginationParameters) {
        List<AnswerResponseDto> resultDtos = answerResponseDtoRepository.getDtos(paginationParameters);
        Long resultCount = answerResponseDtoRepository.getCount();
        addOwners(resultDtos);
        return new Page<>(resultDtos, resultCount);
    }




}
