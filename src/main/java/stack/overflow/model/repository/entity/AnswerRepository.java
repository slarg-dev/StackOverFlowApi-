package stack.overflow.model.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stack.overflow.model.entity.Answer;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("""
            SELECT q
            FROM Answer q
            JOIN FETCH q.owner ow
            WHERE q.id = :answerId
            """)
    Optional<Answer> getByAnswerIdWithOwner(Long answerId);

    @Query("""
            SELECT q
            FROM Answer q
            WHERE q.question.id = :questionId
            AND q.owner.id = :ownerId
            """)
    Optional<Answer> getAnswersByQuestionIdAndAccountId(Long questionId, Long ownerId);
}
