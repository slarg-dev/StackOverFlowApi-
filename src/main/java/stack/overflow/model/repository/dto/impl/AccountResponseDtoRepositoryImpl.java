package stack.overflow.model.repository.dto.impl;

import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;
import stack.overflow.model.dto.response.AccountResponseDto;
import stack.overflow.model.repository.dto.AccountResponseDtoRepository;
import stack.overflow.util.JpaResultUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AccountResponseDtoRepositoryImpl implements AccountResponseDtoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AccountResponseDto> getOwnerByQuestionId(Long questionId) {
        TypedQuery<AccountResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AccountResponseDto(
                                ow.id,
                                ow.username)
                                FROM Question q
                                JOIN q.owner ow
                                WHERE q.id = :questionId
                                """, AccountResponseDto.class)
                .setParameter("questionId", questionId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }
    @Override
    public Optional<AccountResponseDto> getOwnerByAnswerId(Long answerId) {
        TypedQuery<AccountResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AccountResponseDto(
                                ow.id,
                                ow.username)
                                FROM Answer q
                                JOIN q.owner ow
                                WHERE q.id = :answerId
                                """, AccountResponseDto.class)
                .setParameter("answerId", answerId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public Optional<AccountResponseDto> getOwnerByAnswerCommentId(Long answerCommentId) {
        TypedQuery<AccountResponseDto> typedQuery = entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.AccountResponseDto(
                                    ow.id,
                                    ow.username)
                                FROM AnswerComment ac
                                JOIN ac.owner ow
                                WHERE ac.id = :answerCommentId
                                """, AccountResponseDto.class)
                .setParameter("answerCommentId", answerCommentId);
        return JpaResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public Map<Long, AccountResponseDto> getOwnersByQuestionIds(List<Long> questionIds) {
        Map<Long, AccountResponseDto> resultMap = new HashMap<>();
        entityManager.createQuery(
                        """
                                SELECT
                                q.id,
                                ow.id,
                                ow.username
                                FROM Question q
                                JOIN q.owner ow
                                WHERE q.id IN :questionIds
                                """, Tuple.class)
                .setParameter("questionIds", questionIds)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        Long questionId = (Long) objects[0];
                        Long ownerId = (Long) objects[1];
                        String ownerUsername = (String) objects[2];
                        resultMap.put(questionId, new AccountResponseDto(ownerId, ownerUsername));
                        return null;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .getResultList();
        return resultMap;
    }

    @Override
    public Map<Long, AccountResponseDto> getOwnersByAnswerCommentIds(List<Long> answerCommentIds) {
        Map<Long, AccountResponseDto> resultMap = new HashMap<>();
        entityManager.createQuery(
                        """
                                SELECT
                                ac.id,
                                ow.id,
                                ow.username
                                FROM AnswerComment ac
                                JOIN ac.owner ow
                                WHERE ac.id IN :answerCommentIds
                                """, Tuple.class)
                .setParameter("answerCommentIds", answerCommentIds)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        Long answerCommentId = (Long) objects[0];
                        Long ownerId = (Long) objects[1];
                        String ownerUsername = (String) objects[2];
                        resultMap.put(answerCommentId, new AccountResponseDto(ownerId, ownerUsername));
                        return null;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .getResultList();
        return resultMap;
    }
}
