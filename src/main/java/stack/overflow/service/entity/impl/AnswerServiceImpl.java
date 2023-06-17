package stack.overflow.service.entity.impl;

import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.entity.Answer;
import stack.overflow.model.repository.entity.AnswerRepository;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.AnswerService;

import java.util.Optional;

@Service
public class AnswerServiceImpl extends CrudServiceImpl<Answer, Long> implements AnswerService {
    private final AnswerRepository answerRepository;
    public AnswerServiceImpl(AnswerRepository answerRepository){
        super(answerRepository);
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer getByAnswerIdWithOwner(Long answerId) {
        return answerRepository.getByAnswerIdWithOwner(answerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Answer with id#%d not found", answerId)));
    }

    @Override
    public Optional<Answer> getAnswersByQuestionIdAndAccountId(Long questionId, Long ownerId) {
        return answerRepository.getAnswersByQuestionIdAndAccountId(questionId, ownerId);
    }
}
