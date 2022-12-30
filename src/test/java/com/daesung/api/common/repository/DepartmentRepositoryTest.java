package com.daesung.api.common.repository;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.domain.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRepositoryTest extends BaseControllerTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @DisplayName("")
    @Test
    public void _테스트_repository() throws Exception{

//        Department department = departmentRepository.findByDeptNum("D001").get();
//        System.out.println("department = " + department);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String today = simpleDate.format(time);
        System.out.println("today = " + today);

        int year  = Integer.parseInt(today.substring(0, 4));
        int month = Integer.parseInt(today.substring(4, 6));
        int date  = Integer.parseInt(today.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date);

        cal.add(Calendar.YEAR, -1);     // 1년 전

        String lastYear = simpleDate.format(cal.getTime());
        System.out.println("lastYear = " + lastYear);

    }

    @DisplayName("")
    @Test
    public void _테스트2() throws Exception{

        Department deptNumber = departmentRepository.findByDeptNumber("D002");
        System.out.println("deptNumber = " + deptNumber);


    }


    private static String CalculationDate(String sttDate, int year, int month , int day) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();

        Date date = sdf.parse(sttDate);

        cal.setTime(date);

        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);

        return sdf.format(cal.getTime());
    }

    @DisplayName("더미 데이터 생성")
    @Test
    public void _테스트() throws Exception{

//        Department department = Department.builder()
//                .deptNum("D001")
//                .deptName("정보시스템사업부")
//                .deptInfo("정보..")
//                .accountRole(AccountRole.INFO)
//                .build();
//
//        departmentRepository.save(department);

        Department department = Department.builder()
                .deptNum("D002")
                .deptName("인사총무부")
                .deptInfo("정보..")
                .accountRole(AccountRole.PERSON)
                .build();

        departmentRepository.save(department);

        Department department01 = Department.builder()
                .deptNum("D003")
                .deptName("재무IR기획부")
                .deptInfo("정보..")
                .accountRole(AccountRole.FINANCE)
                .build();

        departmentRepository.save(department01);

        Department department02 = Department.builder()
                .deptNum("D004")
                .deptName("감사실")
                .deptInfo("정보..")
                .accountRole(AccountRole.AUDIT)
                .build();

        departmentRepository.save(department02);




    }


}