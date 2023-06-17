package stack.overflow.service.entity;

import stack.overflow.model.entity.Question;
import stack.overflow.service.crud.CrudService;

public interface QuestionService extends CrudService<Question, Long> {

    Question getByQuestionIdWithOwner(Long questionId);
}
