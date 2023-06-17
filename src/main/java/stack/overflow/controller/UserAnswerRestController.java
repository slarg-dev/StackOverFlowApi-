package stack.overflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stack.overflow.exception.AccessIsDeniedException;
import stack.overflow.exception.AnswerCountLimitException;
import stack.overflow.model.api.Data;
import stack.overflow.model.dto.request.AnswerRequestDto;
import stack.overflow.model.dto.response.AnswerResponseDto;
import stack.overflow.model.entity.*;
import stack.overflow.model.enumeration.SortType;
import stack.overflow.model.mapper.AnswerMapper;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.service.dto.AnswerResponseDtoService;
import stack.overflow.service.entity.AnswerService;
import stack.overflow.service.entity.QuestionService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user/answers")
public class UserAnswerRestController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final AnswerResponseDtoService answerResponseDtoService;
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AnswerRequestDto dto, @NotNull Authentication authentication) {
        Answer answer = AnswerMapper.toEntity(dto);
        Account currentUser = (Account) authentication.getPrincipal();
        if(answerService.getAnswersByQuestionIdAndAccountId(dto.questionId(), currentUser.getId()).isPresent()){
            throw new AnswerCountLimitException(String.format("You have already answered the question with id#%d", dto.questionId()));
        }
        answer.setQuestion(questionService.getById(dto.questionId()));
        answer.setOwner(currentUser);
        answerService.create(answer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long answerId,
                                       @NotNull Authentication authentication){
        Answer answer = answerService.getByAnswerIdWithOwner(answerId);
        Account currentUser = (Account) authentication.getPrincipal();
        if (!currentUser.equals(answer.getOwner())) {
            throw new AccessIsDeniedException(String.format("Access to question with id#%d is denied", answerId));
        }
        answerService.delete(answer);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{answerId}")
    public ResponseEntity<Data<AnswerResponseDto>> getByAnswerId(@PathVariable @NotNull @Positive Long answerId){
        AnswerResponseDto dto = answerResponseDtoService.getByAnswerId(answerId);
        return ResponseEntity.ok(Data.build(dto));
    }
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Data<Page<AnswerResponseDto>>> getPage(@PathVariable @NotNull @Positive Integer pageNumber,
                                                                   @RequestParam(defaultValue = "20") @NotNull @Positive Integer size,
                                                                   @RequestParam(defaultValue = "ID_ASC") @NotNull SortType sortType) {
        PaginationParameters parameters = new PaginationParameters(pageNumber, size, sortType);
        Page<AnswerResponseDto> page = answerResponseDtoService.getPage(parameters);
        return ResponseEntity.ok(Data.build(page));
    }

}
