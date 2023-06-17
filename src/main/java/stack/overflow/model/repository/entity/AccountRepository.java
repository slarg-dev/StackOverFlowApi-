package stack.overflow.model.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stack.overflow.model.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a JOIN FETCH a.permissions WHERE a.username = :username")
    Optional<Account> findByUsername(String username);
}
