package stack.overflow.service.crud;

import java.util.List;

public interface CrudService<T, ID> {

    T create(T entity);

    void delete(T entity);

    void deleteById(ID id);

    T getById(ID id);

    boolean existsById(ID id);

    List<T> getByIds(Iterable<ID> ids);
}
