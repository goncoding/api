package com.daesung.api.news.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class NewsRepositoryTest extends BaseControllerTest {

    @Autowired
    NewsRepository newsRepository;


    @DisplayName("뉴스 insert")
    @Test
    public void _테스트_news_insert() throws Exception{

        NewsSearchCondition condition = NewsSearchCondition.builder()
                .nbType(NbType.RE)
                .searchTitle("대성쎌틱")
//                .searchText("content..222")
                .build();

        Sort sort = Sort.by("newsId").descending();
        Pageable pageable= PageRequest.of(0, 9, sort);


        Page<News> news = newsRepository.searchNewsList(condition, pageable);

        for (News news1 : news) {
            System.out.println("news1 = " + news1);
        }

    }

    @DisplayName("보도 insert")
    @Test
    public void _테스트_report_insert() throws Exception{


    }

}