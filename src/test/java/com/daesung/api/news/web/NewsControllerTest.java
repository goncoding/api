package com.daesung.api.news.web;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.events.EventRepository;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.NewsRepository;
import com.daesung.api.news.web.dto.NewsForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        NewsForm newsForm = NewsForm.builder()
//                .nbType(NbType.NE.name())
//                .title("title..")
//                .content("content...")
                .newCompany("다음뉴스")
                .link("https://v.daum.net/v/20220919104308505")
                .viewCnt(21L)
                .language("ko")
                .build();

        News news = modelMapper.map(newsForm, News.class);

        News save = newsRepository.save(news);

        System.out.println("save = " + save);

    }

    @DisplayName("뉴스 insert  정상")
    @Test
    public void test01() throws Exception {

        NewsForm newsForm = NewsForm.builder()
//                .nbType(NbType.RE.name())
                .title("title..")
                .content("content...")
//                .newCompany()
//                .link()
                .viewCnt(21L)
                .language("ko")
                .build();

        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newsForm))
        )
                .andDo(print())
                .andExpect(status().isCreated());


    }


}