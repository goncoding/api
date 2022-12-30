package com.daesung.api.history.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.utils.search.Search;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class HistoryRecordRepositoryTest extends BaseControllerTest {

    @Autowired
    HistoryRecordRepository historyRecordRepository;

    @DisplayName("sql 테스트")
    @Test
    public void _테스트() throws Exception{

//        Search search = Search.builder()
//                .searchType("tit")
//                .searchTitle("01")
//                .hrCategory(HrCategory.NA)
//                .build();
//
//        HistoryRecord prevRecord = historyRecordRepository.searchPrevRecord(21L, search);
//        System.out.println("prevRecord = " + prevRecord);
//        System.out.println("###########################");
//        System.out.println("###########################");
//        HistoryRecord nextRecord = historyRecordRepository.searchNextRecord(21L, search);
//        System.out.println("nextRecord = " + nextRecord);


//        HistoryRecord en = historyRecordRepository.findByIdAndLanguage(88L, "kr");
//        System.out.println("en = " + en);

    }


}