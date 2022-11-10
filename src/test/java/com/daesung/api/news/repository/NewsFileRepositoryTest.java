package com.daesung.api.news.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.enumType.ShowYn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class NewsFileRepositoryTest extends BaseControllerTest {

    @Autowired
    NewsFileRepository newsFileRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        List<NewsFile> all = newsFileRepository.findAll();
//        for (NewsFile newsFile : all) {
//            System.out.println("newsFile = " + newsFile);
//        }

//        List<NewsFile> newsFiles = newsFileRepository.findByNewsId(1L);
//        for (NewsFile newsFile : newsFiles) {
//            System.out.println("newsFile = " + newsFile);
//        }


//        int resultInt = newsFileRepository.updateShowN(38L);
//        System.out.println("resultInt = " + resultInt);

//        List<NewsFile> byNewsIdAndShowYn = newsFileRepository.findByNewsIdAndShowYn(42L, ShowYn.Y);
//        for (NewsFile newsFile : byNewsIdAndShowYn) {
//            System.out.println("newsFile = " + newsFile);
//        }

//        String name = "collect-img01_b7fc8234-991d-480d-b624-ab2cfaf4f9c5.jpg";
//        NewsFile byNewsFileSavedName = newsFileRepository.findByNewsFileSavedName(name);
//        System.out.println("byNewsFileSavedName = " + byNewsFileSavedName);

        int result = newsFileRepository.deleteByNewsId(49L);
        System.out.println("result = " + result);


    }



}