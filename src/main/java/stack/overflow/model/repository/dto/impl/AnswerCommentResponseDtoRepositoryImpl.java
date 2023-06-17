package stack.overflow.model.repository.dto.impl;

import org.springframework.stereotype.Repository;
import stack.overflow.model.dto.response.AnswerCommentResponseDto;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.dto.AnswerCommentResponseDtoRepository;
import stack.overflow.util.JpaResultUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerCommentResponseDtoRepositoryImpl implements AnswerCommentResponseDtoRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AnswerCommentResponseDto> getByAnswerCommentId(Long answerCommentId) {
        TypedQuery<AnswerCommentResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AnswerCommentResponseDto(
                                ac.id,
                                ac.answer.id,
                                ac.createdDate,
                                ac.modifiedDate,
                                ac.text
                                )
                                FROM AnswerComment ac
                                WHERE ac.id = :answerCommentId
                                """, AnswerCommentResponseDto.class)
                .setParameter("answerCommentId", answerCommentId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public List<AnswerCommentResponseDto> getDtos(PaginationParameters paginationParameters) {
        return entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AnswerCommentResponseDto(
                                ac.id,
                                ac.answer.id,
                                ac.createdDate,
                                ac.modifiedDate,
                                ac.text
                                )
                                FROM AnswerComment ac
                                ORDER BY
                                """ + paginationParameters.sortType().getQuery(), AnswerCommentResponseDto.class)
                .setFirstResult((paginationParameters.pageNumber() - 1) * paginationParameters.size())
                .setMaxResults(paginationParameters.size())
                .getResultList();
    }

    @Override
    public Long getCount() {
        return entityManager.createQuery(
                        """
                                SELECT COUNT(ac.id)
                                FROM AnswerComment ac
                                """, Long.class)
                .getSingleResult();
    }
}
