package stack.overflow.model.pagination;

import stack.overflow.model.enumeration.SortType;

public record PaginationParameters(
        Integer pageNumber,
        Integer size,
        SortType sortType) {
}
