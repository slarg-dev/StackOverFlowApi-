package stack.overflow.model.repository.entity;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stack.overflow.model.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTitle(String title);
    @Query("""
            SELECT q
            FROM Question q
            JOIN FETCH q.owner ow
            WHERE q.id = :questionId
            """)
    Optional<Question> getByQuestionIdWithOwner(Long questionId);
}
