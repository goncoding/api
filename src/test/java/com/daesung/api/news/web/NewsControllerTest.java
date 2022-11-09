package com.daesung.api.news.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.NewsRepository;
import com.daesung.api.news.web.dto.NewsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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


        News news = News.builder()
                .nbType(NbType.NE)
                .title("title")
                .content("content")
                .viewCnt(21L)
                .language("kr")
                .build();

        News save = newsRepository.save(news);
        System.out.println("save = " + save);


//        News save = newsRepository.save(news);

//        System.out.println("save = " + save);

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

        mockMvc.perform(get("/api/kr/news")
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

        mockMvc.perform(get("/api/kr/news/"+id)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}




















