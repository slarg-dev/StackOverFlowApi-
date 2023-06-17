package stack.overflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stack.overflow.exception.AccessIsDeniedException;
import stack.overflow.model.api.Data;
import stack.overflow.model.dto.request.QuestionRequestDto;
import stack.overflow.model.dto.response.QuestionResponseDto;
import stack.overflow.model.entity.Account;
import stack.overflow.model.entity.Question;
import stack.overflow.model.entity.Tag;
import stack.overflow.model.enumeration.SortType;
import stack.overflow.model.mapper.QuestionMapper;
import stack.overflow.model.pagination.Page;
import stack.overflow.model.pagination.PaginationParameters;
import stack.overflow.model.repository.entity.QuestionRepository;
import stack.overflow.service.dto.QuestionResponseDtoService;
import stack.overflow.service.entity.QuestionService;
import stack.overflow.service.entity.TagService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user/questions")
public class UserQuestionRestController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final TagService tagService;
    private final QuestionResponseDtoService questionResponseDtoService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid QuestionRequestDto dto,
                                       @NotNull Authentication authentication) {
        Question question = QuestionMapper.toEntity(dto);
        Account currentUser = (Account) authentication.getPrincipal();
        question.setOwner(currentUser);
        List<Tag> tags = new ArrayList<>();
        if (dto.tagIds() != null) {
            tags = tagService.getByIds(dto.tagIds());
        }
        question.setTags(new HashSet<>(tags));
        questionService.create(question);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long questionId,
                                       @NotNull Authentication authentication) {
        Question question = questionService.getByQuestionIdWithOwner(questionId);
        Account currentUser = (Account) authentication.getPrincipal();
        if (!currentUser.equals(question.getOwner())) {
            throw new AccessIsDeniedException(String.format("Access to question with id#%d is denied", questionId));
        }
        questionService.delete(question);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Data<QuestionResponseDto>> getByQuestionId(@PathVariable @NotNull @Positive Long questionId) {
        QuestionResponseDto dto = questionResponseDtoService.getByQuestionId(questionId);
        return ResponseEntity.ok(Data.build(dto));
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Data<Page<QuestionResponseDto>>> getPage(@PathVariable @NotNull @Positive Integer pageNumber,
                                                                   @RequestParam(defaultValue = "20") @NotNull @Positive Integer size,
                                                                   @RequestParam(defaultValue = "ID_ASC") @NotNull SortType sortType) {
        PaginationParameters parameters = new PaginationParameters(pageNumber, size, sortType);
        Page<QuestionResponseDto> page = questionResponseDtoService.getPage(parameters);
        return ResponseEntity.ok(Data.build(page));
    }

    @GetMapping("/findByTitle/{title}")
    public Iterable<Question> findByTitle(@PathVariable String title){
        return questionRepository.findByTitle(title);
    }
}
