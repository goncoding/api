package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;


public interface HistoryRepository extends JpaRepository<History, Long>{

    @Query("select h " +
            "from History h " +
            "where :detailYear between h.hiStartYear and h.hiEndYear")
    History searchHistoryBetween(String detailYear);


}
