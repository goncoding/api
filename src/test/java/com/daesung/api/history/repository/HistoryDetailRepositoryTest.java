package com.daesung.api.history.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HistoryDetailRepositoryTest extends BaseControllerTest {

    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    HistoryDetailRepository historyDetailRepository;

    @DisplayName("연혁 디테일 dummy 데이터")
    @Test
    public void _테스트() throws Exception{

        History history01 = historyRepository.findById(1L).get();
        History history02 = historyRepository.findById(2L).get();
        History history03 = historyRepository.findById(3L).get();
        History history04 = historyRepository.findById(4L).get();
        History history05 = historyRepository.findById(5L).get();

        HistoryDetail historyDetail01 = HistoryDetail.builder()
                .content("대성산업공사 설립")
                .hdYear("1947")
                .hdMonth("05")
                .history(history01)
                .hdSequence(1)
                .build();

        historyDetailRepository.save(historyDetail01);

        for (int i = 1; i < 7; i++) {

            Random random = new Random();

            HistoryDetail historyDetail = HistoryDetail.builder()
                    .content("대성산업공사 설립"+i)
                    .hdYear("1947")
                    .hdMonth("05")
                    .history(history02)
                    .hdSequence(i)
                    .build();

            historyDetailRepository.save(historyDetail);

        }

        for (int i = 1; i < 7; i++) {

            Random random = new Random();

            HistoryDetail historyDetail = HistoryDetail.builder()
                    .content("대성산업공사 설립"+i)
                    .hdYear("1977")
                    .hdMonth("05")
                    .history(history03)
                    .hdSequence(i)
                    .build();

            historyDetailRepository.save(historyDetail);

        }

        for (int i = 1; i < 5; i++) {

            Random random = new Random();

            HistoryDetail historyDetail = HistoryDetail.builder()
                    .content("대성산업공사 설립"+i)
                    .hdYear("2000")
                    .hdMonth("05")
                    .history(history04)
                    .hdSequence(i)
                    .build();

            historyDetailRepository.save(historyDetail);

        }

        for (int i = 1; i < 5; i++) {

            Random random = new Random();

            HistoryDetail historyDetail = HistoryDetail.builder()
                    .content("대성산업공사 설립"+i)
                    .hdYear("2007")
                    .hdMonth("05")
                    .history(history05)
                    .hdSequence(i)
                    .build();

            historyDetailRepository.save(historyDetail);

        }



    }

}