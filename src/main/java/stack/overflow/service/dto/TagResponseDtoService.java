package stack.overflow.service.dto;

import stack.overflow.model.dto.response.TagResponseDto;

import java.util.List;
import java.util.Map;

public interface TagResponseDtoService {

    List<TagResponseDto> getByQuestionId(Long questionId);

    Map<Long, List<TagResponseDto>> getTagsByQuestionIds(List<Long> questionIds);
}
