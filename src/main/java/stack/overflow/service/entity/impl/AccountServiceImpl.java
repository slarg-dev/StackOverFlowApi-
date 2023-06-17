package stack.overflow.service.entity.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stack.overflow.model.entity.Account;
import stack.overflow.model.repository.entity.AccountRepository;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.AccountService;

@Service
public class AccountServiceImpl extends CrudServiceImpl<Account, Long> implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        super(accountRepository);
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return super.create(account);
    }
}
