//package com.daesung.api.ir.web;
//
//import com.daesung.api.common.BaseControllerTest;
//import com.daesung.api.ir.domain.DisclosureInfo;
//import com.daesung.api.ir.repository.DisclosureInfoRepository;
//import com.daesung.api.ir.web.dto.DisclosureInfoDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.FileInputStream;
//import java.nio.charset.StandardCharsets;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//class DisclosureInfoControllerTest extends BaseControllerTest {
//
//    @Autowired
//    DisclosureInfoRepository disclosureInfoRepository;
//
////    @DisplayName("전자공시 - 리스트 조회")
////    @Test
////    public void _테스트() throws Exception{
////
////    }
//
//   /* @DisplayName("전자공시 - 등록")
//    @Test
//    public void _테스트() throws Exception{
//
//        String path = "C:/daesung/poto/cat01.jpg";
//
//        FileInputStream fileInputStream = new FileInputStream(path);
//
//        DisclosureInfoDto dto = DisclosureInfoDto.builder()
//                .diTitle("외부인 선임보고")
//                .build();
//
//        MockMultipartFile jsonResult = new MockMultipartFile("disClosureDto", "disClosureDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
//        MockMultipartFile attachFile = new MockMultipartFile("attachFile", "cat01.jpg", "image/jpeg", fileInputStream);
//
//
//        mockMvc.perform(multipart("/kr/disclosure-info")
//
//        )*/
//
//
//    }
//
////    @DisplayName("전자공시 - 삭제")
////    @Test
////    public void _테스트() throws Exception{
////
////    }
//
//
//}