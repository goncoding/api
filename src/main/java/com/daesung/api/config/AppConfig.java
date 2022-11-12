package com.daesung.api.config;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.service.AccountService;
import com.querydsl.core.annotations.Config;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public ApplicationRunner applicationRunner() {
//
//        return new ApplicationRunner() {
//
//            @Autowired
//            AccountService accountService;
//
//            @Override
//            public void run(ApplicationArguments args) throws Exception {
//
//                Set<AccountRole> roles = new HashSet<>();
//                roles.add(AccountRole.DS_POWER);
//                roles.add(AccountRole.DS_ENERGY);
//
//                Account gon = Account.builder()
//                        .loginId("gon01@email.com")
//                        .loginPwd("gon")
//                        .roles(roles)
//                        .build();
//
//                accountService.saveAccount();
//
//            }
//        }
//    }


}
