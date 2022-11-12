package com.daesung.api.config;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.service.AccountService;
import com.daesung.api.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @DisplayName("인증 토큰을 발급 받는 테스트")
    @Test
    public void _테스트_getAuthToken() throws Exception{

        //요청과 응답이 한번으로 받을 수 있다..
        //보통은 client -> facebook등 redirection이 일어남..
        //password 방식은 grant_type = password
        //cliend_id
        //인증 정보를 가지고 있는... 서버스가 만드는 앱에서만 쓸 수 있는 방식..


        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.DS_POWER);
        roles.add(AccountRole.DS_ENERGY);

        String loginId = "gon05@email.com";
        String loginPwd = "gon";

        Account gon = Account.builder()
                .loginId(loginId)
                .loginPwd(loginPwd)
                .roles(roles)
                .build();

        accountService.saveAccount(gon);

        String clientId = "daesung";
        String clientSecret = "pass";

        mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username",loginId)
                        .param("password",loginPwd)
                        .param("grant_type","password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());




    }


}