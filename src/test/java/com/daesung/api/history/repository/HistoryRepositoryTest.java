package com.daesung.api.history.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.domain.History;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryRepositoryTest extends BaseControllerTest {

    @Autowired
    HistoryRepository historyRepository;

    @DisplayName("dummy data")
    @Test
    public void kr_테스트() throws Exception{

        History history01 = History.builder()
                .hiStartYear("1947")
                .title("1947's")
                .content("현 대성의 모태")
                .build();

        History history02 = History.builder()
                .hiStartYear("1957")
                .hiEndYear("1976")
                .title("1957’s ~ 1976’s")
                .content("에너지 전문기업 기반 구축")
                .build();

        History history03 = History.builder()
                .hiStartYear("1977")
                .hiEndYear("1999")
                .title("1977's ~1999's")
                .content("사업다각화")
                .build();

        History history04 = History.builder()
                .hiStartYear("2000")
                .hiEndYear("2005")
                .title("2000's ~2005's")
                .content("제 2창업, 새로운 도전의 시작")
                .build();

        History history05 = History.builder()
                .hiStartYear("2006")
                .hiEndYear("현재")
                .title("2006's ~현재")
                .content("신규사업 확대 및 본격적인 해외 진출")
                .build();


        historyRepository.save(history01);
        historyRepository.save(history02);
        historyRepository.save(history03);
        historyRepository.save(history04);
        historyRepository.save(history05);

    }
    @DisplayName("en_dummy data")
    @Test
    public void en_테스트() throws Exception{

        History history01 = History.builder()
                .hiStartYear("1947")
                .title("1947's")
                .content("the birthplace of modernity")
                .language("en")
                .build();

        History history02 = History.builder()
                .hiStartYear("1957")
                .hiEndYear("1976")
                .title("1957’s ~ 1976’s")
                .content("Building a Foundation for Energy Specialized Companies")
                .language("en")
                .build();

        History history03 = History.builder()
                .hiStartYear("1977")
                .hiEndYear("1999")
                .title("1977's ~1999's")
                .content("business diversification")
                .language("en")
                .build();

        History history04 = History.builder()
                .hiStartYear("2000")
                .hiEndYear("2005")
                .title("2000's ~2005's")
                .content("The 2nd Start-up, the Beginning of a New Challenge")
                .language("en")
                .build();

        History history05 = History.builder()
                .hiStartYear("2006")
                .hiEndYear("present")
                .title("2006's ~ present")
                .content("Expansion of new business and full-scale overseas expansion")
                .language("en")
                .build();

        historyRepository.save(history01);
        historyRepository.save(history02);
        historyRepository.save(history03);
        historyRepository.save(history04);
        historyRepository.save(history05);

    }

    @DisplayName("")
    @Test
    public void _테스트02() throws Exception{

//        History history = historyRepository.searchHistoryBetween("1988");
//        System.out.println("history = " + history);

//        List<History> historyList = historyRepository.findByLanguage("en");
//        for (History history : historyList) {
//            System.out.println("history = " + history);
//        }

    }



}