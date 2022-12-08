package com.daesung.api.ir.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.utils.search.Search;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IrInfoRepositoryTest extends BaseControllerTest {

    @Autowired
    IrInfoRepository irInfoRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        Search search = Search.builder()
//                .build();
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//
//        Page<IrInfo> irInfos = irInfoRepository.searchIrInfoList(search, pageRequest);
//
//        for (IrInfo irInfo : irInfos) {
//            System.out.println("irInfo = " + irInfo);
//        }

//        List<IrInfo> byIrYear = irInfoRepository.findByIrYear(2L);
//        for (IrInfo irInfo : byIrYear) {
//            System.out.println("irInfo = " + irInfo);
//        }

        PageRequest of = PageRequest.of(0, 1);

        List<IrInfo> irInfos = irInfoRepository.findbyLastIrId(of);
        for (IrInfo irInfo : irInfos) {
            System.out.println("irInfo = " + irInfo);
        }


    }



}