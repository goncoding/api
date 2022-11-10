package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDetailRepository extends JpaRepository<HistoryDetail, Long> {
}
