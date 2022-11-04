package com.daesung.api.news.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.news.domain.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class NewsRepositoryTest extends BaseControllerTest {

    @Autowired
    NewsRepository newsRepository;


    @DisplayName("뉴스 insert")
    @Test
    public void _테스트_news_insert() throws Exception{

//        News.builder()
//                .nbType()
//                .title()
//                .content()
//                .viewCnt()
//                .newsFiles()
//                .newsThumbnailFile()
//                .build();

    }

    @DisplayName("보도 insert")
    @Test
    public void _테스트_report_insert() throws Exception{


    }

}