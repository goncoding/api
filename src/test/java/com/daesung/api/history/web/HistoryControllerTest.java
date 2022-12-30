//package com.daesung.api.history.web;
//
//import com.daesung.api.accounts.domain.Account;
//import com.daesung.api.accounts.domain.enumType.AccountRole;
//import com.daesung.api.accounts.properties.AdminProperties;
////import com.daesung.api.accounts.service.AccountService;
//import com.daesung.api.accounts.service.AccountService;
//import com.daesung.api.common.BaseControllerTest;
//import com.daesung.api.events.EventDto;
//import com.daesung.api.history.domain.History;
//import com.daesung.api.history.domain.HistoryDetail;
//import com.daesung.api.history.repository.HistoryRepository;
//import com.daesung.api.history.web.dto.HistoryDetailDto;
//import com.daesung.api.history.web.dto.HistorytDto;
//import com.google.common.net.HttpHeaders;
//import org.h2.util.ThreadDeadlockDetector;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.MediaTypes;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.springframework.restdocs.headers.HeaderDocumentation.*;
//import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
//import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class HistoryControllerTest extends BaseControllerTest {
//
//    @Autowired
//    HistoryRepository historyRepository;
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    AdminProperties adminProperties;
//
//    @DisplayName("연혁 list 조회 - 성공")
//    @Test
//    public void _테스트_list() throws Exception{
//
//        mockMvc.perform(get("/{lang}/history","kr")
//                        .accept(MediaTypes.HAL_JSON_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andDo(document("history-list",
////                        requestHeaders(
////                                headerWithName(HttpHeaders.ACCEPT).description("application/hal+json;charset-utf8")
////                        )
////                        ,
////                        relaxedResponseFields(
//////                                fieldWithPath("_embedded.historyList[0].hiOriginFileName").type(JsonFieldType.STRING).description("실제파일명")
//////                                fieldWithPath("_embedded.historyList[0].hiSaveFileName").description("저장파일명")
//////                                fieldWithPath("_embedded.historyList[0].hiFileSavedPath").description("저장경로").type(String.class),
////                                fieldWithPath("_embedded.historyList[0].id").description("연혁번호"),
////                                fieldWithPath("_embedded.historyList[0].hiStartYear").description("시작연도"),
////                                fieldWithPath("_embedded.historyList[0].hiEndYear").description("끝연도"),
////                                fieldWithPath("_embedded.historyList[0].title").description("연도제목"),
////                                fieldWithPath("_embedded.historyList[0].content").description("연혁내용"),
////                                fieldWithPath("_embedded.historyList[0].regDate").description("등록일자"),
////                                fieldWithPath("_embedded.historyList[0].regUser").description("등록자사번"),
////                                fieldWithPath("_embedded.historyList[0].updDate").description("수정일자"),
////                                fieldWithPath("_embedded.historyList[0].updUser").description("수정자사번"),
////                                fieldWithPath("_embedded.historyList[0].language").description("언어"),
////                                fieldWithPath("_embedded.historyList[0]._links.history-get.href").description("연혁 단건 조회"),
////                                fieldWithPath("_embedded.historyList[0]._links.history-update.href").description("연혁 단건 수정"),
////                                fieldWithPath("_embedded.historyList[0]._links.detail-management.href").description("연혁 세부 조회")
////                        )
////                        ))
//        ;
//
//
//    }
//
//    @DisplayName("연혁 단건 - 성공")
//    @Test
//    public void _테스트_get() throws Exception{
//
//        mockMvc.perform(get("/{lang}/history/{id}","kr","2")
//                        .accept(MediaTypes.HAL_JSON_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andDo(document("history-get",
////                        requestHeaders(
////                                headerWithName(HttpHeaders.ACCEPT).description("application/hal+json;charset-utf8")
////                        )
////                        ,
////                        relaxedResponseFields(
////                                fieldWithPath("id").description("연혁번호"),
////                                fieldWithPath("hiStartYear").description("시작연도"),
////                                fieldWithPath("hiEndYear").description("끝연도"),
////                                fieldWithPath("title").description("연도제목"),
////                                fieldWithPath("content").description("실제파일명"),
////                                fieldWithPath("hiOriginFileName").description("저장파일명"),
////                                fieldWithPath("hiSaveFileName").description("저장경로"),
////                                fieldWithPath("hiFileSavedPath").description("연혁내용"),
////                                fieldWithPath("regDate").description("등록일자"),
////                                fieldWithPath("regUser").description("등록자사번"),
////                                fieldWithPath("updDate").description("수정일자"),
////                                fieldWithPath("updUser").description("수정자사번"),
////                                fieldWithPath("language").description("언어"),
////                                fieldWithPath("adminUser").description("연혁번호"),
////                                fieldWithPath("_links.self.href").description("self")
////                        )
////
////                        ))
//        ;
//    }
//
//    @DisplayName("언어연혁 단건 수정 - 성공")
//    @Test
//    public void _테스트() throws Exception{
//
//        String updateContent = "update content.....";
//
//        History history = historyRepository.findById(1L).get();
//
//        HistorytDto requestDto = HistorytDto.builder()
//                .content(updateContent)
//                .build();
//
//        String valueAsString = objectMapper.writeValueAsString(requestDto);
//        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
//        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.jpg", "image/jpeg", "test file".getBytes(StandardCharsets.UTF_8) );
//
//        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
//                        .file(multipartFile)
//                        .file(multipartFile1)
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//        ;
//    }
//
//    @DisplayName("(에러)연혁 단건 수정 - badRequest - 파일 확장자 검증")
//    @Test
//    public void _테스트_badRequest_extBad() throws Exception{
//
//        String updateContent = "update content.....";
//
//        History history = historyRepository.findById(1L).get();
//
//        HistorytDto requestDto = HistorytDto.builder()
//                .content(updateContent)
//                .build();
//
//        String valueAsString = objectMapper.writeValueAsString(requestDto);
//        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
//        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.txt", "text/html", "test file".getBytes(StandardCharsets.UTF_8) );
//
//        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
//                        .file(multipartFile)
//                        .file(multipartFile1)
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//
//        )
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("errorMessage").exists())
//        ;
//    }
//
//    @DisplayName("연혁상세 list 조회 - 성공")
//    @Test
//    public void _테스트_detail_list_authentication() throws Exception{
//
//        mockMvc.perform(get("/kr/history/detail"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//
//    @DisplayName("연혁상세 단건 조회(인증없음) - 성공")
//    @Test
//    public void _테스트_detail_get() throws Exception{
//
//        mockMvc.perform(get("/kr/history/detail/1")
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("_links.create-event").exists())
//        ;
//    }
//
//    @DisplayName("연혁상세 단건 조회(인증 있는 경우) - 성공")
//    @Test
//    public void _테스트_detail_get_authentication() throws Exception{
//
//        mockMvc.perform(get("/kr/history/detail/1")
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
////                .andExpect()
//        ;
//    }
//
//    @DisplayName("연셕세부 수정 - 성공")
//    @Test
//    public void _테스트_detail_insert() throws Exception{
//
//        HistoryDetailDto historyDetail = HistoryDetailDto.builder()
//                .hdYear("1987")
//                .hdMonth("08")
//                .content("aaaaaaaaaaaaaaaaaaaaaaaa")
//                .hdSequence(12)
//                .build();
//
//        mockMvc.perform(post("/kr/history/detail")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(historyDetail))
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//        ;
//    }
//
////    @DisplayName("연혁세부 단건 삭제 - 성공")
////    @Test
////    public void _테스트_detail_detail() throws Exception{
////
////        mockMvc.perform(delete("/kr/history/detail/26")
////                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken())
////                )
////                .andDo(print())
////                .andExpect(status().isOk());
//
////    }
//    @DisplayName("연셕세부 수정 - 성공")
//    @Test
//    public void _테스트_detail_update() throws Exception{
//
//        HistoryDetailDto historyDetail = HistoryDetailDto.builder()
//                .hdYear("1987")
//                .hdMonth("08")
//                .content("gggggggupdate...")
//                .hdSequence(5)
//                .build();
//
//        mockMvc.perform(put("/kr/history/detail/34")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(historyDetail))
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                ;
//    }
//
//
//    @DisplayName("연셕세부 노출순서 변경 - 성공")
//    @Test
//    public void _테스트_detail_sequence_ok() throws Exception{
//
//        History history = historyRepository.findById(5L).get();
//
//        HistoryDetailDto historyDetail = HistoryDetailDto.builder()
//                .hdYear("1987")
//                .hdMonth("08")
//                .content("변경변경")
//                .hdSequence(3)
//                .build();
//
//        mockMvc.perform(post("/kr/history/detail")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(historyDetail))
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                ;
//
//    }
//
//
//    private String getBearerToken() throws Exception {
//        return "Bearer " + getAccessToken();
//    }
//
//    private String getAccessToken() throws Exception {
//
//        Set<AccountRole> roles = new HashSet<>();
//        roles.add(AccountRole.DS_POWER);
//        roles.add(AccountRole.DS_ENERGY);
//
//        String loginId = "gon1";
//        String loginPwd = "gon";
////
////        Account gon = Account.builder()
////                .loginId(loginId)
////                .loginPwd(loginPwd)
////                .roles(roles)
////                .build();
////
////        accountService.saveAccount(gon);
//
//        String clientId = "daesung";
//        String clientSecret = "pass";
//
//        ResultActions perform = mockMvc.perform(post("/oauth/token")
//                        .with(httpBasic(adminProperties.getClientId(), adminProperties.getClientSecret()))
//                        .param("username", adminProperties.getAdminUsername())
//                        .param("password", adminProperties.getAdminPassword())
//                        .param("grant_type", "password")
//                )
////                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("access_token").exists());
//
//
//        String responseBody = perform.andReturn().getResponse().getContentAsString();
//        Jackson2JsonParser parser = new Jackson2JsonParser();
//        String access_token = parser.parseMap(responseBody).get("access_token").toString();
//        System.out.println("access_token = " + access_token);
//        return access_token;
//
//    }
//
//}