package com.daesung.api.nice.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.nice.domain.NiceInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class NiceCertRepositoryTest extends BaseControllerTest {

    @Autowired
    NiceCertRepository niceCertRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{
        NiceInfo reqNo = niceCertRepository.findByReqNo("REC_e9322af70a4644b4");
        System.out.println("reqNo = " + reqNo);
    }


}