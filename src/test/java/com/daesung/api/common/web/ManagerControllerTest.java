package com.daesung.api.common.web;

import com.daesung.api.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManagerControllerTest extends BaseControllerTest {

    @DisplayName("연혁 list 조회 - 성공")
    @Test
    public void _테스트_list() throws Exception{

        mockMvc.perform(get("/{lang}/manager","kr"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("연혁 단일 조회 - 성공")
    @Test
    public void _테스트_get() throws Exception{

        mockMvc.perform(get("/{lang}/manager/{id}","kr","1101"))
                .andDo(print())
                .andExpect(status().isOk());
    }


}