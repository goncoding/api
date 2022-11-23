package com.daesung.api.common.domain;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.ManagerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

class ManagerTest extends BaseControllerTest {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    BusinessFieldRepository businessFieldRepository;

    @DisplayName("dummay 하나 추가")
    @Test
    public void _테스트_insert() throws Exception{

        BusinessField businessField01 = businessFieldRepository.findById(19L).get();

//        Manager manager01 = Manager.builder()
//                .mnNum("1105")
//                .mnName("김상수")
////                .mnCategory(MnCategory.DS_RELAY)
//                .mnCategory("윤리경영신고")
//                .mnDepartment("감사실")
//                .mnPosition("대리")
//                .mnPhone("010-2222-3333")
//                .mnEmail("aaa@email.com")
//                .businessField(businessField01)
//                .build();
//
//        managerRepository.save(manager01);

        Manager manager01 = Manager.builder()
                .mnNum("1105")
                .mnName("김상수")
//                .mnCategory(MnCategory.DS_RELAY)
                .mnCategory("윤리경영신고")
                .mnDepartment("감사실")
                .mnPosition("대리")
                .mnPhone("010-2222-3333")
                .mnEmail("aaa@email.com")
                .businessField(businessField01)
                .build();

        managerRepository.save(manager01);

    }

    @DisplayName("dummay data insert")
    @Test
    public void _테스트() throws Exception{

        BusinessField businessField01 = businessFieldRepository.findById(8L).get();
        BusinessField businessField02 = businessFieldRepository.findById(1L).get();
        BusinessField businessField03 = businessFieldRepository.findById(2L).get();


        Manager manager01 = Manager.builder()
                .mnNum("1101")
                .mnName("직원01")
//                .mnCategory(MnCategory.DS_RELAY)
                .mnDepartment("부서01")
                .mnPosition("대리")
                .mnPhone("010-2222-3333")
                .mnEmail("aaa@email.com")
                .businessField(businessField01)
                .build();

     Manager manager02 = Manager.builder()
                .mnNum("1102")
                .mnName("직원02")
//                .mnCategory(MnCategory.DS_POWER)
                .mnDepartment("부서02")
                .mnPosition("과장")
                .mnPhone("010-545-4533")
                .mnEmail("bbb@email.com")
             .businessField(businessField02)
                .build();

     Manager manager03 = Manager.builder()
                .mnNum("1103")
                .mnName("직원03")
//                .mnCategory(MnCategory.DS_ENERGY)
                .mnDepartment("부서03")
                .mnPosition("차장")
                .mnPhone("010-2233-444")
                .mnEmail("ccc@email.com")
             .businessField(businessField03)
                .build();


     managerRepository.save(manager01);
     managerRepository.save(manager02);
     managerRepository.save(manager03);

    }




}