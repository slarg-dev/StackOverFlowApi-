package stack.overflow.model.api;

import lombok.Getter;

@Getter
public final class Error {

    private final String error;

    private Error(String error) {
        this.error = error;
    }

    public static Error build(String error) {
        return new Error(error);
    }
}
