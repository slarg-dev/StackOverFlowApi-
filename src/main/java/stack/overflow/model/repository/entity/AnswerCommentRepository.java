package stack.overflow.model.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stack.overflow.model.entity.AnswerComment;

import java.util.Optional;

public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
    @Query("""
            SELECT ac
            FROM AnswerComment ac
            JOIN FETCH ac.owner ow
            JOIN FETCH ac.answer ans
            WHERE ac.id = :answerCommentId
            """)
    Optional<AnswerComment> getByAnswerCommentIdWithOwner(Long answerCommentId);

}
