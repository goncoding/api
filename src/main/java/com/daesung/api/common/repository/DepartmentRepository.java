package com.daesung.api.common.repository;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {


    Optional<Department> findByDeptNum(String mnDeptNum);

    @Query("select d from Department d where d.deptNum = :mnDeptNum")
    Department findByDeptNumber(String mnDeptNum);


    Optional<Department> findByAccountRole(AccountRole accountRole);

}
