package stack.overflow.exception;

public class AnswerCountLimitException extends RuntimeException {

    public AnswerCountLimitException(String message) {
        super(message);
    }
}
