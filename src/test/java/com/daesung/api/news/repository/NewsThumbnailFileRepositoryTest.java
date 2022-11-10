package com.daesung.api.news.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.news.domain.NewsThumbnailFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewsThumbnailFileRepositoryTest extends BaseControllerTest {

    @Autowired
    NewsThumbnailFileRepository newsThumbnailFileRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        List<NewsThumbnailFile> byNewsId = newsThumbnailFileRepository.findByNewsId(1L);
//        for (NewsThumbnailFile newsThumbnailFile : byNewsId) {
//            System.out.println("newsThumbnailFile = " + newsThumbnailFile);
//        }

//        List<NewsThumbnailFile> dateDesc = newsThumbnailFileRepository.findByNewsIdOrderByRegDateDesc(1L);
//        for (NewsThumbnailFile newsThumbnailFile : dateDesc) {
//            System.out.println("newsThumbnailFile = " + newsThumbnailFile);
//        }

//        newsThumbnailFileRepository.deleteByNewId()
        int result = newsThumbnailFileRepository.deleteByNewsId(47L);
        System.out.println("result = " + result);


    }


}