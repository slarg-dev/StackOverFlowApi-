package stack.overflow.service.entity;

import stack.overflow.model.entity.Answer;
import stack.overflow.service.crud.CrudService;

import java.util.Optional;

public interface AnswerService extends CrudService<Answer, Long> {
    Answer getByAnswerIdWithOwner(Long answerId);

    Optional<Answer> getAnswersByQuestionIdAndAccountId(Long questionId, Long ownerId);

}
