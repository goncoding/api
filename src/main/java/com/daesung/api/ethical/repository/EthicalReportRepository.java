package com.daesung.api.ethical.repository;

import com.daesung.api.common.domain.Manager;
import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.daesung.api.ethical.web.dto.EthicalUpdateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EthicalReportRepository extends JpaRepository<EthicalReport, Long>, EthicalReportRepositoryCustom {

//    @Query("select er,mn " +
//            "from EthicalReport er " +
//            "left join fetch er.manager mn " +
//            "where er.id = :id")
//    EthicalUpdateResponseDto findEthicalReportAndManager(Long id);

    Page<EthicalReport> searchEthicalList(EthicalSearchCondition searchCondition, Pageable pageable);
}
