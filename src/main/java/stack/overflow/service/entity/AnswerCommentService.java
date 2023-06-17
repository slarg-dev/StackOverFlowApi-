package stack.overflow.service.entity;

import stack.overflow.model.entity.AnswerComment;
import stack.overflow.service.crud.CrudService;

public interface AnswerCommentService extends CrudService<AnswerComment, Long> {
    AnswerComment getByAnswerCommentIdWithOwner(Long answerCommentId);
}
