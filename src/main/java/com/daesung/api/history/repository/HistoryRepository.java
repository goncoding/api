package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
