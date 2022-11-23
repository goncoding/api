package com.daesung.api.common.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.Business;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusinessRepositoryTest extends BaseControllerTest {

    @Autowired
    BusinessRepository businessRepository;

    @DisplayName("단일 dummy data")
    @Test
    public void _테스트2() throws Exception{

        Business business01 = Business.builder()
                .businessName("재무IR기획부")
                .businessInfo("정보 재무IR기획부")
                .build();

        businessRepository.save(business01);


    }

    @DisplayName("dummy data")
    @Test
    @Commit
    public void _테스트() throws Exception{

        Business business01 = Business.builder()
                .businessName("GREEN ENERGY")
                .businessInfo("GREEN ENERGY INFO")
                .build();
        Business business02 = Business.builder()
                .businessName("GREEN ENVIRONMENT")
                .businessInfo("GREEN ENVIRONMENT INFO")
                .build();
        Business business03 = Business.builder()
                .businessName("GENERAL MACHINERY")
                .businessInfo("GENERAL MACHINERY INFO")
                .build();
        Business business04 = Business.builder()
                .businessName("IT & Life·Culture")
                .businessInfo("IT & Life·Culture INFO")
                .build();
        Business business05 = Business.builder()
                .businessName("GLOBAL BUSINESS")
                .businessInfo("GLOBAL BUSINESS INFO")
                .build();
        Business business06 = Business.builder()
                .businessName("ELSE")
                .businessInfo("ELSE INFO")
                .build();

        businessRepository.save(business01);
        businessRepository.save(business02);
        businessRepository.save(business03);
        businessRepository.save(business04);
        businessRepository.save(business05);
        businessRepository.save(business06);
    }

    @DisplayName("")
    @Test
    public void _테스트_select() throws Exception{

//        Business business = businessRepository.findById(1L).get();
//        System.out.println("business = " + business);
        List<Business> all = businessRepository.findAll();
        for (Business business : all) {
            System.out.println("business = " + business);
        }

    }


}