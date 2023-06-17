package stack.overflow.service.entity.impl;

import org.springframework.stereotype.Service;
import stack.overflow.model.entity.Tag;
import stack.overflow.model.repository.entity.TagRepository;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.TagService;

@Service
public class TagServiceImpl extends CrudServiceImpl<Tag, Long> implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }
}
