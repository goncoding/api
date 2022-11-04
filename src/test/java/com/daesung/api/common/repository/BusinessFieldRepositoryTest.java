package com.daesung.api.common.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.Business;
import com.daesung.api.common.domain.BusinessField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

class BusinessFieldRepositoryTest extends BaseControllerTest {


    @Autowired
    BusinessFieldRepository businessFieldRepository;

    @Autowired
    BusinessRepository businessRepository;

    @DisplayName("dummy data")
    @Test
    @Commit
    public void _테스트() throws Exception{

        Business business01 = businessRepository.findById(1L).get();
        Business business02 = businessRepository.findById(2L).get();
        Business business03 = businessRepository.findById(3L).get();
        Business business04 = businessRepository.findById(4L).get();
        Business business05 = businessRepository.findById(5L).get();
        Business business06 = businessRepository.findById(6L).get();

        BusinessField businessField01 = BusinessField.builder()
                .busFieldName("DS파워")
                .busFieldInfo("정보 DS파워")
                .business(business01)
                .build();
        BusinessField businessField02 = BusinessField.builder()
                .busFieldName("대성히트에너시스")
                .busFieldInfo("정보 대성히트에너시스")
                .business(business01)
                .build();
        BusinessField businessField03 = BusinessField.builder()
                .busFieldName("대성산업(주) 해외자원개발부")
                .busFieldInfo("정보 대성산업(주) 해외자원개발부")
                .business(business01)
                .build();
        BusinessField businessField04 = BusinessField.builder()
                .busFieldName(" 대성산업(주) 석유가스사업부")
                .busFieldInfo("정보  대성산업(주) 석유가스사업부")
                .business(business01)
                .build();

        businessFieldRepository.save(businessField01);
        businessFieldRepository.save(businessField02);
        businessFieldRepository.save(businessField03);
        businessFieldRepository.save(businessField04);



        BusinessField businessField05 = BusinessField.builder()
                .busFieldName("대성쎌틱에너시스")
                .busFieldInfo("정보 대성쎌틱에너시스")
                .business(business02)
                .build();
     BusinessField businessField06 = BusinessField.builder()
                .busFieldName("대성C&S")
                .busFieldInfo("정보 대성C&S")
                .business(business02)
                .build();
     BusinessField businessField07 = BusinessField.builder()
                .busFieldName("한국캠브리지필터")
                .busFieldInfo("정보 한국캠브리지필터")
                .business(business02)
                .build();

        businessFieldRepository.save(businessField05);
        businessFieldRepository.save(businessField06);
        businessFieldRepository.save(businessField07);

        BusinessField businessField08 = BusinessField.builder()
                .busFieldName("대성계전")
                .busFieldInfo("정보 대성계전")
                .business(business03)
                .build();
        BusinessField businessField09 = BusinessField.builder()
                .busFieldName("대성산업㈜ 기계사업부")
                .busFieldInfo("정보 대성산업㈜ 기계사업부")
                .business(business03)
                .build();
        BusinessField businessField10 = BusinessField.builder()
                .busFieldName("대성나찌유압공업")
                .busFieldInfo("정보 대성나찌유압공업")
                .business(business03)
                .build();
        BusinessField businessField11 = BusinessField.builder()
                .busFieldName("한국물류용역")
                .busFieldInfo("정보 한국물류용역")
                .business(business03)
                .build();


        businessFieldRepository.save(businessField08);
        businessFieldRepository.save(businessField09);
        businessFieldRepository.save(businessField10);
        businessFieldRepository.save(businessField11);


        BusinessField businessField12 = BusinessField.builder()
                .busFieldName("정보시스템사업부")
                .busFieldInfo("정보 정보시스템사업부")
                .business(business04)
                .build();
        BusinessField businessField13 = BusinessField.builder()
                .busFieldName("대성아트센터")
                .busFieldInfo("정보 대성아트센터")
                .business(business04)
                .build();
        BusinessField businessField14 = BusinessField.builder()
                .busFieldName("디큐브거제백화점")
                .busFieldInfo("정보 디큐브거제백화점")
                .business(business04)
                .build();
        BusinessField businessField15 = BusinessField.builder()
                .busFieldName("문경새재관광")
                .busFieldInfo("정보 문경새재관광")
                .business(business04)
                .build();
        BusinessField businessField16 = BusinessField.builder()
                .busFieldName("대성물류건설")
                .busFieldInfo("정보 대성물류건설")
                .business(business04)
                .build();


        businessFieldRepository.save(businessField12);
        businessFieldRepository.save(businessField13);
        businessFieldRepository.save(businessField14);
        businessFieldRepository.save(businessField15);
        businessFieldRepository.save(businessField16);

        BusinessField businessField17 = BusinessField.builder()
                .busFieldName("대성본사")
                .busFieldInfo("정보 대성본사")
                .business(business06)
                .build();
        BusinessField businessField18 = BusinessField.builder()
                .busFieldName("기계사업부")
                .busFieldInfo("정보 기계사업부")
                .business(business06)
                .build();


        businessFieldRepository.save(businessField17);
        businessFieldRepository.save(businessField18);


    }

}