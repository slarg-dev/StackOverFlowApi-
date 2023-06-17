package stack.overflow.service.entity.impl;

import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.entity.AnswerComment;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.AnswerCommentService;
import stack.overflow.model.repository.entity.AnswerCommentRepository;


@Service
public class AnswerCommentServiceImpl extends CrudServiceImpl<AnswerComment, Long> implements AnswerCommentService {
    private final AnswerCommentRepository answerCommentRepository;

    public AnswerCommentServiceImpl(AnswerCommentRepository answerCommentRepository) {
        super(answerCommentRepository);
        this.answerCommentRepository = answerCommentRepository;
    }

    @Override
    public AnswerComment getByAnswerCommentIdWithOwner(Long answerCommentId) {
        return answerCommentRepository.getByAnswerCommentIdWithOwner(answerCommentId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer comment with id#%d not found", answerCommentId)));
    }
}
