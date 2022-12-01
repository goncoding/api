//package com.daesung.api.accounts.service;
//
//import com.daesung.api.accounts.domain.Account;
//import com.daesung.api.accounts.domain.enumType.AccountRole;
//import com.daesung.api.accounts.repository.AccountRepository;
//import com.daesung.api.common.BaseControllerTest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.*;
//
//
//class AccountServiceTest extends BaseControllerTest {
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @DisplayName("")
//    @Test
//    public void _findByUsername() throws Exception{
//
//        Set<AccountRole> roles = new HashSet<>();
//        roles.add(AccountRole.DS_POWER);
//        roles.add(AccountRole.DS_ENERGY);
//
//
//        //given
//        String username = "gon03@email.com";
//        String password = "gon";
//
//        Account account = Account.builder()
//                .loginId(username)
//                .loginPwd(password)
//                .roles(roles)
//                .build();
//
//        Account account1 = accountService.saveAccount(account);
//
//        //when
//        UserDetailsService userDetailsService = (UserDetailsService) accountService;
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        System.out.println("userDetails = " + userDetails);
//        //then
//        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
//
//
//
//    }
//
//    @DisplayName("loginId 불러오기 실패")
//    @Test
//    public void _테스트_usernameGet_fail() throws Exception{
//        String username = "gon@email.com";
//        try {
//            accountService.loadUserByUsername(username);
//            fail("supposed to be failed");
//        } catch (UsernameNotFoundException e) {
//            assertThat(e.getMessage()).containsSequence(username);
//        }
//
//    }
//
//
//}