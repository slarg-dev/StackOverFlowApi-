package stack.overflow.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import stack.overflow.exception.AccessIsDeniedException;
import stack.overflow.exception.AnswerCountLimitException;
import stack.overflow.exception.EntityNotFoundException;
import stack.overflow.model.api.Error;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
public class AdviceRestController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> exception(Exception e) {
        return ResponseEntity.internalServerError().body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Error> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> constraintViolationException(ConstraintViolationException e) {
        StringBuilder error = new StringBuilder();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            error.append(violation.getPropertyPath().toString()).append(" - ").append(violation.getMessage()).append(" ");
        }
        return ResponseEntity.badRequest().body(Error.build(error.toString()));
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder error = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append(" ");
        }
        return ResponseEntity.badRequest().body(Error.build(error.toString()));
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> authenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(AnswerCountLimitException.class)
    public ResponseEntity<Error> answerCountLimitException(AnswerCountLimitException e) {
        return ResponseEntity.badRequest().body(Error.build(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(AccessIsDeniedException.class)
    public ResponseEntity<Error> accessIsDeniedException(AccessIsDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Error.build(e.getMessage()));
    }
}
