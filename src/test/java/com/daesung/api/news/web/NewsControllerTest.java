package com.daesung.api.news.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.web.dto.HistorytDto;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.NewsRepository;
import com.daesung.api.news.web.dto.NewsDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NewsControllerTest extends BaseControllerTest {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    ModelMapper modelMapper;

    @DisplayName("news insert")
    @Test
    @Commit
    public void test012() throws Exception {


        NewsDto news = NewsDto.builder()
                .nbType("NE")
                .title("title")
                .content("content")
                .viewCnt(21L)
                .language("kr")
                .thumbSummary("썸네일 관련 내용입니다.")
                .build();

        String valueAsString = objectMapper.writeValueAsString(news);
        MockMultipartFile multipartFile = new MockMultipartFile("newsDto", "requestDto", "application/json", valueAsString.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile1 = new MockMultipartFile("thumbnailFile", "test01.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/{lang}/news","kr")
                        .file(multipartFile)
                        .file(multipartFile1)
//                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())

                )
                .andDo(print())
                .andExpect(status().isCreated())
        ;




    }

    @DisplayName("reposrt insert")
    @Test
    @Commit
    public void test0123() throws Exception {

        NewsDto newsDto = NewsDto.builder()
//                .nbType(NbType.NE.name())
//                .title("title..")
//                .content("content...")
                .newCompany("다음뉴스")
                .link("https://v.daum.net/v/20220919104308505")
                .viewCnt(21L)
                .language("ko")
                .build();

        News news = modelMapper.map(newsDto, News.class);

        News save = newsRepository.save(news);

        System.out.println("save = " + save);

    }

    @DisplayName("30개의 new를 10개씩 두번째 페이지 조회하기..")
    @Test
    public void test01() throws Exception {

        mockMvc.perform(get("/kr/news")
                .param("page","1")
                .param("size","5")
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("기존의 이벤트 하나로 조회하기")
    @Test
    public void _테스트() throws Exception{

        Long id = 2L;

        mockMvc.perform(get("/kr/news/"+id)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}




















