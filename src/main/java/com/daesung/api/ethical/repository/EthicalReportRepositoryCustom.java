package com.daesung.api.ethical.repository;

import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EthicalReportRepositoryCustom {

    Page<EthicalReport> searchEthicalList(EthicalSearchCondition searchCondition, Pageable pageable);

}
