package com.daesung.api.ethical.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.daesung.api.ethical.web.dto.EthicalUpdateResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

class EthicalReportRepositoryTest extends BaseControllerTest {

    @Autowired
    EthicalReportRepository ethicalReportRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        EthicalUpdateResponseDto ethicalReportAndManager = ethicalReportRepository.findEthicalReportAndManager(1L);
//        System.out.println("ethicalReportAndManager = " + ethicalReportAndManager);

        EthicalSearchCondition condition = EthicalSearchCondition.builder()
                .searchType("mnName")
//                .searchName("홍길동")
                .searchMnName("김상수")
//                .searchText("1111")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<EthicalReport> ethicalReportPage = ethicalReportRepository.searchEthicalList(condition, pageable);
        for (EthicalReport ethicalReport : ethicalReportPage) {
            System.out.println("ethicalReport = " + ethicalReport);
        }

    }


}