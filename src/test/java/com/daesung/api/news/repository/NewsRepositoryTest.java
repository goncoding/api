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
import org.springframework.data.jpa.repository.Query;

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


//        @Query(nativeQuery = true, value = "SELECT n.id  FROM News n where n.id  < :id  ORDER BY n.id DESC Limit 1")
//        News findByIdPrev(Long id);
//
//        @Query(nativeQuery = true, value = "SELECT n.id  FROM News n where n.id  > :id  ORDER BY n.id DESC Limit 1")
//        News findByIdNext(Long id);


    }


    @DisplayName("뉴스 insert")
    @Test
    public void _테스트_news_search() throws Exception{

        NewsSearchCondition condition = NewsSearchCondition.builder()
                .nbType(NbType.NE)
                .searchTitle("title..1")
//                .searchText("content..222")
                .build();

        News news = newsRepository.searchPrevNews(18L, condition);

        System.out.println("news = " + news);

        News news2 = newsRepository.searchNextNews(18L, condition);

        System.out.println("news2 = " + news2);




    }

}