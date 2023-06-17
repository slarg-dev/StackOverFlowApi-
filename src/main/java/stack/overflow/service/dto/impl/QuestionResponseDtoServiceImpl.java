package stack.overflow.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.dto.response.AccountResponseDto;
import stack.overflow.model.dto.response.QuestionResponseDto;
import stack.overflow.model.dto.response.TagResponseDto;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.dto.QuestionResponseDtoRepository;
import stack.overflow.service.dto.AccountResponseDtoService;
import stack.overflow.service.dto.QuestionResponseDtoService;
import stack.overflow.service.dto.TagResponseDtoService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuestionResponseDtoServiceImpl implements QuestionResponseDtoService {

    private final QuestionResponseDtoRepository questionResponseDtoRepository;
    private final AccountResponseDtoService accountResponseDtoService;
    private final TagResponseDtoService tagResponseDtoService;

    @Override
    public QuestionResponseDto getByQuestionId(Long questionId) {
        QuestionResponseDto resultDto = questionResponseDtoRepository.getByQuestionId(questionId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Question with id#%d not found", questionId)));
        resultDto.setOwner(accountResponseDtoService.getOwnerByQuestionId(questionId));
        resultDto.setTags(tagResponseDtoService.getByQuestionId(questionId));
        return resultDto;
    }

    @Override
    public Page<QuestionResponseDto> getPage(PaginationParameters paginationParameters) {
        List<QuestionResponseDto> resultDtos = questionResponseDtoRepository.getDtos(paginationParameters);
        Long resultCount = questionResponseDtoRepository.getCount();
        addOwners(resultDtos);
        addTags(resultDtos);
        return new Page<>(resultDtos, resultCount);
    }

    private void addOwners(List<QuestionResponseDto> questionResponseDtos) {
        List<Long> questionIds = questionResponseDtos.stream().map(QuestionResponseDto::getId).toList();
        Map<Long, AccountResponseDto> ownerMap = accountResponseDtoService.getOwnersByQuestionIds(questionIds);
        for (QuestionResponseDto dto : questionResponseDtos) {
            AccountResponseDto owner = ownerMap.get(dto.getId());
            if (owner != null) {
                dto.setOwner(owner);
            }
        }
    }

    private void addTags(List<QuestionResponseDto> questionResponseDtos) {
        List<Long> questionIds = questionResponseDtos.stream().map(QuestionResponseDto::getId).toList();
        Map<Long, List<TagResponseDto>> tagMap = tagResponseDtoService.getTagsByQuestionIds(questionIds);
        for (QuestionResponseDto dto : questionResponseDtos) {
            List<TagResponseDto> tags = tagMap.get(dto.getId());
            if (tags != null) {
                dto.setTags(tags);
            }
        }
    }
}
