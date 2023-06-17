package stack.overflow.service.entity.impl;

import org.springframework.stereotype.Service;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.entity.Question;
import stack.overflow.model.repository.entity.QuestionRepository;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.QuestionService;

@Service
public class QuestionServiceImpl extends CrudServiceImpl<Question, Long> implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        super(questionRepository);
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getByQuestionIdWithOwner(Long questionId) {
        return questionRepository.getByQuestionIdWithOwner(questionId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Question with id#%d not found", questionId)));
    }
}
