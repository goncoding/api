package com.daesung.api.common.repository;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Department;
import com.daesung.api.common.domain.Manager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerRepositoryTest extends BaseControllerTest {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    BusinessFieldRepository businessFieldRepository;

    @Autowired
    DepartmentRepository departmentRepository;


    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        Manager byMnNum = managerRepository.findByMnNum("1102").get();
//        System.out.println("byMnNum = " + byMnNum);

//        List<Manager> byAccountRole = managerRepository.findByAccountRole("DS_HEAD");
//        for (Manager manager : byAccountRole) {
//            System.out.println("manager = " + manager);
//        }

//        BusinessField businessField = businessFieldRepository.findByBusFieldNum("B001").get();
//        System.out.println("businessField = " + businessField);
//        List<Manager> byBusinessField = managerRepository.findByBusinessField(businessField);
//        for (Manager manager : byBusinessField) {
//            System.out.println("manager = " + manager);
//        }

        Department department = departmentRepository.findByDeptNum("D004").get();
        Manager manager = managerRepository.findByMnDepartment(department).get();
        System.out.println("manager = " + manager);


    }


}