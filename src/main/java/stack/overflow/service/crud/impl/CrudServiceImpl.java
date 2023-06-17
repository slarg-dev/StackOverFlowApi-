package stack.overflow.service.crud.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.service.crud.CrudService;

import java.util.List;

@RequiredArgsConstructor
public abstract class CrudServiceImpl<T, ID> implements CrudService<T, ID> {

    private final JpaRepository<T, ID> jpaRepository;

    @Transactional
    @Override
    public T create(T entity) {
        return jpaRepository.save(entity);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        jpaRepository.delete(entity);
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public T getById(ID id) {
        return jpaRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Entity with id#%s not found", id)));
    }

    @Override
    public boolean existsById(ID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<T> getByIds(Iterable<ID> ids) {
        return jpaRepository.findAllById(ids);
    }
}
