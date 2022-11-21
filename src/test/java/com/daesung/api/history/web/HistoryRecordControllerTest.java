package com.daesung.api.history.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.history.repository.HistoryRecordFileRepository;
import com.daesung.api.history.repository.HistoryRecordRepository;
import com.daesung.api.history.web.dto.RecordDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HistoryRecordControllerTest extends BaseControllerTest {

    @Autowired
    HistoryRecordRepository historyRecordRepository;

    @Autowired
    HistoryRecordFileRepository historyRecordFileRepository;

    @DisplayName("(연혁기록) 리스트 조회")
    @Test
    public void _테스트() throws Exception{

        mockMvc.perform(get("/{lang}/history/record","kr")
                        .param("searchType","tit")
                        .param("searchText","테스트")
                        .param("recordType","CO")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("(연혁기록) 등록 - 성공")
    @Test
    public void _테스트_insert() throws Exception{


        RecordDto recordDto = RecordDto.builder()
                .hrCategory("HY")
                .hrCategoryName("신년사")
                .hrTitle("신년사 관련 타이틀")
                .hrContent("신년사 관련 내용")
                .build();

        String valueAsString = objectMapper.writeValueAsString(recordDto);

        MockMultipartFile jsonResult = new MockMultipartFile("recordDto", "recordDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile = new MockMultipartFile("recordFiles", "cat01.jpg", "image/jpeg", valueAsString.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/{lang}/history/record","kr")
                        .file(jsonResult)
                        .file(multipartFile)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 기존 file { id, seq}와 동일한 데이터가 있으면 안됨.
     * 75번에 01파일 추가시 01파일은 삭제되어있어야함.
     */
    @DisplayName("(연혁기록) 수정 - 성공")
    @Test
    public void _테스트_update() throws Exception{


        RecordDto recordDto = RecordDto.builder()
                .hrCategory("CI")
                .hrCategoryName("update..")
                .hrTitle("update..")
                .hrContent("update..")
                .build();

        String valueAsString = objectMapper.writeValueAsString(recordDto);

        MockMultipartFile jsonResult = new MockMultipartFile("recordDto", "recordDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile01 = new MockMultipartFile("recordFiles", "cat01.jpg", "image/jpeg", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile02 = new MockMultipartFile("recordFiles", "cat02.jpg", "image/jpeg", valueAsString.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/{lang}/history/record/modify/{id}","kr","75")
                        .file(jsonResult)
                        .file(multipartFile01)
                        .file(multipartFile02)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("(연혁기록) 단건 삭제")
    @Test
    public void _테스트_delete() throws Exception{

        mockMvc.perform(delete("/{lang}/history/record/{id}","kr","7"))
                .andDo(print())
                .andExpect(status().isOk());
    }






}