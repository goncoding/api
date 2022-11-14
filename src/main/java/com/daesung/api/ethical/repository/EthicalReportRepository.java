package com.daesung.api.ethical.repository;

import com.daesung.api.ethical.domain.EthicalReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EthicalReportRepository extends JpaRepository<EthicalReport, Long> {
}
