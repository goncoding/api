package com.daesung.api.ir.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.repository.IrInfoRepository;
import com.daesung.api.ir.web.dto.IrInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IrInfoControllerTest extends BaseControllerTest {

    @Autowired
    IrInfoRepository irInfoRepository;

    @DisplayName("IR 자료관리 - 등록")
    @Test
    public void insert_테스트() throws Exception{

        String path = "C:/daesung/poto/cat01.jpg";

        FileInputStream fileInputStream = new FileInputStream(path);

        IrInfoDto dto = IrInfoDto.builder()
                .irYear("2020")
                .irType("BR")
                .irTitle("2020년 사업보고서")
                .build();

        String valueAsString = objectMapper.writeValueAsString(dto);

        MockMultipartFile jsonResult = new MockMultipartFile("irInfoDto", "irInfoDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile attachFile = new MockMultipartFile("attachFile", "cat01.jpg", "image/jpeg", fileInputStream);

        mockMvc.perform(multipart("/kr/ir")
                .file(jsonResult)
                .file(attachFile)
        )
                .andDo(print())
                .andExpect(status().isOk());


    }

    @DisplayName("IR 자료관리 - 삭제")
    @Test
    public void delete_테스트() throws Exception{

        mockMvc.perform(delete("/kr/ir/{id}","26"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("IR 자료관리 - 리스트 조회")
    @Test
    public void list_테스트() throws Exception{

        mockMvc.perform(get("/kr/ir"))
                .andDo(print())
                .andExpect(status().isOk());

    }



}