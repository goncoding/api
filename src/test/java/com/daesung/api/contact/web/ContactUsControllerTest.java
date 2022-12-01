//package com.daesung.api.contact.web;
//
//import com.daesung.api.common.BaseControllerTest;
//import com.daesung.api.common.repository.ManagerRepository;
//import com.daesung.api.contact.domain.ContactUs;
//import com.daesung.api.contact.repository.ContactUsRepository;
//import com.daesung.api.contact.web.dto.ContactUsDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
//import java.util.Random;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class ContactUsControllerTest extends BaseControllerTest {
//
//    @Autowired
//    ContactUsRepository contactUsRepository;
//
//    @Autowired
//    ManagerRepository managerRepository;
//
//    @DisplayName("1대1 list 조회 - 성공")
//    @Test
//    public void get_list_ok() throws Exception{
//
//        mockMvc.perform(get("/kr/contact")
//                        .param("page","0")
//                        .param("size","10")
////                        .param("searchType","mnName")
////                        .param("searchText","직원02")
//                                .param("searchType","busFieldName")
//                                .param("searchText","DS파워")
//                )
//                .andExpect(status().isOk())
//                .andDo(print())
//        ;
//
//    }
//
//    @DisplayName("1대1 등록 - 성공")
//    @Test
//    public void post_insert_ok() throws Exception{
//
////        Random random = new Random();
////        for (int i = 3; i < 100; i++) {
////
////            String value = String.valueOf(random.nextInt((3 - 1) + 1) + 1);
////
////            ContactUsDto dto = ContactUsDto.builder()
////                    .cuName("홍길동0" + i)
////                    .cuEmail("a" + i + "@email.com")
////                    .cuPhone("010-222-3333")
////                    .cuContent("문의 내용..." + i)
////                    .mnNum(value)
////                    .build();
////
////            mockMvc.perform(post("/kr/contact")
////                            .contentType(MediaType.APPLICATION_JSON_VALUE)
////                            .content(objectMapper.writeValueAsString(dto))
////                    )
////                    .andExpect(status().isOk())
////            ;
////        }
//
//    ContactUsDto dto = ContactUsDto.builder()
//                    .cuName("홍길동")
//                    .cuEmail("aaa@email.com")
//                    .cuPhone("010-222-3333")
//                    .cuContent("문의 내용...")
//                    .build();
//
//
//        mockMvc.perform(post("/kr/contact")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto))
//        )
//                .andExpect(status().isOk())
//                .andDo(print())
//                ;
//    }
//
//    @DisplayName("1대1 등록 - bad_request_email")
//    @Test
//    public void post_insert_bad_request() throws Exception{
//
//        ContactUsDto dto = ContactUsDto.builder()
//                .cuName("홍길동")
//                .cuEmail("aaaemail.com")
//                .cuPhone("010-2222-3333")
//                .cuContent("문의 내용...........")
//                .build();
//
//        mockMvc.perform(post("/kr/contact")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto))
//        )
//                .andExpect(status().isBadRequest())
//                .andDo(print())
//                ;
//    }
//
//    @DisplayName("1대1 단건 조회 - 성공")
//    @Test
//    public void get_detail_ok() throws Exception{
//
//        mockMvc.perform(get("/kr/contact/{id}",1L))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("1대1 수정 - 성공")
//    @Test
//    public void put_update_ok() throws Exception{
//
//
//        ContactUsDto dto = ContactUsDto.builder()
//                .cuName("33홍길동")
//                .cuEmail("33aaa2@email.com")
//                .cuPhone("010-3222-3333")
//                .cuContent("update...........")
//                .mnNum("1103")
//                .build();
//
//        mockMvc.perform(put("/kr/contact/{id}",1L)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(dto))
//                )
//                .andExpect(status().isOk())
//                .andDo(print())
//        ;
//    }
//
//
//
//}