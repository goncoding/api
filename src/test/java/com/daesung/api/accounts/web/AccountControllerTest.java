package com.daesung.api.accounts.web;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.repository.AccountRepository;
import com.daesung.api.accounts.web.dto.AccountDto;
import com.daesung.api.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("계정 등록")
    @Test
    public void _테스트() throws Exception{

        HashSet<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.DS_ENERGY);
        roles.add(AccountRole.DS_POWER);

        String clientId = "daesung";
        String clientSecret = "pass";

        AccountDto dto = AccountDto.builder()
                .loginId("gon3")
                .loginPwd("gon")
                .acNum("1122")
                .acName("홍길동")
                .roles(roles)
                .build();

        mockMvc.perform(post("/kr/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk());


    }

    @DisplayName("(에러)로그인 id 중복")
    @Test
    public void _테스트_loginID_error() throws Exception{

        HashSet<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.DS_ENERGY);
        roles.add(AccountRole.DS_POWER);

        String clientId = "daesung";
        String clientSecret = "pass";

        AccountDto dto = AccountDto.builder()
                .loginId("gon3")
                .loginPwd("gon")
                .acNum("1122")
                .acName("홍길동")
                .roles(roles)
                .build();

        mockMvc.perform(post("/kr/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }





}