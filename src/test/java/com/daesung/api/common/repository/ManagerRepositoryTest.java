package com.daesung.api.common.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.Manager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ManagerRepositoryTest extends BaseControllerTest {

    @Autowired
    ManagerRepository managerRepository;


    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

        Manager byMnNum = managerRepository.findByMnNum("1102").get();
        System.out.println("byMnNum = " + byMnNum);

    }


}