package stack.overflow.model.repository.dto.impl;

import org.springframework.stereotype.Repository;
import stack.overflow.model.dto.response.QuestionResponseDto;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.dto.QuestionResponseDtoRepository;
import stack.overflow.util.JpaResultUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionResponseDtoRepositoryImpl implements QuestionResponseDtoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<QuestionResponseDto> getByQuestionId(Long questionId) {
        TypedQuery<QuestionResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.QuestionResponseDto(
                                q.id,
                                q.createdDate,
                                q.modifiedDate,
                                q.title,
                                q.description)
                                FROM Question q
                                WHERE q.id = :questionId
                                """, QuestionResponseDto.class)
                .setParameter("questionId", questionId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public List<QuestionResponseDto> getDtos(PaginationParameters paginationParameters) {
        return entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.QuestionResponseDto(
                                q.id,
                                q.createdDate,
                                q.modifiedDate,
                                q.title,
                                q.description)
                                FROM Question q
                                ORDER BY
                                """ + paginationParameters.sortType().getQuery(), QuestionResponseDto.class)
                .setFirstResult((paginationParameters.pageNumber() - 1) * paginationParameters.size())
                .setMaxResults(paginationParameters.size())
                .getResultList();
    }

    @Override
    public Long getCount() {
        return entityManager.createQuery(
                        """
                                SELECT COUNT(q.id)
                                FROM Question q
                                """, Long.class)
                .getSingleResult();
    }
}
