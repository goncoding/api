package com.daesung.api.accounts.repository;

import com.daesung.api.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByLoginId(String username);

    Optional<Account> findByAcNum(String acNum);


}
