package stack.overflow.model.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import stack.overflow.model.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
