package com.daesung.api.common.repository;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Department;
import com.daesung.api.common.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByMnNum(String mnNum);

    List<Manager> findByAccountRole(AccountRole accountRole);

    List<Manager> findByBusinessField(BusinessField businessField);

    Optional<Manager> findByMnDepartment(Department department);


}
