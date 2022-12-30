package com.daesung.api.common.repository;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.domain.Business;
import com.daesung.api.common.domain.BusinessField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BusinessFieldRepository extends JpaRepository<BusinessField, Long> {

    Optional<BusinessField> findByBusFieldName(String busFieldName);
    Optional<BusinessField> findByBusFieldNum(String busFieldNum);

    @Query("select b from BusinessField b where b.busFieldNum = :busFieldNum")
    BusinessField findByBusFieldNumber(String busFieldNum);

    Optional<BusinessField> findByAccountRole(AccountRole accountRole);


}
