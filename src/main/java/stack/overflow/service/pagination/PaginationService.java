package stack.overflow.service.pagination;

import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;

public interface PaginationService<T> {

    Page<T> getPage(PaginationParameters paginationParameters);
}
