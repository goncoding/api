package com.daesung.api.ir.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.repository.IrYearRepository;
import com.daesung.api.ir.web.dto.IrYearDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IrYearControllerTest extends BaseControllerTest {

    @Autowired
    IrYearRepository irYearRepository;

    @DisplayName("list 테스트")
    @Test
    public void _테스트_list_get() throws Exception{

        mockMvc.perform(get("/kr/ir-year")
                )
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }


    @DisplayName("get 테스트")
    @Test
    public void _테스트_get() throws Exception{

        mockMvc.perform(get("/kr/ir-year/{id}","2")
                )
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @DisplayName("insert 테스트")
    @Test
    public void _테스트() throws Exception{

        IrYearDto dto = IrYearDto.builder()
                .iyYear("2021")
//                .regUser("reg01")
                .build();

        mockMvc.perform(post("/kr/ir-year")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("iyYear").value("2023"))
                .andDo(print())
        ;
    }


    @DisplayName("update 테스트")
    @Test
    public void _테스트_update() throws Exception{

        IrYear irYear = irYearRepository.findById(3L).get();

        IrYearDto dto = IrYearDto.builder()
                .iyYear("2023")
//                .regUser("reg02")
                .build();


        mockMvc.perform(put("/kr/ir-year/{id}", irYear.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @DisplayName("delete 테스트")
    @Test
    public void _테스트_delete() throws Exception{

        mockMvc.perform(delete("/kr/ir-year/{id}","2")
                )
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }


}