package stack.overflow.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stack.overflow.model.dto.response.TagResponseDto;
import stack.overflow.model.repository.dto.TagResponseDtoRepository;
import stack.overflow.service.dto.TagResponseDtoService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TagResponseDtoServiceImpl implements TagResponseDtoService {

    private final TagResponseDtoRepository tagResponseDtoRepository;

    @Override
    public List<TagResponseDto> getByQuestionId(Long questionId) {
        return tagResponseDtoRepository.getByQuestionId(questionId);
    }

    @Override
    public Map<Long, List<TagResponseDto>> getTagsByQuestionIds(List<Long> questionIds) {
        return tagResponseDtoRepository.getTagsByQuestionIds(questionIds);
    }
}
