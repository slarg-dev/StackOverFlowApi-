package stack.overflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stack.overflow.exception.AccessIsDeniedException;
import stack.overflow.model.api.Data;
import stack.overflow.model.dto.request.AnswerCommentRequestDto;
import stack.overflow.model.dto.response.AnswerCommentResponseDto;
import stack.overflow.model.entity.Account;
import stack.overflow.model.entity.AnswerComment;
import stack.overflow.model.enumeration.SortType;
import stack.overflow.model.mapper.AnswerCommentMapper;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.service.dto.AnswerCommentResponseDtoService;
import stack.overflow.service.entity.AnswerCommentService;
import stack.overflow.service.entity.AnswerService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user/answer-comments")
public class UserAnswerCommentRestController {

    private final AnswerCommentService answerCommentService;
    private final AnswerService answerService;
    private final AnswerCommentResponseDtoService answerCommentResponseDtoService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AnswerCommentRequestDto dto,
                                       @NotNull Authentication authentication) {
        AnswerComment answerComment = AnswerCommentMapper.toEntity(dto);
        Account currentUser = (Account) authentication.getPrincipal();
        answerComment.setOwner(currentUser);
        answerComment.setAnswer(answerService.getById(dto.answerId()));
        answerCommentService.create(answerComment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{answerCommentId}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long answerCommentId,
                                       @NotNull Authentication authentication) {
        AnswerComment answerComment = answerCommentService.getByAnswerCommentIdWithOwner(answerCommentId);
        Account currentUser = (Account) authentication.getPrincipal();
        if (!currentUser.equals(answerComment.getOwner())) {
            throw new AccessIsDeniedException(String.format("Access to answer comment with id#%d is denied", answerCommentId));
        }
        answerCommentService.delete(answerComment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{answerCommentId}")
    public ResponseEntity<Data<AnswerCommentResponseDto>> getByAnswerCommentId(@PathVariable @NotNull @Positive Long answerCommentId) {
        AnswerCommentResponseDto dto = answerCommentResponseDtoService.getByAnswerCommentId(answerCommentId);
        return ResponseEntity.ok(Data.build(dto));
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Data<Page<AnswerCommentResponseDto>>> getPage(@PathVariable @NotNull @Positive Integer pageNumber,
                                                                   @RequestParam(defaultValue = "20") @NotNull @Positive Integer size,
                                                                   @RequestParam(defaultValue = "ID_ASC") @NotNull SortType sortType) {
        PaginationParameters parameters = new PaginationParameters(pageNumber, size, sortType);
        Page<AnswerCommentResponseDto> page = answerCommentResponseDtoService.getPage(parameters);
        return ResponseEntity.ok(Data.build(page));
    }

}
