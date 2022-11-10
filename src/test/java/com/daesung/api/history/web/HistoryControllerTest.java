package com.daesung.api.history.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.events.EventDto;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.repository.HistoryRepository;
import com.daesung.api.history.web.dto.HistoryRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HistoryControllerTest extends BaseControllerTest {

    @Autowired
    HistoryRepository historyRepository;

    @DisplayName("update 수정 - 성공")
    @Test
    public void _테스트() throws Exception{

        String updateContent = "update content.....";

        History history = historyRepository.findById(1L).get();

        HistoryRequestDto requestDto = HistoryRequestDto.builder()
                .content(updateContent)
                .build();

        String valueAsString = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.jpg", "image/jpeg", "test file".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
                .file(multipartFile)
                .file(multipartFile1)

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

        HistoryRequestDto requestDto = HistoryRequestDto.builder()
                .content(updateContent)
                .build();

        String valueAsString = objectMapper.writeValueAsString(requestDto);
        MockMultipartFile multipartFile = new MockMultipartFile("requestDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.txt", "text/html", "test file".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/{lang}/history/modify/{id}","kr", history.getId())
                .file(multipartFile)
                .file(multipartFile1)

        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").exists())
        ;
    }





}