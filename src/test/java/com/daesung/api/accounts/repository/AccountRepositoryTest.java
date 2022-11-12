package com.daesung.api.accounts.repository;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.BaseTimeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest extends BaseControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        List<Account> all = accountRepository.findAll();
//        for (Account account : all) {
//            System.out.println("account = " + account);
//        }


        Optional<Account> byAcNum = accountRepository.findByAcNum("0002");
        Account account = byAcNum.get();
        System.out.println("account = " + account);

    }


}