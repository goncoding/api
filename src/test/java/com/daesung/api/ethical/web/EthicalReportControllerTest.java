package com.daesung.api.ethical.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.EthicalReportRepository;
import com.daesung.api.ethical.web.dto.EthicalReportDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EthicalReportControllerTest extends BaseControllerTest {

    @Autowired
    EthicalReportRepository ethicalReportRepository;

    @DisplayName("윤리경영 등록")
    @Test
    public void _테스트() throws Exception{

        EthicalReportDto reportDto = EthicalReportDto.builder()
                .erName("테스트명")
                .erEmail("aaa@email.com")
                .erPhone("010-2222-3333")
                .erContent("테스트")
                .build();

        mockMvc.perform(post("/kr/ethical")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(reportDto))
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("윤리경영 수정")
    @Test
    public void _테스트_update_ok() throws Exception{

        EthicalReportDto reportDto = EthicalReportDto.builder()
                .erName("테스트명")
                .erEmail("aaa@email.com")
                .erPhone("010-2222-3333")
                .erContent("테스트")
//                .mnNum("1104")
//                .erMemo("test memo")
                .build();

        mockMvc.perform(put("/kr/ethical/{id}","1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(reportDto))
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

                
                
                
                


}