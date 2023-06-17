package stack.overflow.model.repository.dto.impl;

import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;
import stack.overflow.model.dto.response.TagResponseDto;
import stack.overflow.model.repository.dto.TagResponseDtoRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagResponseDtoRepositoryImpl implements TagResponseDtoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagResponseDto> getByQuestionId(Long questionId) {
        return entityManager.createQuery(
                        """
                                SELECT new stack.overflow.model.dto.response.TagResponseDto(
                                t.id,
                                t.name)
                                FROM Question q
                                JOIN q.tags t
                                WHERE q.id = :questionId
                                """, TagResponseDto.class)
                .setParameter("questionId", questionId)
                .getResultList();
    }

    @Override
    public Map<Long, List<TagResponseDto>> getTagsByQuestionIds(List<Long> questionIds) {
        Map<Long, List<TagResponseDto>> resultMap = new HashMap<>();
        entityManager.createQuery(
                        """
                                SELECT
                                q.id,
                                t.id,
                                t.name
                                FROM Question q
                                JOIN q.tags t
                                WHERE q.id IN :questionIds
                                """, Tuple.class)
                .setParameter("questionIds", questionIds)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        Long questionId = (Long) objects[0];
                        Long tagId = (Long) objects[1];
                        String tagName = (String) objects[2];
                        if (!resultMap.containsKey(questionId)) {
                            resultMap.put(questionId, new ArrayList<>());
                        }
                        resultMap.get(questionId).add(new TagResponseDto(tagId, tagName));
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
