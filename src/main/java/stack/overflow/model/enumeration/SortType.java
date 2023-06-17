package stack.overflow.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SortType {

    ID_ASC(" id "),
    ID_DESC(" id DESC "),
    CREATED_DATE_ASC(" createdDate "),
    CREATED_DATE_DESC(" createdDate DESC "),
    MODIFIED_DATE_ASC(" modifiedDate "),
    MODIFIED_DATE_DESC(" modifiedDate DESC ");

    private final String query;
}
