package stack.overflow.model.repository.dto.impl;

import org.springframework.stereotype.Repository;
import stack.overflow.model.dto.response.AnswerResponseDto;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.dto.AnswerResponseDtoRepository;
import stack.overflow.util.JpaResultUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerResponseDtoRepositoryImpl implements AnswerResponseDtoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AnswerResponseDto> getByAnswerId(Long answerId) {
        TypedQuery<AnswerResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AnswerResponseDto(
                                a.id,
                                a.createdDate,
                                a.modifiedDate,
                                a.isAnswerAccepted,
                                a.text,
                                a.question.id
                                )
                                FROM Answer a
                                WHERE a.id = :answerId
                                """, AnswerResponseDto.class)
                .setParameter("answerId", answerId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public List<AnswerResponseDto> getDtos(PaginationParameters paginationParameters) {
        return entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AnswerResponseDto
                                (a.id,
                                a.createdDate,
                                a.modifiedDate,
                                a.isAnswerAccepted,
                                a.text,
                                a.question.id
                                )
                                FROM Answer a
                                ORDER BY
                                """ + paginationParameters.sortType().getQuery(), AnswerResponseDto.class)
                .setFirstResult((paginationParameters.pageNumber() - 1) * paginationParameters.size())
                .setMaxResults(paginationParameters.size())
                .getResultList();
    }
    @Override
    public Long getCount() {
        return entityManager.createQuery(
                        """
                                SELECT COUNT(a.id)
                                FROM Answer a
                                """, Long.class)
                .getSingleResult();
    }


}
