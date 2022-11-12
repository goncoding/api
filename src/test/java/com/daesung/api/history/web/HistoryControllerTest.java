package com.daesung.api.history.web;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.properties.AdminProperties;
import com.daesung.api.accounts.service.AccountService;
import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.events.EventDto;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import com.daesung.api.history.repository.HistoryRepository;
import com.daesung.api.history.web.dto.HistoryDetailDto;
import com.daesung.api.history.web.dto.HistorytDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HistoryControllerTest extends BaseControllerTest {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AdminProperties adminProperties;

    @DisplayName("update 수정 - 성공")
    @Test
    public void _테스트() throws Exception{

        String updateContent = "update content.....";

        History history = historyRepository.findById(1L).get();

        HistorytDto requestDto = HistorytDto.builder()
                .content(updateContent)
                .build();

        String valueAsString = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.jpg", "image/jpeg", "test file".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
                        .file(multipartFile)
                        .file(multipartFile1)
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())

        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("update 에러 - badRequest")
    @Test
    public void _테스트_badRequest() throws Exception{

        String updateContent = "update content.....";

        History history = historyRepository.findById(1L).get();

        HistorytDto requestDto = HistorytDto.builder()
                .content(updateContent)
                .build();

        String valueAsString = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.txt", "text/html", "test file".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
                        .file(multipartFile)
                        .file(multipartFile1)
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())

        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").exists())
        ;
    }

    @DisplayName("연혁 list 조회 - 성공")
    @Test
    public void _테스트_list() throws Exception{

        mockMvc.perform(get("/{lang}/history","kr"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("연혁 단일 조회 - 성공")
    @Test
    public void _테스트_get() throws Exception{

        mockMvc.perform(get("/{lang}/history/{id}","kr","2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("연혁세부 list 조회 - 성공")
    @Test
    public void _테스트_detail_list() throws Exception{

        mockMvc.perform(get("/kr/history/detail"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("연혁세부 단건 조회(인증없음) - 성공")
    @Test
    public void _테스트_detail_get() throws Exception{

        mockMvc.perform(get("/kr/history/detail/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("연혁세부 단건 조회(인증 있는 경우) - 성공")
    @Test
    public void _테스트_detail_get_authentication() throws Exception{

        mockMvc.perform(get("/kr/history/detail/1")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect()
        ;
    }


//    @DisplayName("연혁세부 단건 삭제 - 성공")
//    @Test
//    public void _테스트_detail_detail() throws Exception{
//
//        mockMvc.perform(delete("/kr/history/detail/26")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken())
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @DisplayName("연셕세부 수정 - 성공")
    @Test
    public void _테스트_detail_insert() throws Exception{

        HistoryDetailDto historyDetail = HistoryDetailDto.builder()
                .hdYear("1987")
                .hdMonth("08")
                .content("update...")
                .hdSequence(6)
                .build();

        mockMvc.perform(put("/kr/history/detail/38")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(historyDetail))
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
                ;

    }


    @DisplayName("연셕세부 노출순서 변경 - 성공")
    @Test
    public void _테스트_detail_sequence_ok() throws Exception{

        History history = historyRepository.findById(5L).get();

        HistoryDetailDto historyDetail = HistoryDetailDto.builder()
                .hdYear("1987")
                .hdMonth("08")
                .content("변경변경")
                .hdSequence(3)
                .build();

        mockMvc.perform(post("/kr/history/detail")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(historyDetail))
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
                ;

    }


    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {

        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.DS_POWER);
        roles.add(AccountRole.DS_ENERGY);

        String loginId = "gon1";
        String loginPwd = "gon";
//
//        Account gon = Account.builder()
//                .loginId(loginId)
//                .loginPwd(loginPwd)
//                .roles(roles)
//                .build();
//
//        accountService.saveAccount(gon);

        String clientId = "daesung";
        String clientSecret = "pass";

        ResultActions perform = mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(adminProperties.getClientId(), adminProperties.getClientSecret()))
                        .param("username", adminProperties.getAdminUsername())
                        .param("password", adminProperties.getAdminPassword())
                        .param("grant_type", "password")
                )
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());


        String responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        String access_token = parser.parseMap(responseBody).get("access_token").toString();
        System.out.println("access_token = " + access_token);
        return access_token;

    }
}