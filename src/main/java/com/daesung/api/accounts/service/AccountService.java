package com.daesung.api.accounts.service;

import com.daesung.api.accounts.AccountAdapter;
import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        account.setLoginPwd(passwordEncoder.encode(account.getLoginPwd()));
        return accountRepository.save(account);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new AccountAdapter(account);
    }

}
