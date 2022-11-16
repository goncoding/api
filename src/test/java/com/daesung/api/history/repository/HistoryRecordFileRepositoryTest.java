package com.daesung.api.history.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.history.domain.HistoryRecordFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryRecordFileRepositoryTest extends BaseControllerTest {

    @Autowired
    HistoryRecordFileRepository historyRecordFileRepository;

    @DisplayName("")
    @Test
    @Transactional
    public void _테스트() throws Exception{

        List<HistoryRecordFile> byHistoryRecord = historyRecordFileRepository.findByHrId(1L);
        for (HistoryRecordFile historyRecordFile : byHistoryRecord) {
            System.out.println("historyRecordFile = " + historyRecordFile);
        }


    }


}